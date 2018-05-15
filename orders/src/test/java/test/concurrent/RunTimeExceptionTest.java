package test.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RunTimeExceptionTest {
	//private static ExecutorService exec = Executors.newFixedThreadPool(10);
	private static ExecutorService exec = new TraceThreadPoolExecutor(0,Integer.MAX_VALUE,0L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
	
	public static class DemoTask implements Runnable{
		int a,b;
		public DemoTask(int a,int b){
			this.a=a;
			this.b=b;
		}
		@Override
		public void run() {
			int re = a/b;
			System.out.println(re);
		}
		
	}
	
	public static void main(String[] args){
		for(int i=0;i<5;i++){
			//exec.submit(new DemoTask(100,i));
			exec.execute(new DemoTask(100,i));
		}
		exec.shutdown();
	}
	
	public static class TraceThreadPoolExecutor extends ThreadPoolExecutor{

		public TraceThreadPoolExecutor(int arg0, int arg1, long arg2,
				TimeUnit arg3, BlockingQueue<Runnable> arg4) {
			super(arg0, arg1, arg2, arg3, arg4);
		}

		@Override
		public Future<?> submit(Runnable task) {
			return super.submit(wrap(task,clientTrace(),Thread.currentThread().getName()));
		}

		
		@Override
		public void execute(Runnable command) {
			super.execute(wrap(command,clientTrace(),Thread.currentThread().getName()));
		}

		private Runnable wrap(Runnable command, Exception clientTrace, String name) {
			// TODO Auto-generated method stub
			return new Runnable(){
				public void run(){
					try{
						command.run();
					}catch(Exception e){
						clientTrace.printStackTrace();
						throw e;
					}
				}
			};
		}

		private Exception clientTrace() {
			return new Exception("Runnable Exception trace");
		}
		
	}
}
