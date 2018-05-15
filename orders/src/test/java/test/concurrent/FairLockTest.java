package test.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁测试
 * @author TangJun
 *
 */
public class FairLockTest implements Runnable {
	private static ReentrantLock lock = new ReentrantLock(true);
	
	private boolean isStoped = false;

	@Override
	public void run() {
		while(!isStoped){
			try {
				lock.lock();
				System.out.println("线程"+Thread.currentThread().getId()+"获得锁。");
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally{
				lock.unlock();
			}
		}
	}
	
	public static void main(String[] args){
		FairLockTest fl = new FairLockTest();
		Thread t1 = new Thread(fl);
		Thread t2 = new Thread(fl);
		t1.start();
		t2.start();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {}
		fl.isStoped = true;
	}
}
