package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class JDBCExample5 {

	public static void main(String[] args) {

		// 아이디, 비밀번호, 이름을 입력 받아
		// TB_USER 테이블에 삽입(INSERT) 하기
		// 드라이브 수업자료 04_JDBC - TB_USER테이블.sql 이용함..!
		
		// 1. JDBC 객체 참조변수 선언
		Connection conn = null;
		
		/*
		 * java.sql.PreparedStatement (준비된 Statement)
		 * - SQL 중간에 ? (위치홀더, placeholder)를 작성하여 
		 *   ? 자리에 java 값을 대입할 준비가 되어 있는 Statement
		 * 
		 * 장점 1 : SQL 작성이 간단해짐!
		 * 장점 2 : ?에 값 대입 시 자료형에 맞는 형태의 리터럴 표기법으로 대입됨!
		 * 			ex) String 대입 -> '값' (자동으로 '' 추가)
		 * 			ex) int 대입 -> 값 
		 * 장점 3 : 성능, 속도에서 우위를 가지고 있음
		 * 
		 * ** PreparedStatement 는 Statement 의 자식
		 * 
		 * */
		
		PreparedStatement psmt = null;
		
		// SELECT 가 아니기 때문에 ResultSet 필요 없음!!
		
		try {
			// 2. DriverManager 를 이용해서 Connection 객체 생성
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			String url = "jdbc:oracle:thin:@localhost:1521:XE";
			String userName = "kh_jwj";				// 사용자 계정명
			String password = "kh1234";				// 계정 비밀번호
			
			conn = DriverManager.getConnection(url, userName, password);
			
			// 3. SQL 작성
			Scanner sc = new Scanner(System.in);
			
			System.out.print("아이디 입력 : ");
			String id = sc.nextLine();
			
			System.out.print("비밀번호 입력 : ");
			String pw = sc.nextLine();
			
			System.out.print("이름 입력 : ");
			String name = sc.nextLine();
			
			String sql = """
					INSERT INTO TB_USER 
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)
					""";
			
			/* Auto Commit 끄기!
			 * -> 왜???? 개발자가 트랜젝션을 마음대로 제어하기 위해서!!
			 * 
			 * */
			conn.setAutoCommit(false);
			
			// 4. PreparedStatement 객체 생성
			// -> 객체 생성과 동시에 SQL 이 담겨지게 됨
			// -> 미리 ? (placeholder)에 값을 받을 준비를 해야되기 때문에...!
			psmt = conn.prepareStatement(sql);
			
			// 5. ? 위치홀더에 알맞은 값 대입
			// psmt.set자료형(?순서, 대입할 값)
			psmt.setString(1, id);
			psmt.setString(2, pw);
			psmt.setString(3, name);
			// -> 여기까지 실행하면 SQL이 작성완료된 상태!
			
			// 6. SQL(INSERT) 수행 후 결과 반환 받기
			// executeQuery()  : SELECT 수행, ResultSet 반환
			// executeUpdate() : DML(INSERTT, UPDATE, DELETE) 수행, 결과 행 개수(int) 반환
			// -> 보통 DML 실패 시 0, 성공 시 0 초과된 값이 반환됨
			
			// PreparedStatement 를 이용하여 SQL 수행 시 
			// executeQuery() / executeUpdate() 매개변수 자리에 아무것도 없어야 한다!!!
			int result = psmt.executeUpdate();
			
			// 7. result 값에 따른 결과 처리 + 트랜젝션 제어처리
			if(result > 0) {		// INSERT 성공 시
				System.out.println(name + "님이 추가 되었습니다.");
				conn.commit();		// COMMIT 수행 -> DB에 INSERT 영구 반영

			} else {
				System.out.println("추가 실패");
				conn.rollback();	// 실패 시 ROLLBACK 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				// 8. 사용한 JDBC 객체 자원 반환
				if(psmt != null) psmt.close();
				if(conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
