package com.xiaoke1256.orders.greeter.client;

import com.xiaoke1256.orders.product.GreeterGrpc;
import com.xiaoke1256.orders.product.HelloReply;
import com.xiaoke1256.orders.product.HelloRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

/**
 * gRpc 测试用
 */
@Component
public class GrpcClientService {
    @GrpcClient("api-product")
    GreeterGrpc.GreeterBlockingStub greeterStub;

    public String hello(String name) {
        HelloRequest helloRequest = HelloRequest.newBuilder().setName(name).build();
        HelloReply reply = greeterStub.sayHello(helloRequest);
        System.out.println("s = " + reply.getMessage());
        return reply.getMessage();
    }
}
