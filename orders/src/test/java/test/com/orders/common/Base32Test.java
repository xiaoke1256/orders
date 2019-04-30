package test.com.orders.common;

import org.junit.Test;

import com.xiaoke1256.orders.common.util.Base32;

public class Base32Test {
	
	@Test
	public void testZero() {
		System.out.println(Base32.encode(0));
	}
	
	@Test
	public void test32() {
		System.out.println(Base32.encode(32));
	}
	
	@Test
	public void test33() {
		System.out.println(Base32.encode(33));
	}
	
	@Test
	public void test31() {
		System.out.println(Base32.encode(31));
	}
	
	@Test
	public void test1025() {
		System.out.println(Base32.encode(1025));
	}
	
	
	@Test
	public void testMuse() {
		System.out.println(Base32.encode(-1));
	}
}
