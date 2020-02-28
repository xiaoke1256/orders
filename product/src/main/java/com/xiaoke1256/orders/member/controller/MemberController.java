package com.xiaoke1256.orders.member.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.member.dto.Member;
import com.xiaoke1256.orders.member.service.MemberService;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/member/{accountNo}")
	public Member getMember(String accountNo) {
		com.xiaoke1256.orders.member.bo.Member bo = memberService.getMemberByAccountNo(accountNo);
		if(bo==null)
			return null;
		Member dto = new  Member();
		BeanUtils.copyProperties(bo, dto);
		return dto;
	}
	
	@GetMapping("/members")
	public List<Member> findAll() {
		return null;
	}
}
