package com.zerck.controller;

import com.zerck.service.TodoService;
import lombok.extern.log4j.Log4j2;
import com.zerck.dto.TodoDTO;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name="todoListController", value="/todo/list")
@Log4j2
public class TodoListController extends HttpServlet {
    private TodoService todoService = TodoService.INSTANCE;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("todo list......................");

        //이 코드로 프로그램 실행시에 W2를 appName 이라는 곳에 저장할 수 있다.
        ServletContext servletContext = req.getServletContext();
        log.info("appName: " + servletContext.getAttribute("appName"));

        try{
            List<TodoDTO> dtoList = todoService.listAll();
            req.setAttribute("dtoList", dtoList);
            req.getRequestDispatcher("/WEB-INF/todo/list.jsp").forward(req, resp);
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ServletException("list error");
        }

    }

}
