package com.xiaoke1256.orders.store.intra.client;

import com.xiaoke1256.orders.member.api.MemberQueryService;
import com.xiaoke1256.orders.member.dto.Member;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory=MemberQueryFallbackFactory.class,path="product")
public interface MemberQueryClient extends MemberQueryService {

    @GetMapping("/members")
    public List<Member> findAll();
}
