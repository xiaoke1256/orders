package com.xiaoke1256.orders.store.intra.product.client;

import com.xiaoke1256.orders.common.RespMsg;
import com.xiaoke1256.orders.store.intra.store.client.StoreFallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

public class StorageFallbackFactory implements FallbackFactory<StorageClient> {
    private static final Logger logger = LoggerFactory.getLogger(StoreFallbackFactory.class);
    @Override
    public StorageClient create(Throwable cause) {
        return new StorageClient(){

            @Override
            public Boolean incStorage(String productCode, String optionCode, int incNum) {
                logger.error("connect fail.by hystrix.",cause);
                throw new RuntimeException(cause);
            }

            @Override
            public long getStorageNumByProductCode(String productCode) {
                return 0;
            }
        };
    }
}
