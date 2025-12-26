package com.xiaoke1256.orders.product.grpcservice;

import com.google.protobuf.Timestamp;
import com.xiaoke1256.orders.common.util.DateUtil;
import com.xiaoke1256.orders.product.dto.SimpleProduct;
import com.xiaoke1256.orders.product.grpc.ProductCodeRequest;
import com.xiaoke1256.orders.product.grpc.ProductServerGrpc;
import com.xiaoke1256.orders.product.service.ProductService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@GrpcService
public class ProductGrpcService extends ProductServerGrpc.ProductServerImplBase {

    @Autowired
    private ProductService productService;

    @Override
    public void getSimpleProductByCode(ProductCodeRequest request, StreamObserver<com.xiaoke1256.orders.product.grpc.SimpleProduct> responseObserver) {
        String productCode = request.getProductCode();
        log.info("Receive:"+productCode);
        com.xiaoke1256.orders.product.dto.SimpleProduct product = productService.getSimpleProductByCode(productCode);
        com.xiaoke1256.orders.product.grpc.SimpleProduct.Builder simpleProductBuilder = com.xiaoke1256.orders.product.grpc.SimpleProduct.newBuilder();
        if(product != null) {
            log.info("product:"+product);
            simpleProductBuilder
                    .setProductCode(StringUtils.trimToEmpty(product.getProductCode()))
                    .setProductName(StringUtils.trimToEmpty(product.getProductName()))
                    .setProductPrice(product.getProductPrice() != null ? product.getProductPrice().toPlainString() : null)
                    .setFullProductTypeName(StringUtils.trimToEmpty(product.getFullProductTypeName()))
                    .setStoreName(StringUtils.trimToEmpty(product.getStoreName()))
                    .setStoreNo(StringUtils.trimToEmpty(product.getStoreNo()))
                    .setProductStatus(StringUtils.trimToEmpty(product.getProductStatus()))
                    .setInSeckill(StringUtils.trimToEmpty(product.getInSeckill()))
                    .setProductStatus(StringUtils.trimToEmpty(product.getProductStatus()))
                    .setProductIntro(StringUtils.trimToEmpty(product.getProductIntro()))
                    .setBrand(StringUtils.trimToEmpty(product.getBrand()))
                    .setInsertTime(
                            product.getInsertTime() != null ?
                                    Timestamp.newBuilder().setSeconds(product.getInsertTime().getTime() / 1000).setNanos((int) (product.getInsertTime().getTime() % 1000) * 1000).build()
                                    : null
                    ).setUpdateTime(
                            product.getUpdateTime() != null ?
                                    Timestamp.newBuilder().setSeconds(product.getUpdateTime().getTime() / 1000).setNanos((int) (product.getUpdateTime().getTime() % 1000) * 1000).build()
                                    : null
                    )
                    .build();
        }

        responseObserver.onNext(simpleProductBuilder.build());
        responseObserver.onCompleted();
        log.info("send finished!");
    }
}
