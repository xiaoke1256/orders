package test.utils.concurrent;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BlockingQueueTest {
	private static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(5);
	private static ExecutorService pool = Executors.newFixedThreadPool(100);
	
	private static volatile boolean isProductStoped = false;
	private static volatile boolean isConsumerStoped = false;
	
	//生产者
	private static class Productor implements Runnable{
		private Random radom = new Random();
		
		@Override
		public void run() {
			try {
				while(!isProductStoped){
					String element = "ele"+radom.nextInt();
					queue.put(element);
					System.out.println(Thread.currentThread().getId()+":The '"+element+"' has bean insert into the Queue.");
					Thread.sleep(10);//休眠10毫秒
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getId()+":the product is finished.");
		}
	}
	
	//消费者
	private static class Consumer implements Runnable{

		@Override
		public void run() {
			try {
				while(!isConsumerStoped){
					String element = queue.poll();
					if(element==null){
						//队列是空的
						Thread.sleep(100);//休眠100毫秒再取。
						continue;
					}
					System.out.println(Thread.currentThread().getId()+":The '"+element+"' has bean remove from the Queue.");
					Thread.sleep(10);//休眠10毫秒
					System.out.println(Thread.currentThread().getId()+":consume a ele");
				}
				
				String element = null;
				while(true){
					element = queue.poll(100, TimeUnit.MILLISECONDS);//100毫秒后再取不到就认为队列已经空了。
					if(element==null)
						break;
					System.out.println(Thread.currentThread().getId()+":The '"+element+"' has bean remove from the Queue.");
					Thread.sleep(10);//休眠10毫秒
					System.out.println(Thread.currentThread().getId()+":consume a ele");
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getId()+":the Consumer is finished.");
		}
	}
	
	public static void main(String[] args) throws InterruptedException{
		//初始化50个生产者
		for(int i=0;i<5;i++)
			pool.submit(new Productor());
		
		//初始化50个消费者
		for(int i=0;i<5;i++)
			pool.submit(new Consumer());
		
		//休眠10秒
		Thread.sleep(10000);
		
		//先停掉消费者。
		isConsumerStoped = true;
		
		Thread.sleep(100);
		
		//再停掉生产者
		isProductStoped = true;
		
		//
		pool.shutdown();
		pool.awaitTermination(10, TimeUnit.MINUTES);
	}
}
