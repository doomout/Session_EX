package com.zerck.filter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/todo/*"})
@Log4j2
public class LoginCheckFilter implements Filter {
    @Override
    // 필터가 필터링이 필요한 로직을 구현하는 부분
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Login check filter.....");

        //doFilter() 은 HttpServletRequest/HttpServletResponse 보다 상위 타입의 파라미터를 사용하여
        //HTTP 관련 작업을 하려면 다운 캐스팅 해줘야 한다.
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;

        HttpSession session = req.getSession();

        //세션에 loginInfo 이름의 값이 없다면(로그인 안했다면...) 로그인 화면으로 이동한다.
        if(session.getAttribute("loginInfo")== null) {
            resp.sendRedirect("/login");

            return;
        }

        chain.doFilter(request, response);
    }
}
