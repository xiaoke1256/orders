package com.xiaoke1256.orders.member.assembler;

import com.xiaoke1256.orders.member.domain.Member;
import com.xiaoke1256.orders.member.entity.MemberEntity;
import com.xiaoke1256.orders.product.domain.ProductType;
import com.xiaoke1256.orders.product.entity.ProductTypeEntity;
import org.springframework.beans.BeanUtils;

import java.time.ZoneId;
import java.util.Date;

public class MemberAssembler {
    public static Member toDomain(MemberEntity memberEntity){
        Member domain = new Member();
        BeanUtils.copyProperties(memberEntity,domain);
        domain.setInsertTime(Date.from(memberEntity.getInsertTime().atZone(ZoneId.systemDefault()).toInstant()));
        domain.setUpdateTime(Date.from(memberEntity.getUpdateTime().atZone(ZoneId.systemDefault()).toInstant()));
        return domain;
    }
}
