package com.xiaoke1256.orders.store.intra.store.client;

import com.xiaoke1256.orders.product.dto.StoreMember;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StoreMemberFallbackFactory implements FallbackFactory<StoreMemberClient> {

    private static final Logger logger = LoggerFactory.getLogger(StoreMemberFallbackFactory.class);

    @Override
    public StoreMemberClient create(Throwable cause) {
        return new StoreMemberClient() {
            @Override
            public List<StoreMember> queryStoreMemberByAccountNo(String accountNo) {
                logger.error("connect fail.by hystrix.",cause);
                throw new RuntimeException(cause);
            }
        };
    }
}
