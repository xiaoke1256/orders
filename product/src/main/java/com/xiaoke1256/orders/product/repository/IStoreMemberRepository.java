package com.xiaoke1256.orders.product.repository;

import com.xiaoke1256.orders.product.bo.StoreMember;
import com.xiaoke1256.orders.product.entity.StoreMemberEntity;
import com.xiaoke1256.orders.common.mybatis.repository.IRepository;

import java.util.List;

/**
 * <p>
 * 店员表 服务类
 * </p>
 *
 * @author xiaoke_1256
 * @since 2025-09-10
 */
public interface IStoreMemberRepository extends IRepository<StoreMemberEntity> {
    List<StoreMemberEntity> selectByAccountNo(String accountNo);

    StoreMemberEntity getDefaultStore(String accountNo);

    void saveStoreMember(StoreMemberEntity storeMember);
}
