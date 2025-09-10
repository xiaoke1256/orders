package com.xiaoke1256.orders.member.dao;

import java.util.List;

import com.xiaoke1256.orders.member.bo.Member;

/**
 * @deprecated
 */
@Deprecated
public interface MemberDao {
	public Member getMemberByAccountNo(String accountNo);
	
	public List<Member> findAll();
}
