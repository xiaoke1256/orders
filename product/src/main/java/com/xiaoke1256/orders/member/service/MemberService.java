package com.xiaoke1256.orders.member.service;

import java.util.List;
import java.util.stream.Collectors;

import com.xiaoke1256.orders.member.assembler.MemberAssembler;
import com.xiaoke1256.orders.member.entity.MemberEntity;
import com.xiaoke1256.orders.member.repository.IMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xiaoke1256.orders.member.domain.Member;

@Service
@Transactional
public class MemberService {

	@Autowired
	private IMemberRepository memberRepository;
	
	@Transactional(readOnly = true)
	public Member getMemberByAccountNo(String accountNo) {
		MemberEntity entity = memberRepository.getMemberByAccountNo(accountNo);
		return MemberAssembler.toDomain(entity);
	}
	
	public List<Member> findAll(){
		List<MemberEntity> entities =  memberRepository.list();
		return entities.stream().map((e)->MemberAssembler.toDomain(e)).collect(Collectors.toList());
	}
}
