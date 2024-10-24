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

// /todo/detail 에서 완료 여부 버튼 클릭 시
@WebServlet("/todo/complete")
public class CompleteServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {		
		try {
			// 전달 받은 파라미터 얻어오기 (쿼리스트링 부분!!! 까먹지 말자..)
			int todoNo = Integer.parseInt(req.getParameter("todoNo"));
			
			// 할 일 여부를 변경하는 서비스 호출 후 결과 반환받기
			TodoListService service = new TodoListServiceImpl();
			
			// 수정 성공한 행의 개수
			int result = service.todoComplete(todoNo);
			
			// session 객체 먼저 얻어오기(이하 구문 수행 위해)
			HttpSession session = req.getSession();
			
			// 변경 성공 시 -> 원래 보고 있던 detail 페이지로 redirect
			// + message 로 "완료 여부가 변경되었습니다!"
			if(result > 0) {
				session.setAttribute("message", "완료 여부가 변경되었습니다!");
				resp.sendRedirect("/todo/detail?todoNo=" + todoNo);
				return;
			}
			
			// 변경 실패 시 -> main 페이지로 redirect 
			// + message 로 "todo가 존재하지 않습니다"
			// 혹시라도 확인하려면 url에 /todo/complete?todoNo=22 써보기 (detail.js부분 같이)
			session.setAttribute("message", "todo가 존재하지 않습니다");
			resp.sendRedirect("/");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
