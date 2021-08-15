package com.xiaoke1256.orders.member.api;

import java.util.List;

import com.xiaoke1256.orders.member.dto.Member;

public interface MemberQueryService {
	public List<Member> findAll();
	public Member getMember(String accountNo);
}
