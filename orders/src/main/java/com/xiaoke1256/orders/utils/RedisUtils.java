package com.xiaoke1256.orders.utils;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

public class RedisUtils {
	private static JedisPool pool;
	 
	private static void createPool() {
		// 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxIdle(100);
        // 设置最大阻塞时间，记住是毫秒数milliseconds
        config.setMaxWaitMillis(1000);
        // 设置空闲连接
        config.setMaxIdle(10);
        
        String password = null;
		// 创建连接池
        pool = new JedisPool(config, "192.168.66.100", 6379,10000,password );

	} 
	
	private static synchronized void poolInit() {
        if (pool == null)
        	createPool();
    }

	
	public static Jedis connect() {
		if (pool == null)
			poolInit();
        return pool.getResource();
	}
	
	public static void set(Jedis jedis,String key,String value) {
		String result = jedis.set(key, value);
		if(!"OK".equals(result)) {
			throw new RuntimeException(String.format("Redis has something wrong. Reply is : %s", result));
		}
	}
	
	/**
	 * 
	 * @param jedis
	 * @param key
	 * @param value
	 * @param time expired time as seconds.
	 */
	public static void set(Jedis jedis,String key,String value,long time) {
		String result = jedis.set(key, value,"XX","EX",time);
		if(!"OK".equals(result)) {
			throw new RuntimeException(String.format("Redis has something wrong. Reply is : %s", result));
		}
	}
	
	public static String get(Jedis jedis,String key) {
		return jedis.get(key);
	}
	
	public static void del(Jedis jedis,String key) {
		Long result = jedis.del(key);
		if(result<=0) {
			throw new RuntimeException(String.format("The specified key \"%s\" has not existed.", key));
		}
	}
	
	public static Transaction multi(Jedis jedis) {
		Transaction t = jedis.multi();
		return t;
	}
	
	public static List<Object> exec(Transaction t) {
		List<Object> results = t.exec();
		if(results==null || results.size()==0) {
			throw new RuntimeException("no commad has bean execute maybe something wrong in this transaction");
		}
		if(results!=null) {
			for(Object obj:results) {
				System.out.println("exec result:"+obj);
				if(obj != null) {
					System.out.println("exec result class:"+obj.getClass());
					if(obj instanceof JedisException) {
						throw (JedisException)obj;
					}else if(obj instanceof Exception) {
						throw new RuntimeException((Exception)obj);
					}
				}
			}
		}
		return results;
	}
	
	public static void watch(Jedis jedis ,String... keys) {
		String result = jedis.watch(keys);
		if(!"OK".equals(result)) {
			throw new RuntimeException(String.format("Redis has something wrong. Reply is : %s", result));
		}
	}
	
	public static void incr(Jedis jedis,String key) {
		Long result = jedis.incr(key);
		if(result<=0) {
			throw new RuntimeException(String.format("The specified key \"%s\" has not existed.", key));
		}
	}
	
	public static void incrBy(Jedis jedis,String key,long integer) {
		Long result = jedis.incrBy(key, integer);
		if(result<=0) {
			throw new RuntimeException(String.format("The specified key \"%s\" has not existed.", key));
		}
	}
	
}
