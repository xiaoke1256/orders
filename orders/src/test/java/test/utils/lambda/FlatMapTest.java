package test.utils.lambda;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.junit.Test;


public class FlatMapTest {
	@Test
	public void connect(){
		String result = Arrays.asList("SSSSSS","DDDDD","KKKKKKK").stream().flatMap( s -> Arrays.stream(s.split(""))).collect(Collectors.joining(""));
		System.out.println(result);
	}
}
