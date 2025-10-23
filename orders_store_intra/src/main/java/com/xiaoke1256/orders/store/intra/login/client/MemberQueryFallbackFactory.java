package com.xiaoke1256.orders.store.intra.login.client;

import com.xiaoke1256.orders.member.dto.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

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

			@Override
			public Member getMember(String accountNo) {
				logger.error("connect fail.by hystrix.",cause);
				throw new RuntimeException(cause);
			}

		};
	}

}
