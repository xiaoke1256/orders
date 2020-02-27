package com.xiaoke1256.orders.member.dao;

import com.xiaoke1256.orders.member.bo.Member;

public interface MemberDao {
	public Member getMemberByAccountNo(String accountNo);
}
