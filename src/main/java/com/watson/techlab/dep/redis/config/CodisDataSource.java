package com.watson.techlab.dep.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class CodisDataSource {
	   @Value("${spring.codis.zk-addr}")
	    private String zkAddr;

	    @Value("${spring.codis.zk-proxy-dir}")
	    private String zkProxyDir;

	    @Value("${spring.codis.password:}")
	    private String password;

	    @Value("${spring.codis.timeout}")
	    private int timeout;

	    @Value("${spring.codis.pool.max-active}")
	    private int max_active;

	    @Value("${spring.codis.pool.max-idle}")
	    private int max_idle;

	    @Value("${spring.codis.pool.min-idle}")
	    private int min_idle;

	    @Value("${spring.codis.pool.max-wait}")
	    private long max_wait;


	    @Bean
	    public JedisResourcePool getPool() {

	        JedisPoolConfig poolConfig = new JedisPoolConfig();
	        poolConfig.setMaxIdle(max_idle);
	        poolConfig.setMaxTotal(max_active);
	        poolConfig.setTestOnBorrow(true); //在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
	        poolConfig.setTestOnReturn(true); //在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。
	        poolConfig.setMaxWaitMillis(max_wait);
	        poolConfig.setBlockWhenExhausted(false); //连接耗尽的时候，是否阻塞，false 会抛出异常，true 阻塞直到超时。默认为true。

	        JedisResourcePool pool = (null == password || password.isEmpty())? 
	        		RoundRobinJedisPool.create().poolConfig(poolConfig)
	                .curatorClient(zkAddr, timeout)
	                .zkProxyDir(zkProxyDir).build()
	        		: RoundRobinJedisPool.create().poolConfig(poolConfig)
	                .curatorClient(zkAddr, timeout)
	                .password(password).zkProxyDir(zkProxyDir).build();

	        return pool;
	    }
}
