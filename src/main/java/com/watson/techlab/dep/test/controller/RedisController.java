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
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.util.JedisClusterCRC16;
import redis.clients.jedis.util.SafeEncoder;

@Controller
public class RedisController {
	@Autowired
	private CacheUtil cacheUtil;

	@ResponseBody
	@RequestMapping("/redis/put")
	public void simpleTest() {
		Jedis cacheClient = null;
		JedisCluster cacheCluster = null;
		
		String ckey = "ctesttime";
		String key = "testtime";
		try {
			if (cacheUtil.isCluster()) {
				cacheCluster = cacheUtil.getCacheCluster();
				cacheCluster.set(ckey,  String.valueOf(System.currentTimeMillis()));
				cacheCluster.expire(ckey,  600);
				
				byte[] keyCode  = SafeEncoder.encode(ckey);
				Jedis tmpJedis = cacheCluster.getConnectionFromSlot(JedisClusterCRC16.getSlot(keyCode));
				System.out.println("Jedis . get:" + tmpJedis.get(ckey));
				System.out.println("JeduCluster . get:" + cacheCluster.get(ckey));
			} else {
				cacheClient = cacheUtil.getCacheClient();
				cacheClient.set(key, String.valueOf(System.currentTimeMillis()));
				cacheClient.expire(key, 300);
				
				System.out.println("redis . get:" + cacheClient.get(key));
			}
			// 进行各种操作.........
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (null != cacheClient) {
				cacheClient.close();
			}
			if (null != cacheCluster) {
				cacheCluster.close();
			}
		}
	}

	/**
	 * 切换库 redisTemplate
	 * 
	 * @param index
	 */
//	@ResponseBody
//	@RequestMapping("/redis/{index}")
//	public void stringTest(@PathVariable("index") int index) {
////	        useRedisTemplate();
//	}
//
//	private void useRedisTemplate() {
//		RedisTemplate redisTemplate = cacheUtil.getRedisTemplate(0); // 随时切换
//		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
//		valueOperations.set("hello", "redis", 300, TimeUnit.SECONDS);
//		valueOperations.set("hello2", 123, 450, TimeUnit.SECONDS);
//		Map<String, String> map = new HashMap<>();
//		map.put("a", "av");
//		map.put("b", "bv");
//		valueOperations.set("hello3", map, 360, TimeUnit.SECONDS);
//		System.out.println("useRedisDao = " + valueOperations.get("hello"));
//		System.out.println("useRedisDao = " + valueOperations.get("hello2"));
//		System.out.println("useRedisDao = " + valueOperations.get("hello3").getClass());
//		Map<String, String> mmm = (Map<String, String>) valueOperations.get("hello3");
//		System.out.println(mmm.get("a"));
//	}
}
