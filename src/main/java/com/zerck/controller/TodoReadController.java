package com.zerck.controller;

import com.zerck.service.TodoService;
import lombok.extern.log4j.Log4j2;
import com.zerck.dto.TodoDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="todoReadController", value = "/todo/read")
@Log4j2
public class TodoReadController extends HttpServlet {
    private TodoService todoService = TodoService.INSTANCE;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long tno = Long.parseLong(req.getParameter("tno"));
            TodoDTO todoDTO = todoService.get(tno);

            //모델 담기
            req.setAttribute("dto", todoDTO);

            //쿠키 찾기(쿠키 이름은 viewTodos 로 지정)
            Cookie viewTodoCookie = findCookie(req.getCookies(), "viewTodos");
            
            // 쿠키 값을 저장
            String todoListStr = viewTodoCookie.getValue();

            boolean exist = false;

            //쿠키 값이 있고, 인덱스가 0 이상이면.
            if (todoListStr != null && todoListStr.indexOf(tno + "-") >= 0) {
                exist = true; //true로 저장
            }

            log.info("exist: " + exist);

            //값이 있다면~
            if (!exist) {
                todoListStr += tno + "-"; //예:1-2-3-4-
                viewTodoCookie.setValue(todoListStr); //
                viewTodoCookie.setMaxAge(60*60*24); //유효 기간은 24시간으로 설정
                viewTodoCookie.setPath("/"); //쿠키 저장소는 /
                resp.addCookie(viewTodoCookie);
            }
            req.getRequestDispatcher("/WEB-INF/todo/read.jsp").forward(req, resp);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ServletException("read error");
        }
    }

    private Cookie findCookie(Cookie[] cookies, String cookieName) {
        Cookie targetCookie = null;
        
        //쿠키가 있고, 쿠키의 기간이 남아 있는가?
        if (cookies != null && cookies.length > 0) {
            //있으면 반복 해서 
            for (Cookie ck:cookies) {
                //쿠키 이름과 일치하면 저장해~
                if (ck.getName().equals(cookieName)) {
                    targetCookie = ck;
                    break;
                }
            }
        }
        
        //쿠키가 없어?
        if (targetCookie == null) {
            targetCookie = new Cookie(cookieName, ""); //생성하고
            targetCookie.setPath("/"); //경로는 상위
            targetCookie.setMaxAge(60*60*24); //유효시간은 24시간
        }
        return targetCookie; //쿠키 전송~
    }
}
