package test.com.orders.service;

import org.apache.commons.lang.StringUtils;

public class GenOrderNoTest {
	public static void main(String[] args) {
		StringBuffer orderNoBffer = new StringBuffer();
		long nanoSecode = System.nanoTime();//这是纳秒，需要转成微妙。
		System.out.println(nanoSecode);
		System.out.println(nanoSecode/1000);
		System.out.println((nanoSecode/1000)%1000);
		int microSecond = (int)((nanoSecode/1000)%1000);
		System.out.println(microSecond);
		orderNoBffer.append(StringUtils.leftPad(String.valueOf(microSecond), 3, '0'));
		System.out.println(orderNoBffer.toString());
	}
}
