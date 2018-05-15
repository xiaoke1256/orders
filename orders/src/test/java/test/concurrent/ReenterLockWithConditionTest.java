package test.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 带有Condition的重入锁。
 * @author TangJun
 *
 */
public class ReenterLockWithConditionTest implements Runnable {
	
	
	private static ReentrantLock lock = new ReentrantLock();
	private static Condition condition = lock.newCondition();

	@Override
	public void run() {
		try {
			lock.lock();
			condition.await();
			System.out.println("Thread is going on.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException{
		Thread t1 = new Thread(new ReenterLockWithConditionTest());
		t1.start();
		Thread.sleep(2000);
		lock.lock();
		condition.signal();
		lock.unlock();
		System.out.println("Main thread is finished.");
	}
}
