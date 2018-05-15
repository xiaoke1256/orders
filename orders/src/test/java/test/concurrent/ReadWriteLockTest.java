package test.concurrent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockTest {
	private static Lock lock = new ReentrantLock();
	private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private static Lock readLock = readWriteLock.readLock();
	private static Lock writeLock = readWriteLock.writeLock();
	private int value = 0;
	
	/**
	 * 模拟读操作
	 * @param lock
	 * @return
	 * @throws InterruptedException
	 */
	public Object handleRead(Lock lock) throws InterruptedException{
		try{
			lock.lock();
			Thread.sleep(1000);
			return value;
		}finally{
			lock.unlock();
		}
	}
	
	/**
	 * 模拟写操作
	 * @param lock
	 * @throws InterruptedException 
	 */
	public void  handleWrite(Lock lock,int newValue) throws InterruptedException{
		try{
			lock.lock();
			Thread.sleep(1000);
			value = newValue;
		}finally{
			lock.unlock();
		}
	}
	
	public static void main(String[] args){
		final ReadWriteLockTest demo = new ReadWriteLockTest();
		Runnable readRunnal = new Runnable(){
			SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.S");
			@Override
			public void run() {
				try {
					Object val = demo.handleRead(readLock);
//					Object val = demo.handleRead(lock);
					System.out.println("current time is :"+format.format(new Date())+".current thread id is:"+Thread.currentThread().getId()+". the value is :"+val);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		Runnable writeRunnal = new Runnable(){
			SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.S");
			@Override
			public void run() {
				try {
					demo.handleWrite(writeLock, new Random().nextInt());
//					demo.handleWrite(lock, new Random().nextInt());
					System.out.println("current time is :"+format.format(new Date())+".current thread id is:"+Thread.currentThread().getId()+".has setted new value. ");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		
		for(int i=0;i<18;i++){
			new Thread(readRunnal).start();
		}
		
		for(int i=18;i<20;i++){
			new Thread(writeRunnal).start();
		}
	}
}
