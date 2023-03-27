package com.zerck.controller;

import com.zerck.dto.MemberDTO;
import com.zerck.service.MemberService;
import lombok.extern.java.Log;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
@Log
public class LoginController extends HttpServlet {

    //get 방식은 로그인 화면을 보여줌
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("login get..........");
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }
    //post 방식은 로그인을 처리함
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("login post............");

        String mid = req.getParameter("mid"); //로그인 화면에서 ID 부분
        String mpw = req.getParameter("mpw"); //로그인 화면에서 PW 부분

        try {
            MemberDTO memberDTO = MemberService.INSTANCE.login(mid, mpw);
            HttpSession session = req.getSession(); //세션 생성

            //정상적으로 로그인 된 경우 HttpSession 을 이용해서 사용자 공간에 loginInfo 라는 이름으로 문자열 보관
            session.setAttribute("loginInfo", memberDTO);
            resp.sendRedirect("/todo/list");

        } catch (Exception e){
            resp.sendRedirect("/login?result=error");
        }





    }
}
