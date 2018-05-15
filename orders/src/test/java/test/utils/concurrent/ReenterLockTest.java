package test.utils.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可中断响应的重入锁。
 * @author TangJun
 *
 */
public class ReenterLockTest implements Runnable {
	
	private static ReentrantLock lock1 = new ReentrantLock();
	private static ReentrantLock lock2 = new ReentrantLock();

	int flag = 0;
	
	public ReenterLockTest(int flag) {
		super();
		this.flag = flag;
	}

	@Override
	public void run() {
		try {
			System.out.println(Thread.currentThread().getId()+":线程启动。flag="+flag);
			if(flag == 1){
				lock1.lockInterruptibly();
				System.out.println(Thread.currentThread().getId()+":lock1加锁。");
				try{
					Thread.sleep(1000);
				}catch (InterruptedException e) {}
				lock2.lockInterruptibly();
				System.out.println(Thread.currentThread().getId()+":lock2加锁。");
			}else{
				lock2.lockInterruptibly();
				System.out.println(Thread.currentThread().getId()+":lock2加锁。");
				try{
					Thread.sleep(1000);
				}catch (InterruptedException e) {}
				lock1.lockInterruptibly();
				System.out.println(Thread.currentThread().getId()+":lock1加锁。");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			if(lock1.isHeldByCurrentThread()){
				lock1.unlock();
			}
			if(lock2.isHeldByCurrentThread()){
				lock2.unlock();
			}
			System.out.println(Thread.currentThread().getId()+":线程退出。");
		}
	}
	
	public static void main(String[] args){
		Thread t1 = new Thread(new ReenterLockTest(0));
		Thread t2 = new Thread(new ReenterLockTest(1));
		t1.start();
		t2.start();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {}
		t2.interrupt();
	}
}
