package edu.kh.todoList.controller;

import java.io.IOException;

import edu.kh.todoList.dto.Todo;
import edu.kh.todoList.serivce.TodoListService;
import edu.kh.todoList.serivce.TodoListServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 할 일 상세 조회 요청 처리 Servlet
@WebServlet("/todo/detail")
public class DetailServlet extends HttpServlet{

	@Override	// a 태그 요청은 GET 방식
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// 요청 시 전달 받은 파라미터를 얻어오기 및 형변환(String -> int)
			int todoNo = Integer.parseInt(req.getParameter("todoNo"));
			
			// Service 객체 생성
			TodoListService service = new TodoListServiceImpl();
			
			// TODO_NO 컬럼값이 todoNo와 같은 todo가 있으면 Todo 객체, 없으면 null 반환
			Todo todo = service.todoDetailView(todoNo);
			
			// todo가 존재하지 않을 경우(null) -> 메인페이지(/)로 redirect 후
			// "할 일이 존재하지 않습니다"라는 alert 출력
			if(todo == null) {
				
				// session 객체 생성 후 message 세팅하기
				HttpSession session = req.getSession();
				session.setAttribute("message", "할 일이 존재히지 않습니다.");
				
				resp.sendRedirect("/");
				return;
			}
			
			// todo가 존재하는 경우 detail.jsp로 forward해서 응답
			req.setAttribute("todo", todo);
			String path = "/WEB-INF/views/detail.jsp";
			req.getRequestDispatcher(path).forward(req, resp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
