package test.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTaskTest extends RecursiveTask<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int THRESHOLD = 10000;
	private long start;
	private long end;
	
	public ForkJoinTaskTest(long start,long end){
		this.start = start;
		this.end = end;
	}
	

	@Override
	protected Long compute() {
		long sum = 0;
		boolean canCompute = (end - start)<THRESHOLD;
		if(canCompute){
			for(long i=start ;i<=end;i++){
				sum += i;
			}
		}else{
			//分成100个任务
			long step = (start+end)/100;
			List<ForkJoinTaskTest> subTasks = new ArrayList<ForkJoinTaskTest>();
			long pos=start;
			for(int i=0;i<100;i++){
				long lastOne = pos + step;
				if(lastOne>end)
					lastOne=end;
				ForkJoinTaskTest subTask = new ForkJoinTaskTest(pos,lastOne);
				pos+=step+1;
				subTasks.add(subTask);
			}
			for(ForkJoinTaskTest subTask:subTasks){
				sum += subTask.join();
			}
		}
		return sum;
	}
	
	public static void main(String[] args){
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTaskTest task = new ForkJoinTaskTest(0,200000L);
		ForkJoinTask<Long> result = pool.submit(task);
		try{
			long res = result.get();
			System.out.println(res);
		}catch(ExecutionException | InterruptedException e){
			e.printStackTrace();
		}finally{
			pool.shutdown();
		}
	}
}
