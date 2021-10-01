package com.xiaoke1256.orders.product.service;

import com.xiaoke1256.orders.product.bo.StoreMember;
import com.xiaoke1256.orders.product.dao.StoreMemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreMemberService {
    @Autowired
    private StoreMemberDao storeMemberDao;

    @Transactional(readOnly=true)
    public List<StoreMember> selectByAccountNo(String accountNo){
        return storeMemberDao.selectByAccountNo(accountNo);
    }

}
