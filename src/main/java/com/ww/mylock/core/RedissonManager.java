package com.ww.mylock.core;


import com.ww.mylock.core.strategy.RedissonConfigStrategy;
import com.ww.mylock.core.strategy.impl.ClusterRedissonConfigStrategyImpl;
import com.ww.mylock.core.strategy.impl.MasterslaveRedissonConfigStrategyImpl;
import com.ww.mylock.core.strategy.impl.SentinelRedissonConfigStrategyImpl;
import com.ww.mylock.core.strategy.impl.StandaloneRedissonConfigStrategyImpl;
import com.ww.mylock.enums.RedisConnectionType;
import com.ww.mylock.prop.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.config.Config;

import java.util.Optional;


/**
 * Redisson配置管理器，用于初始化的redisson实例
 *
 * @author weiwang127
 */
@Slf4j
public class RedissonManager {

    private Config config = new Config();

    private Redisson redisson = null;

    public RedissonManager() {
    }

    public RedissonManager(RedissonProperties redissonProperties) {
        //装配开关
        Boolean enabled = redissonProperties.getEnabled();
        if (enabled) {
            try {
                config = RedissonConfigFactory.getInstance().createConfig(redissonProperties);
                redisson = (Redisson) Redisson.create(config);
            } catch (Exception e) {
                log.error("Redisson初始化错误", e);
            }
        }
    }

    public Redisson getRedisson() {
        return redisson;
    }

    /**
     * Redisson连接方式配置工厂
     * 双重检查锁
     */
    static class RedissonConfigFactory {

        private RedissonConfigFactory() {
        }

        private static volatile RedissonConfigFactory factory = null;

        public static RedissonConfigFactory getInstance() {
            if (factory == null) {
                synchronized (Object.class) {
                    if (factory == null) {
                        factory = new RedissonConfigFactory();
                    }
                }
            }
            return factory;
        }

        /**
         * 根据连接类型創建连接方式的配置
         *
         * @param redissonProperties
         * @return Config
         */
        Config createConfig(RedissonProperties redissonProperties) {
            Optional.ofNullable(redissonProperties).orElseThrow(() -> new RuntimeException("redissonProperties不能为空"));
            RedisConnectionType connectionType = redissonProperties.getType();
            // 声明连接方式
            RedissonConfigStrategy redissonConfigStrategy;
            if (connectionType.equals(RedisConnectionType.SENTINEL)) {
                redissonConfigStrategy = new SentinelRedissonConfigStrategyImpl();
            } else if (connectionType.equals(RedisConnectionType.CLUSTER)) {
                redissonConfigStrategy = new ClusterRedissonConfigStrategyImpl();
            } else if (connectionType.equals(RedisConnectionType.MASTERSLAVE)) {
                redissonConfigStrategy = new MasterslaveRedissonConfigStrategyImpl();
            } else {
                redissonConfigStrategy = new StandaloneRedissonConfigStrategyImpl();
            }
            Optional.of(redissonConfigStrategy).orElseThrow(() -> new RuntimeException("连接方式创建异常"));
            return redissonConfigStrategy.createRedissonConfig(redissonProperties);
        }
    }
}
