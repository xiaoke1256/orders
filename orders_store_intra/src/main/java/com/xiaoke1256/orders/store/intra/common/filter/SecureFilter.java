package com.xiaoke1256.orders.store.intra.common.filter;

import com.xiaoke1256.orders.common.RespCode;
import com.xiaoke1256.orders.store.intra.common.encrypt.HMAC256;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        if(request.getPathInfo().startsWith("/login")){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)){
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print("{code:'"+ RespCode.LOGIN_ERROR.getCode()+"',msg:'尚未登录'}");
            return;
        }
        if(loginTokenGenerator.verify(token)){
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print("{code:'"+ RespCode.LOGIN_ERROR.getCode()+"',msg:'token失效'}");
            return;
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        //do nothing
    }
}
