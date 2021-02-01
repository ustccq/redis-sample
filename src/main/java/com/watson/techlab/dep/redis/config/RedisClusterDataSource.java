package com.watson.techlab.dep.redis.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisClusterDataSource {
	@Value("${spring.cluster.host}")
    private String host;

    @Value("${spring.cluster.port}")
    private int port;

    @Value("${spring.cluster.timeout}")
    private int timeout;

    @Value("${spring.cluster.password}")
    private String password;

    @Value("${spring.cluster.pool.max-active}")
    private int max_active;

    @Value("${spring.cluster.pool.max-idle}")
    private int max_idle;

    @Value("${spring.cluster.pool.min-idle}")
    private int min_idle;

    @Value("${spring.cluster.pool.max-wait}")
    private long max_wait;

    /**
     * 连接redis的工厂类
     * @return
     */
    @Bean
    public JedisCluster getJedisCluster() {

    	JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(max_idle);
        jedisPoolConfig.setMaxTotal(max_active);
        jedisPoolConfig.setMinIdle(min_idle);
        jedisPoolConfig.setMaxWaitMillis(max_wait);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setBlockWhenExhausted(false);
        
		Set<HostAndPort> nodes = new HashSet<>();
		String[] endpoints = host.split(",");
		for (String endpoint : endpoints) {
			String[] addresses = endpoint.split(":");
			nodes.add(new HostAndPort(addresses[0], Integer.valueOf(addresses[1])));
		}
        return new JedisCluster(nodes, timeout, timeout, 3, password, jedisPoolConfig);
    }
}
