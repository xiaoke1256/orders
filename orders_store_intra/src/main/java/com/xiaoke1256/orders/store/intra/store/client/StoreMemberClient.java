package com.xiaoke1256.orders.store.intra.store.client;

import com.xiaoke1256.orders.product.api.StoreMemberService;
import com.xiaoke1256.orders.product.dto.StoreMember;
import com.xiaoke1256.orders.store.intra.product.client.ProductQueryFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory= ProductQueryFallbackFactory.class,path="product")
public interface StoreMemberClient extends StoreMemberService {
    @GetMapping("/storeMember/byAccountNo")
    public List<StoreMember> queryStoreMemberByAccountNo(String accountNo);
}
