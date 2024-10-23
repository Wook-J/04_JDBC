package edu.kh.todoList.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.kh.todoList.dto.Todo;
import edu.kh.todoList.serivce.TodoListService;
import edu.kh.todoList.serivce.TodoListServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// "/main" 요청을 매핑하여 처리하는 Servlet
@WebServlet("/main")
public class MainServlet extends HttpServlet{
	
	/* 왜 "/main" 메인 페이지 요청을 처리하는 서블릿을 만들었는가?
	 * 
	 * - Sevlet(Back-end)에서 추가한 데이터(DB에서 조회한 데이터)를
	 *   메인 페이지에서 사용할 수 있게 하려고 
	 * 
	 * */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			// Service 객체 생성
			// 요청         -> Controller ->  Service 호출 -> DAO 호출 -> DB에서 조회
			// 응답 <- View <- Controller <-  Service      <-   DAO    <- DB에서 결과
			TodoListService service = new TodoListServiceImpl();
			
			// 전체 할 일 목록 + 완료된 Todo 개수가 담긴 Map 을
			// Service 호출해서 얻어오기
			Map<String, Object> map = service.todoListFullView();
			
			// Map 에 저장된 값을 풀어내기
			List<Todo> todoList = (List<Todo>) map.get("todoList");	// Object 로 불러왔으므로 강제 형변환
			int completeCount = (int) map.get("completeCount");		// Object 로 불러왔으므로 강제 형변환
			
			// request scope에 객체 값 추가하기
			req.setAttribute("todoList", todoList);
			req.setAttribute("completeCount", completeCount);
			
			// 메인 페이지 응답을 담당하는 jsp 에 요청 위임
			String path = "/WEB-INF/views/main.jsp";
			req.getRequestDispatcher(path).forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}













