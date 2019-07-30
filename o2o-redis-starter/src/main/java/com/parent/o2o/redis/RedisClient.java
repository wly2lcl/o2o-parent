/**********************************************************      
  
 * 类名称：RedisClient   
 * 类描述：   
 * 创建时间：2019年4月22日 上午10:30:40   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.redis;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisCluster;

@Repository(value="redisClient")
@Slf4j
public class RedisClient implements IRedisClient {
	
    @Resource
    private JedisCluster jedisCluster;

	@Override
	public Long getInc(String key, Long step, long milliSeconds) throws RuntimeException {
		try {
			Long count = jedisCluster.incrBy(key, step);
			if (milliSeconds > 0) {
				jedisCluster.pexpire(key, milliSeconds);
			}
			return count;
		} catch (Exception e) {
			log.error("redis异常，不能正常获取序列数,key:" + key, e);
			throw new RuntimeException("redis异常，不能正常获取序列数", e);
		}
	}

	@Override
	public Long getInc(String key) throws RuntimeException {
		return getInc(key, 1L, -1);
	}

	@Override
	public Long getInc(String key, Long step) throws RuntimeException {
		return getInc(key, step, -1);
	}

	@Override
	public long del(String key) throws RuntimeException {
		return jedisCluster.del(key);
	}
	
	@Override
	public String getString(String key) {
		try {
			return jedisCluster.get(key);
		} catch (Exception e) {
			log.error("redis异常，不能正常获取String,key:" + key, e);
			return null;
		}
	}

}

  