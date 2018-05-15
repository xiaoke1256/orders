package test.utils.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
//import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CopyOnWriteArrayListTest {
	private static ExecutorService pool = Executors.newFixedThreadPool(50);
	
	//static List<String> list = new CopyOnWriteArrayList<String>();
	static List<String> list = Collections.synchronizedList(new ArrayList<String>());
	
	public static void main(String[] args){
		//200个Add线程
		for(int i=0;i<200;i++){
			pool.execute(new AddTask());
		}
		//150个Delete线程
		for(int i=0;i<150;i++){
			pool.execute(new DeleteTask());
		}
		//800个Read线程
		for(int i=0;i<800;i++){
			pool.execute(new ReadTask());
		}
		
		pool.shutdown();
		
	}
	
	private static class AddTask implements Runnable{

		@Override
		public void run() {
			System.out.println("start add at "+System.currentTimeMillis());
			list.add(String.valueOf(new Random().nextInt(100)));
			list.add(String.valueOf(new Random().nextInt(100)));
			System.out.println("end add at "+System.currentTimeMillis());
		}
		
	}
	
	private static class DeleteTask implements Runnable{

		@Override
		public void run() {
			System.out.println("start delete at "+System.currentTimeMillis());
			if(list.isEmpty())
				return;
			list.remove(new Random().nextInt(list.size()));
			System.out.println("start delete at "+System.currentTimeMillis());
		}
		
	}
	
	private static class ReadTask implements Runnable{

		@Override
		public void run() {
			System.out.println("start read at "+System.currentTimeMillis());
			if(list.isEmpty())
				return;
			String value = list.get(new Random().nextInt(list.size()));
			System.out.println("value:"+value);
			System.out.println("start read at "+System.currentTimeMillis());
		}
		
	}
	
}
