package com.xiaoke1256.orders.member.repository;

import com.xiaoke1256.orders.member.bo.Member;
import com.xiaoke1256.orders.member.entity.MemberEntity;
import com.xiaoke1256.orders.common.mybatis.repository.IRepository;
import com.xiaoke1256.orders.product.bo.StoreMember;

import java.util.List;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-10
 */
public interface IMemberRepository extends IRepository<MemberEntity> {
    MemberEntity getMemberByAccountNo(String accountNo);

    List<MemberEntity> findAll();
}
