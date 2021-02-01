package com.watson.techlab.dep.test.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.watson.techlab.dep.redis.utils.CacheUtil;

import redis.clients.jedis.Jedis;

@Controller
public class RedisController {
	 @Autowired
	    private CacheUtil cacheUtil;

	    /**
	     * 切换库 redisTemplate
	     * @param index
	     */
	    @ResponseBody
	    @RequestMapping("/redis/{index}")
	    public void stringTest(@PathVariable("index") int index){
	        RedisTemplate redisTemplate = cacheUtil.getRedisTemplate(0);  // 随时切换
	        try {
				Jedis jedis = cacheUtil.getCacheClient();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
//	        valueOperations.append("hello", "redis");
//	        valueOperations.set("hello2", 123);
	        valueOperations.set("hello", "redis", 300, TimeUnit.SECONDS);
	        valueOperations.set("hello2", 123, 450, TimeUnit.SECONDS);
	        Map<String,String> map = new HashMap<>();
	        map.put("a","av");
	        map.put("b","bv");
	        valueOperations.set("hello3", map, 360,TimeUnit.SECONDS);
	        System.out.println("useRedisDao = " + valueOperations.get("hello"));
	        System.out.println("useRedisDao = " + valueOperations.get("hello2"));
	        System.out.println("useRedisDao = " + valueOperations.get("hello3").getClass());
	        Map<String, String> mmm = (Map<String, String>) valueOperations.get("hello3");
	        System.out.println(mmm.get("a"));
	    }
}
