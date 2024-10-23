package edu.kh.todoList.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {
	
	private int todoNo;			// TODO_NO(번호)
	private String title;		// TODO_TITLE(제목)
	private String detail;		// TODO_DETAIL(상세내용)
	private boolean complete;	// TODO_COMPLETE(완료여부)
	private String regDate;		// REG_DATE(등록일)
}
