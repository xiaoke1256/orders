package com.xiaoke1256.orders.store.intra.client;

import com.xiaoke1256.orders.product.dto.ProductCondition;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.dto.SimpleProductQueryResultResp;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductQueryFallbackFactory implements FallbackFactory<ProductQueryClient> {

    private static final Logger logger = LoggerFactory.getLogger(ProductQueryFallbackFactory.class);

    @Override
    public ProductQueryClient create(Throwable cause) {
        return new ProductQueryClient(){

            @Override
            public SimpleProductQueryResultResp searchProductByCondition(ProductCondition condition) {
                logger.error("connect fail.by hystrix.",cause);
                throw new RuntimeException(cause);
            }

            @Override
            public SimpleProduct getSimpleProductByCode(String productCode) {
                logger.error("connect fail.by hystrix.",cause);
                throw new RuntimeException(cause);
            }
        };
    }
}
