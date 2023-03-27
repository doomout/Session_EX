package com.zerck.controller;

import lombok.extern.log4j.Log4j2;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/logout")
@Log4j2
public class LogoutController extends HttpServlet {
    //로그 아웃은 중요한 처리 과정이니 post 로만 동작하도록 한다.
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("log out............");

        HttpSession session = req.getSession(); //세션 생성

        session.removeAttribute("loginInfo"); //세션에서 로그인 정보 삭제
        session.invalidate(); //해당 세션이 더이상 유효하지 않다고 알려줌

        resp.sendRedirect("/"); //상위 경로로 이동
    } 
}
