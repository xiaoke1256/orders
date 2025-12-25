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
        com.xiaoke1256.orders.product.grpc.SimpleProduct simpleProduct = com.xiaoke1256.orders.product.grpc.SimpleProduct.newBuilder()
                .setProductCode(product.getProductCode())
                .setProductName(product.getProductName())
                .setProductPrice(product.getProductPrice()!=null?product.getProductPrice().toPlainString():null)
                .setFullProductTypeName(product.getFullProductTypeName())
                .setStoreName(product.getStoreName())
                .setStoreNo(product.getStoreNo())
                .setProductStatus(product.getProductStatus())
                .setInSeckill(product.getInSeckill())
                .setProductStatus(product.getProductStatus())
                .setProductIntro(product.getProductIntro())
                .setBrand(product.getBrand())
                .setInsertTime(
                        product.getInsertTime()!=null?
                                Timestamp.newBuilder().setSeconds(product.getInsertTime().getTime()/1000).setNanos((int)(product.getInsertTime().getTime()%1000)*1000).build()
                                :null
                ).setUpdateTime(
                        product.getUpdateTime()!=null?
                                Timestamp.newBuilder().setSeconds(product.getUpdateTime().getTime()/1000).setNanos((int)(product.getUpdateTime().getTime()%1000)*1000).build()
                                :null
                )
                .build();

        responseObserver.onNext(simpleProduct);
        responseObserver.onCompleted();
    }
}
