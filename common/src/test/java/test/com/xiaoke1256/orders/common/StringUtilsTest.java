package test.com.xiaoke1256.orders.common;


import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class StringUtilsTest {
	@Test
	public void testLeftPad() {
		System.out.println(StringUtils.leftPad("3", 2, '0'));
		
		System.out.println(StringUtils.leftPad("423", 2, '0'));
	}
}
