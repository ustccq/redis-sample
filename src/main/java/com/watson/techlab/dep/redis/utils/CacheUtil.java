package com.watson.techlab.dep.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import io.codis.jodis.JedisResourcePool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

@Component
public class CacheUtil {
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private JedisResourcePool jedisResourcePool;

    @Autowired
    private JedisPool jedisPool;
    
    @Autowired
    private JedisCluster jedisCluster;

    @Value("${spring.redis.database}")
    private int redis_database;

    @Value("${spring.codis.database}")
    private int codis_database;

    @Value("${spring.cache.index}")
    private int cache_index;

    /**
     * 指定redis库
     */
    public RedisTemplate getRedisTemplate(int index) {
        return getTemplate(redisTemplate, index);
    }

    /**
     * 获取SpringRedisTemplate
     */
    public RedisTemplate getTemplate(RedisTemplate redisTemplate, int index) {
        jedisConnectionFactory.setDatabase(index);
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 获取原生redis/codis client
     * @return
     * @throws Exception 
     */
    public Jedis getCacheClient() throws Exception{

        Jedis jedis = null;
        if(cache_index==1){  // jedis 1
            jedis = jedisPool.getResource();
            jedis.select(redis_database);  // 设置默认库
        }else if(cache_index==2){  // codis 2
            jedis = jedisResourcePool.getResource();
            jedis.select(codis_database);  // 设置默认库
        }else if(cache_index==3){  // jedis cluster 3
        	
        }else {  // exception
            throw new Exception("please select index in {1:redis,2:codis} ! your select index is : "+cache_index);
        }
        return jedis;
    }
}
