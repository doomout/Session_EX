package com.zerck.filter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = {"/*"}) //모든 경로에서
@Log4j2
public class UTF8Filter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("UTF8 Filter.....");
        HttpServletRequest req = (HttpServletRequest) request;
        req.setCharacterEncoding("UTF-8"); //한글이 가능하도록

        chain.doFilter(request, response);
    }
}
