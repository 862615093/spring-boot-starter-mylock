package com.ww.mylock.core.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.ww.mylock.core.strategy.RedissonConfigStrategy;
import com.ww.mylock.enums.GlobalConstant;
import com.ww.mylock.prop.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.config.Config;


/**
 * 集群方式Redisson配置
 * cluster方式至少6个节点(3主3从)
 * 配置方式:127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381,127.0.0.1:6382,127.0.0.1:6383,127.0.0.1:6384
 *
 */
@Slf4j
public class ClusterRedissonConfigStrategyImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            String[] addrTokens = address.split(",");
            // 设置集群(cluster)节点的服务IP和端口
            for (int i = 0; i < addrTokens.length; i++) {
                config.useClusterServers().addNodeAddress(GlobalConstant.REDIS_CONNECTION_PREFIX + addrTokens[i]);
                if (StrUtil.isNotBlank(password)) {
                    config.useClusterServers().setPassword(password);
                }
            }
            log.info("初始化集群方式Config,连接地址:" + address);
        } catch (Exception e) {
            log.error("集群Redisson初始化错误", e);
            e.printStackTrace();
        }
        return config;
    }
}
