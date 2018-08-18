package com.xiaoke1256.orders.utils;

import java.util.List;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

public class RedisUtils {
	private static  final Logger logger = Logger.getLogger(RedisUtils.class);
	
	private static JedisPool pool;
	 
	private static void createPool() {
		// 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxIdle(100);
        //最大连接数, 默认8个
        config.setMaxTotal(500);
        
        // 设置最大阻塞时间，记住是毫秒数milliseconds
        config.setMaxWaitMillis(10000);
        
        //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        config.setBlockWhenExhausted(true);
         
        //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
         
        //是否启用pool的jmx管理功能, 默认true
        config.setJmxEnabled(true);
         
        //MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i); 默 认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
        config.setJmxNamePrefix("pool");
         
        //是否启用后进先出, 默认true
        config.setLifo(true);
         
        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        config.setMinEvictableIdleTimeMillis(1800000);
         
        //最小空闲连接数, 默认0
        config.setMinIdle(0);
         
        //每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(3);
         
        //对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)   
        config.setSoftMinEvictableIdleTimeMillis(1800000);
         
        //在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(false);
         
        //在空闲时检查有效性, 默认false
        config.setTestWhileIdle(false);
         
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(100);
        
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
				logger.debug("exec result:"+obj);
				if(obj != null) {
					logger.debug("exec result class:"+obj.getClass());
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
	
	public static void unwatch(Jedis jedis) {
		String result = jedis.unwatch();
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
