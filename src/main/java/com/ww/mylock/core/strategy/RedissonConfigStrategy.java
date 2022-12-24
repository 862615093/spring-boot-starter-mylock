package com.ww.mylock.core.strategy;

import com.ww.mylock.prop.RedissonProperties;
import org.redisson.config.Config;

/**
 * Redisson配置构建接口
 *
 * @author weiwang127
 */
public interface RedissonConfigStrategy {

	/**
	 * 根据不同的Redis配置策略创建对应的Config
	 *
	 * @param redissonProperties
	 * @return Config
	 */
	Config createRedissonConfig(RedissonProperties redissonProperties);
}
