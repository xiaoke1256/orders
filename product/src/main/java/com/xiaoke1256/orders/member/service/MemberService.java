package com.xiaoke1256.orders.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.member.bo.Member;
import com.xiaoke1256.orders.member.dao.MemberDao;

@Service
@Transactional
public class MemberService {
	@Autowired
	private MemberDao memberDao;
	
	@Transactional(readOnly = true)
	public Member getMemberByAccountNo(String accountNo) {
		return memberDao.getMemberByAccountNo(accountNo);
	}
}
