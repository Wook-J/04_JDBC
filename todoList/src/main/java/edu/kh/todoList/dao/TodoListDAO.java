package edu.kh.todoList.dao;

import java.sql.Connection;
import java.util.List;

import edu.kh.todoList.dto.Todo;

public interface TodoListDAO {

	/** 할 일 목록 전체조회 DAO
	 * @param conn
	 * @return todoList
	 */
	List<Todo> todoListFullview(Connection conn) throws Exception;

	/** 완료된 할 일 개수 조회 DAO
	 * @param conn
	 * @return 완료된 개수
	 */
	int getCompleteCount(Connection conn) throws Exception;

	/** 할 일 추가 DAO
	 * @param conn
	 * @param title
	 * @param detail
	 * @return 성공 시 추가된 행의 개수/ 실패 시 0
	 */
	int todoAdd(Connection conn, String title, String detail) throws Exception;

	/** 할 일 상세 조회 DAO
	 * @param conn
	 * @param todoNo
	 * @return Todo 객체 or null
	 */
	Todo todoDetailView(Connection conn, int todoNo) throws Exception;

	/** 완료 여부 변경 DAO
	 * @param conn
	 * @param todoNo
	 * @return 변경 성공한 행의 개수
	 */
	int todoComplete(Connection conn, int todoNo) throws Exception;

	/** 할 일 삭제 DAO
	 * @param conn
	 * @param todoNo
	 * @return
	 * @throws Exception
	 */
	int todoDelete(Connection conn, int todoNo) throws Exception;

	/** 할 일 수정 DAO
	 * @param conn
	 * @param todoNo
	 * @param title
	 * @param detail
	 * @return
	 */
	int todoUpdate(Connection conn, int todoNo, String title, String detail) throws Exception;

}
