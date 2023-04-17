package com.xiaoke1256.orders.store.intra.login.client;

import com.xiaoke1256.orders.member.api.MemberQueryService;
import com.xiaoke1256.orders.member.dto.Member;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name="api-product",url="${remote.api.product.uri}",fallbackFactory=MemberQueryFallbackFactory.class,path="product")
public interface MemberQueryClient extends MemberQueryService {

    @GetMapping("/members")
    public List<Member> findAll();

    @GetMapping("/member/{accountNo}")
    public Member getMember(@PathVariable("accountNo") String accountNo);
}
