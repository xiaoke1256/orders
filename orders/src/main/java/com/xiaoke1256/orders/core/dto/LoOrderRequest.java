package com.xiaoke1256.orders.core.dto;

/**
 * 提交物流单的请求
 * @author Administrator
 *
 */
public class LoOrderRequest {
	/**子订单号*/
	private String subOrderNo;
	
	/**物流公司代码*/
	private String companyCode;
	
	/**物流单号*/
	private String loOrderNo;

	public String getSubOrderNo() {
		return subOrderNo;
	}

	public void setSubOrderNo(String subOrderNo) {
		this.subOrderNo = subOrderNo;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getLoOrderNo() {
		return loOrderNo;
	}

	public void setLoOrderNo(String loOrderNo) {
		this.loOrderNo = loOrderNo;
	}
	
	
}
