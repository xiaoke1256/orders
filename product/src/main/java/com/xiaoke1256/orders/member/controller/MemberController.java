package com.xiaoke1256.orders.member.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.xiaoke1256.orders.member.dto.Member;

@Controller
public class MemberController {
	
	@GetMapping("/member/{accountNo}")
	public Member getMember(String accountNo) {
		return null;
	}
	
	@GetMapping("/members")
	public List<Member> findAll() {
		return null;
	}
}
