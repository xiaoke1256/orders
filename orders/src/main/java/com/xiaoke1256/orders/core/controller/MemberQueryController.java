package com.xiaoke1256.orders.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.core.client.MemberQueryClient;
import com.xiaoke1256.orders.member.api.MemberQueryService;
import com.xiaoke1256.orders.member.dto.Member;

@RestController
public class MemberQueryController implements MemberQueryService {
	@Autowired
	private MemberQueryClient memberQueryClient;
	
	@Override
	@RequestMapping(value="/members",method={RequestMethod.GET})
	public List<Member> findAll() {
		return memberQueryClient.findAll();
	}

	@Override
	@RequestMapping(value="/member/{accountNo}",method={RequestMethod.GET})
	public Member getMember(@PathVariable("accountNo") String accountNo) {
		return null;
	}

}
