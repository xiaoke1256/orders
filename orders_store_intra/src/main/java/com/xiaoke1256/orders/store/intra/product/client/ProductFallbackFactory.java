package com.xiaoke1256.orders.store.intra.product.client;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductFallbackFactory implements FallbackFactory<ProductClient> {

    private static final Logger logger = LoggerFactory.getLogger(ProductFallbackFactory.class);

    @Override
    public ProductClient create(Throwable cause) {
        return new ProductClient(){

            @Override
            public void switchShelves(String productCode, String onOrOff) {
                logger.error("connect fail.by hystrix.",cause);
                throw new RuntimeException(cause);
            }
        };
    }
}
