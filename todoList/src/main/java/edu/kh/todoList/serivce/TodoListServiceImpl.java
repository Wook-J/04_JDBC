package edu.kh.todoList.serivce;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.kh.todoList.common.JDBCTemplate.*;
import edu.kh.todoList.dao.TodoListDAO;
import edu.kh.todoList.dao.TodoListDAOImpl;
import edu.kh.todoList.dto.Todo;

public class TodoListServiceImpl implements TodoListService{

	// 필드
	private TodoListDAO dao = new TodoListDAOImpl();

	// 메서드
	@Override
	public Map<String, Object> todoListFullView() throws Exception{

		// 커넥션 생성
		Connection conn = getConnection();
		
		// 할 일 목록 얻어오기 (DAO 호출 및 반환)
		List<Todo> todoList = dao.todoListFullview(conn);
		
		// 완료된 할 일 개수 카운트(DAO 호출 및 반환)
		int completeCount = dao.getCompleteCount(conn);
		
		// 메서드에서 반환은 하나의 값 또는 객체밖에 할 수 없으므로,
		// Map 이라는 컬렉션을 이용해 여러 값을 한 번에 묶어서 반환
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("todoList", todoList);
		map.put("completeCount", completeCount);	// 래퍼클래스 : Auto-boxing(기본 자료형 -> 객체화)
		
		close(conn);
		
		return map;
	}

	@Override
	public int todoAdd(String title, String detail) throws Exception{
		
		Connection conn = getConnection();
		int result = dao.todoAdd(conn, title, detail);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}

	@Override
	public Todo todoDetailView(int todoNo) throws Exception{
		Connection conn = getConnection();
		Todo todo = dao.todoDetailView(conn, todoNo);
		
		close(conn);
		
		return todo;
	}

	@Override
	public int todoComplete(int todoNo) throws Exception {
		Connection conn = getConnection();
		int result = dao.todoComplete(conn, todoNo);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}

	@Override
	public int todoDelete(int todoNo) throws Exception {
		Connection conn = getConnection();
		int result = dao.todoDelete(conn, todoNo);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}

	@Override
	public int todoUpdate(int todoNo, String title, String detail) throws Exception {
		Connection conn = getConnection();
		int result = dao.todoUpdate(conn, todoNo, title, detail);
		
		if(result > 0) commit(conn);
		else rollback(conn);;
		
		close(conn);
		
		return result;
	}
	
}
