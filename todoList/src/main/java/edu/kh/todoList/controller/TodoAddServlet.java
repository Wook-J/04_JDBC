package edu.kh.todoList.controller;

import java.io.IOException;

import edu.kh.todoList.serivce.TodoListService;
import edu.kh.todoList.serivce.TodoListServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/todo/add")
public class TodoAddServlet extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 할 일 추가
		try {
			
			// 1. TodoListServiceImpl 객체 생성
			TodoListService service = new TodoListServiceImpl();
			
			// 2. 요청 시 전달받은 파라미터 얻어오기
			String title = req.getParameter("title");
			String detail = req.getParameter("detail");
			
			// 3. 서비스 호출 후 결과 반환 받기
			int result = service.todoAdd(title, detail);
			
			// 4. 성공/ 실패 메시지 세팅하기
			String message = null;
			if(result > 0) message = "추가 성공!";
			else message = "추가 실패...";
			
			// 5. 기존 req 를 사용할 수 없으므로, 
			// Session 을 이용하여 message 세팅
			HttpSession session = req.getSession();
			session.setAttribute("message", message);
			
			// 6. 메인 페이지로 redirect(재요청)
			resp.sendRedirect("/");
			// -> @WebServlet("/") 가 작성된 Servlet 으로 재요청
			
			// redirect 는 무조건 GET 방식!
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
