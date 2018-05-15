package test.concurrent;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {
	static final CountDownLatch end = new CountDownLatch(10);
	
	static Runnable task = new Runnable(){

		@Override
		public void run() {
			try{
				System.out.println("Thread "+Thread.currentThread().getId()+" is start checking...");
				Thread.sleep(new Random().nextInt(10)*1000);
				System.out.println("Thread "+Thread.currentThread().getId()+":check complete!");
				end.countDown();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}};
	
	public static void main(String[] args) throws InterruptedException{
		ExecutorService exec = Executors.newFixedThreadPool(10);
		for(int i=0;i<10;i++){
			exec.submit(task);
		}
		end.await();
		System.out.println("Fire!");
		exec.shutdown();
	}
}
