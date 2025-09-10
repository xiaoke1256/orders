package com.xiaoke1256.orders.product.service;

import com.xiaoke1256.orders.member.entity.MemberEntity;
import com.xiaoke1256.orders.member.repository.IMemberRepository;
import com.xiaoke1256.orders.product.assembler.StoreMemberAssembler;
import com.xiaoke1256.orders.product.domain.StoreMember;
import com.xiaoke1256.orders.product.entity.StoreMemberEntity;
import com.xiaoke1256.orders.product.repository.IStoreMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StoreMemberService {
    @Autowired
    private IStoreMemberRepository storeMemberRepository;

    @Transactional(readOnly=true)
    public List<StoreMember> selectByAccountNo(String accountNo){
        List<StoreMemberEntity> members = storeMemberRepository.selectByAccountNo(accountNo);
        return members.stream().map((m)-> StoreMemberAssembler.toDomain(m)).collect(Collectors.toList());
    }

}
