package test.utils.lambda;

import java.util.stream.Stream;

import org.junit.Test;

public class ReduceTest {
	@Test
	public void longestTest() {
		String result = Stream.of("Asssdf","dsfdgr ewer Gewer","Fewwrer trrt khgg","ewrerwr")
			.reduce("", (o,s)->{
					if(o.length()<s.length()) {
						return s;
					}
					return o;
				});
		System.out.println(result);
	}
}
