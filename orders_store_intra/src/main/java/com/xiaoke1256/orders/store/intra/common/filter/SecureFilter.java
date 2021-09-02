package com.xiaoke1256.orders.store.intra.common.filter;

import com.xiaoke1256.orders.common.exception.InvalidAuthorizationException;
import com.xiaoke1256.orders.store.intra.common.encrypt.HMAC256;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class SecureFilter implements Filter {

    @Resource(name = "loginTokenGenerator")
    private HMAC256 loginTokenGenerator;

    @Override
    public void init(FilterConfig filterConfig)  {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)){
            throw new InvalidAuthorizationException("未登录");
        }
        if(loginTokenGenerator.verify(token)){
            throw new InvalidAuthorizationException("token失效");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        //do nothing
    }
}
