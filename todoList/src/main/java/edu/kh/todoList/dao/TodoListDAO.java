package edu.kh.todoList.dao;

import java.sql.Connection;
import java.util.List;

import edu.kh.todoList.dto.Todo;

public interface TodoListDAO {

	/** 할 일 목록 전체조회
	 * @param conn
	 * @return todoList
	 */
	List<Todo> todoListFullview(Connection conn) throws Exception;

	/** 완료된 할 일 개수 조회
	 * @param conn
	 * @return 완료된 개수
	 */
	int getCompleteCount(Connection conn) throws Exception;

	/** 할 일 추가
	 * @param conn
	 * @param title
	 * @param detail
	 * @return 성공 시 추가된 행의 개수/ 실패 시 0
	 */
	int todoAdd(Connection conn, String title, String detail) throws Exception;

}
