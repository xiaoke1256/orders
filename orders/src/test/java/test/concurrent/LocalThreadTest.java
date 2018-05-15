package test.concurrent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalThreadTest {
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	
	private static DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.S");
	
	private static volatile ThreadLocal<DateFormat> localDateFormat = new ThreadLocal<DateFormat>();
	
	public static void main(String[] args){
		
		//以下任务并发时会报异常
		for(int i = 0 ; i<100; i++){
			Runnable task = new Runnable(){
				@Override
				public void run() {
					try {
						Random radom = new Random();
						int randomI = radom.nextInt(100);
						Date date = dateFormat.parse("2016-06-17 13:14:30."+(randomI%100));
						System.out.println(randomI+":date :"+date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				}
				
			};
			pool.execute(task);
			//task.run();
		}
		
		//休眠1秒
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("after sleep.");
		
		//用localDateFormat解决
		for(int i = 0 ; i<100; i++){
			Runnable task = new Runnable(){
				@Override
				public void run() {
					try {
						if(localDateFormat.get()==null)
							localDateFormat.set(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.S"));
						Random radom = new Random();
						int randomI = radom.nextInt(100);
						Date date = localDateFormat.get().parse("2016-06-17 13:14:30."+(randomI%100));
						System.out.println(randomI+":date :"+date);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
				}
				
			};
			pool.execute(task);
			//task.run();
		}
	}
}
