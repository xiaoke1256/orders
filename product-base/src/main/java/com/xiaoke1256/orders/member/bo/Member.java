package com.xiaoke1256.orders.member.bo;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 会员表
 * @author Administrator
 *
 */
public class Member implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long memberId;
	
	private String accountNo;
	
	private String nickName;
	
	private String gender;
	
	private String intro;
	
	private String email;
	
	private String mobilePhone;
	
	private Timestamp insertTime;
	
	private Timestamp updateTime;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Timestamp getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
}
