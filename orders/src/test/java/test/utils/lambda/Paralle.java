package test.utils.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.junit.Test;

public class Paralle {
	@Test
	public void tetSumOfSquares() {
		long startTime =  System.currentTimeMillis();
		int sum = IntStream.range(100, 1000).map(x -> x*x).sum();
		long endTime =  System.currentTimeMillis();
		System.out.println(String.format("sum:%d; spend time:%d ms", sum,(endTime-startTime)));
		
		startTime =  System.currentTimeMillis();
		sum = IntStream.range(100, 1000).parallel().map(x -> x^2).sum();
		endTime =  System.currentTimeMillis();
		System.out.println(String.format("sum:%d; spend time:%d ms", sum,(endTime-startTime)));
	}
	
	@Test
	public void testMultiplyThrough() {
		int result = multiplyThrough(Arrays.asList(10,12,23,23,234,34,45,33,453));
		System.out.println(String.format("result:%d.", result));
		
		result = multiplyThroughWithParalle(Arrays.asList(10,12,23,23,234,34,45,33,453));
		System.out.println(String.format("result:%d.", result));
	}
	
	/**
	 * 并行化的效率问题
	 * @throws InterruptedException
	 */
	@Test
	public void testSumOfSquars() throws InterruptedException {
		Integer[] arr = new Integer[10000];
		Random r = new Random();
		Arrays.setAll(arr, (i)-> r.nextInt(100) ) ;
		 
		List<Integer> list = new ArrayList<Integer>(Arrays.asList(arr));
		List<Integer> linkedListOfNumbers = new LinkedList<Integer>(list);
		
		long startTime =  System.currentTimeMillis();
		Integer sum = linkedListOfNumbers.parallelStream().map(x -> x*x).reduce(0,(acc,x)-> acc+x);
		long endTime =  System.currentTimeMillis();
		System.out.println(String.format("sum:%d; spend time:%d ms", sum,(endTime-startTime)));
		
		Thread.sleep(1000);
		startTime =  System.currentTimeMillis();
		sum = list.parallelStream().map(x -> x*x).reduce(0,(acc,x)-> acc+x);
		endTime =  System.currentTimeMillis();
		System.out.println(String.format("sum:%d; spend time:%d ms", sum,(endTime-startTime)));
	}
	
	
	/**
	 * 此函数在并行时有问题
	 * @param linkedListOfNumbers
	 * @return
	 */
	private int multiplyThrough(List<Integer> linkedListOfNumbers) {
		return linkedListOfNumbers.stream().reduce(5, (acc,x) -> x*acc );
	}
	
	/**
	 * 改造成并行
	 * @param linkedListOfNumbers
	 * @return
	 */
	private int multiplyThroughWithParalle(List<Integer> linkedListOfNumbers) {
		List<Integer> list = new ArrayList<Integer>(linkedListOfNumbers);
		list.add(0, 5);
		return list.stream().parallel().reduce(1, (acc,x) -> x*acc );
	}
}
