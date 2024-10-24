package edu.kh.todoList.serivce;

import java.util.Map;

import edu.kh.todoList.dto.Todo;

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

	/** 할 일 상세 조회 서비스
	 * @param todoNo
	 * @return 일치하는 TODO_NO 있으면 Todo 객체, 없으면 null
	 */
	Todo todoDetailView(int todoNo) throws Exception;

	/** 완료 여부 변경 서비스
	 * @param todoNo
	 * @return 변경 성공한 행의 개수
	 */
	int todoComplete(int todoNo) throws Exception;

	/** 할 일 삭제 서비스
	 * @param todoNo
	 * @return
	 */
	int todoDelete(int todoNo) throws Exception;

	/** 할 일 수정 서비스
	 * @param todoNo
	 * @param title
	 * @param detail
	 * @return
	 */
	int todoUpdate(int todoNo, String title, String detail) throws Exception;


}
