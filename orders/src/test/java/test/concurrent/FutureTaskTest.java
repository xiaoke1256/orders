package test.concurrent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

public class FutureTaskTest {
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	
	private static DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.S");
	
	public static void main(String[] args){
		final AtomicInteger index = new AtomicInteger(0);
		
		List<FutureTask<Object>> taskList = new ArrayList<FutureTask<Object>>();
		
		System.out.println("ִ�п�ʼʱ�䣺"+dateFormat.format(new Date()));
		//��ʼ��100������
		for(int i = 0 ; i<100; i++){
			FutureTask<Object> task = new FutureTask<Object>(new Callable<Object>(){
				@Override
				public Object call() throws Exception {
					Random radom = new Random();
					int ms = radom.nextInt(100);
					if(ms<20)//���������ִ��ʱ���ر𳤵�
						ms = 1800;
					System.out.println(System.currentTimeMillis()+":������"+index.incrementAndGet()+"������������"+ms+"���롣");
					Thread.sleep(ms);
					return Boolean.TRUE;
				}});
			pool.execute(task);
			
			taskList.add(task);
		}
		
		
		
		for(int i = 0 ; i<100; i++){
			try {
				FutureTask<Object> task = taskList.get(i);
				System.out.println(System.currentTimeMillis()+":��⵽��"+i+"������ִ����ϣ�ִ�н����"+task.get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
		System.out.println(System.currentTimeMillis()+":�������񶼽����ˡ�ִ�н���ʱ�䣺"+dateFormat.format(new Date()));
	}
}
