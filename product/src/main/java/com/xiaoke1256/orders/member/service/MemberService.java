package com.xiaoke1256.orders.member.service;

import java.util.List;

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
	
	public List<Member> findAll(){
		return memberDao.findAll();
	}
}
