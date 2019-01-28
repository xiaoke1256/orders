package test.utils.redis;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.xiaoke1256.common.utils.RedisUtils;

import junit.framework.Assert.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RedisTest {
	
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	
	@Test
	public void testIncr() {
		Jedis jedis = RedisUtils.connect();
		jedis.set("a","10");
		jedis.incr("a");
		String reslt = jedis.get("a");
		System.out.println(reslt); 
		jedis.close();
	}
	
	@Test
	public void testTrancation() {
		Jedis jedis = RedisUtils.connect();
		Transaction t = RedisUtils.multi(jedis);
		t.set("a","10");//若此处改成 t.set("a","a10");，exec时将会报错。
		t.incr("a");
		RedisUtils.exec(t);
		String reslt = jedis.get("a");
		System.out.println(reslt); 
		jedis.close();
	}
	
	@Test
	public void testTrancationWithWatch() {
		Jedis jedis = RedisUtils.connect();
		//初始化a为10
		jedis.set("a","10");
		jedis.watch("a");
		int i = Integer.parseInt(jedis.get("a"));
		Transaction t = RedisUtils.multi(jedis);
		i++;
		t.set("a",String.valueOf(i));
		RedisUtils.exec(t);
		String reslt = jedis.get("a");
		System.out.println(reslt); 
		jedis.close();
	}
	
	/**
	 * 多线程下事务失败的情况
	 * @throws InterruptedException 
	 */
	@Test
	public void testMultiTrancation() throws InterruptedException {
		Jedis j = RedisUtils.connect();//确保连接池以初始化。
		pool.submit(()->{
			Jedis jedis = RedisUtils.connect();
			try {
				//初始化a为10
				jedis.set("a","10");
				jedis.watch("a");
				System.out.println("Thread1 start watch!");
				int i = Integer.parseInt(jedis.get("a"));
				Transaction t = RedisUtils.multi(jedis);
				i++;
				t.set("a",String.valueOf(i));
				Thread.sleep(200);
				System.out.println("Thread1 going to exec!");
				RedisUtils.exec(t);
				String reslt = jedis.get("a");
				System.out.println("Thread1 result:"+reslt);
			}catch(Exception ex) {
				System.out.println("thread1 has something wrong.");
				ex.printStackTrace();
			}finally {
				jedis.close();
			}
		});
		
		pool.submit(()->{
			Jedis jedis = RedisUtils.connect();
			try {
				Thread.sleep(100);
				//修改a为40
				jedis.set("a","40");
				System.out.println("Thread2 has reset 'a' to 40.");
			} catch (Exception e) {
				System.out.println("thread2 has something wrong.");
				e.printStackTrace();
			}finally {
				jedis.close();
			}

		});
		
		pool.shutdown();
		pool.awaitTermination(10, TimeUnit.MINUTES);
		
		String reslt = j.get("a");
		System.out.println("finally result:"+reslt);
		Assert.assertNotEquals("11", reslt);
	}
}
