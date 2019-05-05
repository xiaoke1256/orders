package com.xiaoke1256.orders.common;

import java.io.Serializable;

/**
 * 错误代码，实际上就是一个枚举类型
 * @author Administrator
 *
 */
public class RespCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private String msg;
	
	/**
	 * 禁用构造方法
	 */
	@SuppressWarnings("unused")
	private RespCode() {
		
	}

	protected RespCode(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RespCode other = (RespCode) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	/**错误代码:操作成功 */
	public static final RespCode SUCCESS = new RespCode("00","Success!");
	
	/**错误代码:业务异常 */
	public static final RespCode BUSSNESS_ERROR = new RespCode("80","Bussness exception!");
	
	/**错误代码:网络连接异常 */
	public static final RespCode CONNECT_ERROR = new RespCode("11","Net connect error!");
	
	/**错误代码:空参数错误 */
	public static final RespCode EMPTY_PARAMTER_ERROR = new RespCode("21","Some input paramter can not be null!");
	
	/**错误代码:错误的输入参数 */
	public static final RespCode WRONG_PARAMTER_ERROR = new RespCode("22","Can not find any data by the paramter!");
	
	/**错误代码:并发异常 */
	public static final RespCode CONCURRENCY_ERROR = new RespCode("23","Concurrency error!");
	
	/**错误代码:并发异常 */
	public static final RespCode STATUS_ERROR = new RespCode("24","Business object status error!");
	
	/**错误代码:格式错误 */
	public static final RespCode FORMAT_ERROR = new RespCode("31","Format error!");
	
	/**错误代码:其他未知异常 */
	public static final RespCode OTHER_ERROR = new RespCode("99","Not described exception!");
	
}
