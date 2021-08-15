package com.xiaoke1256.orders.store.intra.client;

import com.xiaoke1256.orders.member.dto.Member;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MemberQueryFallbackFactory implements FallbackFactory<MemberQueryClient> {

	private static final Logger logger = LoggerFactory.getLogger(MemberQueryFallbackFactory.class);
	
	@Override
	public MemberQueryClient create(Throwable cause) {
		return new MemberQueryClient() {

			@Override
			public List<Member> findAll() {
				logger.error("connect fail.by hystrix.",cause);
				throw new RuntimeException(cause);
			}
			
		};
	}

}
