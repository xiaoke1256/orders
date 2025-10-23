package com.xiaoke1256.orders.core.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.xiaoke1256.orders.member.api.MemberQueryService;
import com.xiaoke1256.orders.member.dto.Member;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="api-product",url="${remote.api.product.uri}",
		fallbackFactory=ProductQueryFallbackFactory.class,
		contextId="MemberQuery")
public interface MemberQueryClient extends MemberQueryService {
	
	@GetMapping("/members")
	public List<Member> findAll();

	@RequestMapping(value="/member/{accountNo}",method={RequestMethod.GET})
	public Member getMember(String accountNo);


}
