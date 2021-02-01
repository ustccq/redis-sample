package com.watson.techlab.dep.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class SpringRedisTemplate extends CachingConfigurerSupport {
	 @Value("${spring.redis.host}")
	    private String host;

	    @Value("${spring.redis.port}")
	    private int port;

	    @Value("${spring.redis.timeout}")
	    private int timeout;

	    @Value("${spring.redis.database}")
	    private int database;

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
	    public JedisConnectionFactory jedisConnectionFactory() {
	        JedisConnectionFactory factory = new JedisConnectionFactory();
	        factory.setHostName(host);
	        factory.setPort(port);
	        factory.setTimeout(timeout);
	        factory.setPassword(password);
	        factory.setDatabase(database);
	        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	        jedisPoolConfig.setMaxIdle(max_idle);
	        jedisPoolConfig.setMaxTotal(max_active);
	        jedisPoolConfig.setMinIdle(min_idle);
	        jedisPoolConfig.setMaxWaitMillis(max_wait);
	        jedisPoolConfig.setTestOnBorrow(true);
	        jedisPoolConfig.setTestOnReturn(true);
	        jedisPoolConfig.setBlockWhenExhausted(false);
	        factory.setPoolConfig(jedisPoolConfig);
	        return factory;
	    }
	    

	    /**
	     * 配置RedisTemplate
	     * 设置添加序列化器
	     * key 使用string序列化器
	     * value 使用Json序列化器
	     * 还有一种简答的设置方式，改变defaultSerializer对象的实现。
	     * RedisTemplate提供了以下几种序列化器，我们可以根据项目的需求进行选择。
	     * RedisSerializer redis序列化的接口类
	     * OxmSerializer xml到object的序列化/反序列化
	     * StringRedisSerializer string字符串的序列化/反序列化
	     * JacksonJsonRedisSerializer json到object的序列化/反序列化
	     * Jackson2JsonRedisSerializer json到object的序列化/反序列化
	     * JdkSerializationRedisSerializer java对象的序列化/反序列化
	     *
	     * @return
	     */
	    @Bean
	    public org.springframework.data.redis.core.RedisTemplate<String, Object> getRedisTemplate() {
	        //StringRedisTemplate的构造方法中默认设置了stringSerializer
	        org.springframework.data.redis.core.RedisTemplate<String, Object> template = new org.springframework.data.redis.core.RedisTemplate<>();
	        //set key serializer
	        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	        template.setKeySerializer(stringRedisSerializer);
	        template.setHashKeySerializer(stringRedisSerializer);

	        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
	        ObjectMapper objectMapper = new ObjectMapper();
	        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

	        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
	        //set value serializer
	        template.setDefaultSerializer(jackson2JsonRedisSerializer);

	        template.setConnectionFactory(jedisConnectionFactory());
	        template.afterPropertiesSet();
	        return template;
	    }
}
