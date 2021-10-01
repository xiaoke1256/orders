package com.xiaoke1256.orders.store.intra.store.client;

import com.xiaoke1256.orders.product.dto.Store;
import com.xiaoke1256.orders.product.dto.StoreMember;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StoreFallbackFactory implements FallbackFactory<StoreClient> {

    private static final Logger logger = LoggerFactory.getLogger(StoreFallbackFactory.class);

    @Override
    public StoreClient create(Throwable cause) {
        return new StoreClient() {

            @Override
            public List<Store> queryAvailableStore() {
                logger.error("connect fail.by hystrix.",cause);
                throw new RuntimeException(cause);
            }

            @Override
            public Store getStore(String storeNo) {
                logger.error("connect fail.by hystrix.",cause);
                throw new RuntimeException(cause);
            }

            @Override
            public void createStore(Store store, String leaderAccount) {
                logger.error("connect fail.by hystrix.",cause);
                throw new RuntimeException(cause);
            }
        };
    }
}
