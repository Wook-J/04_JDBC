package edu.kh.todoList.dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static edu.kh.todoList.common.JDBCTemplate.*;
import edu.kh.todoList.dto.Todo;

public class TodoListDAOImpl implements TodoListDAO{

	// 필드
	// JDBC 객체 참조변수 + Properties 참조변수(sql.xml 적힌 SQL 구문을 읽어오려고)
	private Statement stmt;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private Properties prop;
	
	// 기본 생성자
	public TodoListDAOImpl() {
		// TodoListDAOImpl 객체가 생성될 때 sql.xml 파일의 내용을 읽어와
		// Properties prop 객체에 K:V 형태로 세팅
		try {
			String filePath = TodoListDAOImpl.class.getResource("/xml/sql.xml").getPath();
			prop = new Properties();
			prop.loadFromXML(new FileInputStream(filePath));
			
		} catch (Exception e) {
			System.out.println("sql.xml 로드 중 예외 발생");
			e.printStackTrace();
		}	
	}

	// 메서드
	@Override
	public List<Todo> todoListFullview(Connection conn) throws Exception{
		
		List<Todo> todoList = new ArrayList<Todo>();			// 결과 저장용 변수 선언
		
		try {
			String sql = prop.getProperty("todoListFullView");	// SQL 문 작성(sql.xml 파일 사용)
			stmt = conn.createStatement();						// Statement 객체 생성
			rs = stmt.executeQuery(sql);						// SQL 조회 후 결과 반환
			
			while(rs.next()) {

				// Builder 패턴 : 특정 값으로 초기화된 객체를 쉽게 만드는 방법
				// -> Lombok 에서 제공하는 @Builder 어노테이션을 DTO에 작성해두면
				//	  사용 가능한 패턴
				
				boolean complete = (rs.getInt("TODO_COMPLETE") == 1);
				
				Todo todo = Todo.builder()						// 클래스명.builder()
							.todoNo(rs.getInt("TODO_NO"))		// .필드명("세팅할 데이터")
							.title(rs.getString("TODO_TITLE"))	// .필드명("세팅할 데이터")
							.complete(complete)					// .필드명("세팅할 데이터")
							.regDate(rs.getString("REG_DATE"))	// .필드명("세팅할 데이터")
							.build();							// .build()
																// 세팅 안 한 필드는 기본값 들어가 있음
				todoList.add(todo);
			}
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return todoList;
	}

	@Override
	public int getCompleteCount(Connection conn) throws Exception{
		
		int completeCount = 0;
		
		try {
			String sql = prop.getProperty("getCompleteCount");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			if(rs.next()) completeCount = rs.getInt(1);
			
		} finally {
			close(rs);
			close(stmt);
		}
		
		return completeCount;
	}

	@Override
	public int todoAdd(Connection conn, String title, String detail) throws Exception{
		
		int result = 0;
		
		try {
			String sql = prop.getProperty("todoAdd");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, detail);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	@Override
	public Todo todoDetailView(Connection conn, int todoNo) throws Exception{
		
		Todo todo = null;
		
		try {
			String sql = prop.getProperty("todoDetailView");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, todoNo);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				boolean complete = (rs.getInt("TODO_COMPLETE") == 1);
				
				todo = Todo.builder()
						.todoNo(rs.getInt("TODO_NO"))
						.title(rs.getString("TODO_TITLE"))
						.detail(rs.getString("TODO_DETAIL"))
						.complete(complete)
						.regDate(rs.getString("REG_DATE"))
						.build();
			}
			
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return todo;
	}

	@Override
	public int todoComplete(Connection conn, int todoNo) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("todoComplete");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, todoNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	@Override
	public int todoDelete(Connection conn, int todoNo) throws Exception {
		int result = 0;

		try {
			String sql = prop.getProperty("todoDelete");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, todoNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}

	@Override
	public int todoUpdate(Connection conn, int todoNo, String title, String detail) throws Exception {
		int result = 0;
		
		try {
			String sql = prop.getProperty("todoUpdate");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, detail);
			pstmt.setInt(3, todoNo);
			
			result = pstmt.executeUpdate();
			
		} finally {
			close(pstmt);
		}
		
		return result;
	}
	
}
