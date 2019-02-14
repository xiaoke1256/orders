package com.xiaoke1256.orders.common.util;

/**
 * 32进制数转换
 * @author Administrator
 *
 */
public class Base32 {
	private static final char[] BASE_NUMS = new char[] {
		'0','1','2','3','4','5','6'	,'7','8','9','A','B','C','D','E','F','G','H','J','K','L',
		'M','N','P','Q','R','T','U','V','W','X','Y'
	};
	
	public static String encode(int num) {
		String result = String.valueOf(BASE_NUMS[num%32]);
		while(num>=32) {
			num = num>>5;
			result = String.valueOf(BASE_NUMS[num])+result;
		}
		return result;
	}
	
}
