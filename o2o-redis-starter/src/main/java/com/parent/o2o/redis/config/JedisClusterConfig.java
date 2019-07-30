/**********************************************************      
  
 * 类名称：JedisClusterConfig   
 * 类描述：   
 * 创建时间：2019年4月22日 下午3:37:15   
 * 修改备注：   
 *   
 **********************************************************/
package com.parent.o2o.redis.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisClusterConfig {
	
	@Value("${redis.host.cluster}")
	private String rediscluster;
	@Value("${redis.timeout}")
	private int timeout;	
	@Value("${redis.password}")
	private String password;	
	@Value("${redis.maxRedirects}")
	private int maxRedirects;	
	@Value("${redis.config.maxIdle}")
	private int maxIdle;
	@Value("${redis.config.minIdle}")
	private int minIdle;	
	@Value("${redis.config.maxWaitMillis}")
	private long maxWaitMillis;
	@Value("${redis.config.maxTotal}")
	private int maxTotal;
	@Value("${redis.config.testOnBorrow}")
	private boolean testOnBorrow;
	@Value("${redis.config.testOnReturn}")
	private boolean testOnReturn;
	
    @Bean
    public JedisCluster getJedisCluster() {
        String[] serverArray = rediscluster.split(",");
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        for (String ipPort: serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(),Integer.valueOf(ipPortPair[1].trim())));
        }
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);

        JedisCluster jedisCluster = new JedisCluster(nodes, timeout, timeout, maxRedirects, password,jedisPoolConfig);
        return jedisCluster;
    }
    
//    @SuppressWarnings("resource")
//	@Bean
//    public Jedis getJedis() {
//        String[] ipPortPair = rediscluster.split(":");
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMinIdle(minIdle);
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//        jedisPoolConfig.setMaxTotal(maxTotal);
//        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
//        jedisPoolConfig.setTestOnReturn(testOnReturn);
//        Jedis jedis = new JedisPool(jedisPoolConfig, ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim()), timeout, password).getResource();
//        return jedis;
//    }

}

  