package com.xiaoke1256.orders.core.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.member.api.MemberQueryService;
import com.xiaoke1256.orders.member.dto.Member;

@RestController
public class MemberQueryController implements MemberQueryService {

	@Override
	public List<Member> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
