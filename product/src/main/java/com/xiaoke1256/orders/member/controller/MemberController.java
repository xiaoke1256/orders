package com.xiaoke1256.orders.member.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.xiaoke1256.orders.member.dto.Member;
import com.xiaoke1256.orders.member.service.MemberService;

@RestController
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/member/{accountNo}")
	public Member getMember(@PathVariable("accountNo") String accountNo) {
		com.xiaoke1256.orders.member.domain.Member domain = memberService.getMemberByAccountNo(accountNo);
		if(domain==null)
			return null;
		Member dto = new  Member();
		BeanUtils.copyProperties(domain, dto);
		return dto;
	}
	
	@GetMapping("/members")
	public List<Member> findAll() {
		List<com.xiaoke1256.orders.member.domain.Member> bos = memberService.findAll();
		if(bos==null || bos.isEmpty())
			return new ArrayList<>();
		List<Member> dtos = new ArrayList<>();
		for(com.xiaoke1256.orders.member.domain.Member bo:bos) {
			Member dto = new  Member();
			BeanUtils.copyProperties(bo, dto);
			dtos.add(dto);
		}
		return dtos;
	}
}
