package com.xiaoke1256.orders.store.intra.store.client;

import com.xiaoke1256.orders.product.api.StoreMemberService;
import com.xiaoke1256.orders.product.dto.StoreMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="api-product",
        url="${remote.api.product.uri}",
        fallbackFactory= StoreMemberFallbackFactory.class,
        contextId = "StoreMember")
public interface StoreMemberClient extends StoreMemberService {
    @GetMapping(path="/storeMember/byAccountNo",consumes = MediaType.APPLICATION_JSON_VALUE)
    List<StoreMember> queryStoreMemberByAccountNo(@RequestParam("accountNo") String accountNo);
}
