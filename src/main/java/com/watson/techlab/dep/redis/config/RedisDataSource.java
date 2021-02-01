package com.watson.techlab.dep.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisDataSource {
	@Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.pool.max-active}")
    private int max_active;

    @Value("${spring.redis.pool.max-idle}")
    private int max_idle;

    @Value("${spring.redis.pool.min-idle}")
    private int min_idle;

    @Value("${spring.redis.pool.max-wait}")
    private long max_wait;

    /**
     * 连接redis的工厂类
     * @return
     */
    @Bean
    public JedisPool getJedisPool() {
    	JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(max_idle);
        jedisPoolConfig.setMaxTotal(max_active);
        jedisPoolConfig.setMinIdle(min_idle);
        jedisPoolConfig.setMaxWaitMillis(max_wait);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setBlockWhenExhausted(false);
        return new JedisPool(jedisPoolConfig, host, port, timeout, password);
    }
}