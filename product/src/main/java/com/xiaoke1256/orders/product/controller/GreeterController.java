package com.xiaoke1256.orders.product.controller;


import com.xiaoke1256.orders.product.GreeterGrpc;
import com.xiaoke1256.orders.product.HelloReply;
import com.xiaoke1256.orders.product.HelloRequest;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

/**
 * grpc 测试用
 */
@GrpcService
public class GreeterController extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        System.out.println("Receive:"+request.getName());
        responseObserver.onNext(HelloReply.newBuilder().setMessage("Hello " + request.getName()).build());
        responseObserver.onCompleted();
    }
}
