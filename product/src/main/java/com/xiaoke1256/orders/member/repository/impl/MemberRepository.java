package com.xiaoke1256.orders.member.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;
import com.xiaoke1256.orders.member.entity.MemberEntity;
import com.xiaoke1256.orders.member.mapper.MemberMapper;
import com.xiaoke1256.orders.member.repository.IMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-10
 */
@Service
public class MemberRepository extends CrudRepository<MemberMapper, MemberEntity> implements IMemberRepository {


    @Override
    public MemberEntity getMemberByAccountNo(String accountNo) {
        LambdaQueryWrapper<MemberEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberEntity::getAccountNo,accountNo);
        return this.baseMapper.selectOne(wrapper);
    }

    @Override
    public List<MemberEntity> findAll() {
        return baseMapper.selectList(new LambdaQueryWrapper<>());
    }
}
