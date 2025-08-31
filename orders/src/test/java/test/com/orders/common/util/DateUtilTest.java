package test.com.orders.common.util;

import java.util.Date;

import com.xiaoke1256.orders.common.util.DateUtil;
import org.junit.jupiter.api.Test;

public class DateUtilTest {
	@Test
	public void testFormat() {
		Date now = new Date();
		String s = DateUtil.format(now, "yyyyMMdd");
		System.out.println("format :" +s);
	}
}
