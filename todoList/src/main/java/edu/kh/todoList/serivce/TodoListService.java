package edu.kh.todoList.serivce;

import java.util.Map;

public interface TodoListService {

	/** 할 일 목록 반환 서비스
	 * @return todoList + 완료된 todo 개수
	 */
	Map<String, Object> todoListFullView() throws Exception;

	/** 할 일 추가 서비스
	 * @param title
	 * @param detail
	 * @return (int) 성공 시 추가된 행의 개수/ 실패 시 0
	 */
	int todoAdd(String title, String detail) throws Exception;

}
