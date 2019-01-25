package test.utils.lambda;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class StreamTest {
	@Test
	public void testAddUp() {
		int sum = addUp(Stream.of(1,2,3,4,5,6));
		System.out.println("sum is :"+sum);
	}
	
	@Test
	public void testLowCharCount(){
		long count = lowCharCout("sadRWE32sdfTew");
		System.out.println("count is :"+count);
	}
	
	@Test
	public void testGetMostLowCharsString() {
		Optional<String> op = getMostLowCharsString(Arrays.asList("ADSWEWEW","DDDDDwDD"));
		if(op.isPresent())
			System.out.println("The longest String is :"+op.get());
		else
			System.out.println("The longest String is empty.");
		
		op = getMostLowCharsString(Arrays.asList(new String[0]));
		if(op.isPresent())
			System.out.println("The longest String is :"+op.get());
		else
			System.out.println("The longest String is empty.");
	}
	
	/**
	 * 分组
	 */
	@Test
	public void testGroupBy() {
		 Map<Object, Long> result = Arrays.asList("A","B","C","DD","A","D","C","CC","DD").stream()
				.collect(Collectors.groupingBy(s->s,Collectors.counting()));
		 for(Map.Entry<Object, Long> entry:result.entrySet()) {
			  String key = entry.getKey().toString();
			  Long value  = entry.getValue();
			  System.out.println(key +" -> "+ value);
		 }
	}
	
	/**
	 * 总和
	 * @param s
	 * @return
	 */
	private int addUp(Stream<Integer> s) {
		return s.reduce(0, (x,y)-> x+y).intValue();
	}
	
	/**
	 * 求小写字符的数量
	 * @param s
	 * @return
	 */
	private long lowCharCout(String s) {
		return s.chars().filter(c -> c>='a'&&c<='z' ).count();
	}
	
	/**
	 * 获取小写字符最多的字符串
	 * @param list
	 * @return
	 */
	private Optional<String> getMostLowCharsString(List<String> list){
		return list.stream().max(Comparator.comparing(s -> lowCharCout(s)));
	}
}
