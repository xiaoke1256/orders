package com.xiaoke1256.orders.core.client;

import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.grpc.ProductCodeRequest;
import com.xiaoke1256.orders.product.grpc.ProductServerGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@Component
public class ProductQueryGrpcClient {
    @GrpcClient("api-product")
    private ProductServerGrpc.ProductServerBlockingStub blockingStub;

    public SimpleProduct getSimpleProductByCode( String productCode){
        com.xiaoke1256.orders.product.grpc.SimpleProduct productReply = blockingStub.getSimpleProductByCode(ProductCodeRequest.newBuilder().setProductCode(productCode).build());

        SimpleProduct simpleProduct = new SimpleProduct();
        BeanUtils.copyProperties(productReply,simpleProduct);
        if(StringUtils.isNotBlank(productReply.getProductPrice())){
            simpleProduct.setProductPrice(new BigDecimal(productReply.getProductPrice()));
        }

        return simpleProduct;
    }


}