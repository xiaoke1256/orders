package com.xiaoke1256.orders.common.exception;

import java.io.Serializable;

/**
 * 错误代码，实际上就是一个枚举类型
 * @author Administrator
 *
 */
public class ErrorCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	
	private String msg;
	
	/**
	 * 禁用构造方法
	 */
	private ErrorCode() {
		
	}

	private ErrorCode(String code, String msg) {
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
		ErrorCode other = (ErrorCode) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	/**错误代码:操作成功 */
	public static final ErrorCode SUCCESS = new ErrorCode("00","Success!");
	
	/**错误代码:业务异常 */
	public static final ErrorCode BUSSNESS_ERROR = new ErrorCode("80","Bussness exception!");
	
	/**错误代码:网络连接异常 */
	public static final ErrorCode CONNECT_ERROR = new ErrorCode("11","Net connect error!");
	
	/**错误代码:空参数错误 */
	public static final ErrorCode EMPTY_PARAMTER_ERROR = new ErrorCode("21","Some input paramter can not be null!");
	
	/**错误代码:错误的输入参数 */
	public static final ErrorCode WRONG_PARAMTER_ERROR = new ErrorCode("22","Can not find any data by the paramter!");
	
	/**错误代码:格式错误 */
	public static final ErrorCode FORMAT_ERROR = new ErrorCode("31","Format error!");
	
	/**错误代码:其他未知异常 */
	public static final ErrorCode OTHER_ERROR = new ErrorCode("99","Not described exception!");
	
}
