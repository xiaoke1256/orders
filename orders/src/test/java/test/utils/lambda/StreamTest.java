package test.utils.lambda;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
		System.out.println("The longest String is :"+op.get());
	}
	
	private int addUp(Stream<Integer> s) {
		return s.reduce(0, (x,y)-> x+y).intValue();
	}
	
	private long lowCharCout(String s) {
		return s.chars().filter(c -> c>='a'&&c<='z' ).count();
	}
	
	private Optional<String> getMostLowCharsString(List<String> list){
		return list.stream().max(Comparator.comparing(s -> lowCharCout(s)));
	}
}
