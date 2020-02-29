package com.xiaoke1256.orders.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.core.client.MemberQueryClient;
import com.xiaoke1256.orders.member.api.MemberQueryService;
import com.xiaoke1256.orders.member.dto.Member;

@RestController
public class MemberQueryController implements MemberQueryService {

	private MemberQueryClient memberQueryClient;
	
	@Override
	@RequestMapping(value="/members",method={RequestMethod.GET})
	public List<Member> findAll() {
		return memberQueryClient.findAll();
	}

}
