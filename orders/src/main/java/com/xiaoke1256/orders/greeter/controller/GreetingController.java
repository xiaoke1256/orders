package com.xiaoke1256.orders.greeter.controller;

import com.xiaoke1256.orders.greeter.service.GrpcClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * gRpc 测试用
 */
@RestController
@RequestMapping("/greet")
public class GreetingController {

    @Autowired
    private GrpcClientService grpcClientService;

    @GetMapping("/greeting")
    public String greeting(@RequestParam("name") String name) {
        return grpcClientService.hello(name);
    }
}
