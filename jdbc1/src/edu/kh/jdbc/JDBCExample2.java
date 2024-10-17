package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class JDBCExample2 {

	public static void main(String[] args) {
		// 입력 받은 급여보다 초과해서 받은 사원의
		// 사번, 이름, 급여 조회
		
		// 1. JDBC 객체 참조형 변수 선언(Connection, Statement, ResultSet 등)
		Connection conn = null;						// DB 연결정보 저장 객체
		Statement stmt = null;						// SQL 수행, 결과 반환용 객체
		ResultSet rs = null;						// SELECT 결과 수행 결과 저장 객체
		
		try {
			// 2. DriverManager 객체를 이용해서 Connection 객체 생성
			// 2-1) Oracle JDBC Driver 객체 메모리 로드
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2-2) DB 연결 정보 작성
			String type = "jdbc:oracle:thin:@";		// 드라이버의 종류
			String host = "localhost";				// DB 서버 컴퓨터의 IP 또는 도메인 주소 (localhost == 현재컴퓨터)
			String port = ":1521";					// 프로그램 연결을 위한 port 번호
			String dbName = ":XE";					// DBMS 버전 이름(XE == eXpress Edition)

			String userName = "kh_jwj";				// 사용자 계정명
			String password = "kh1234";				// 계정 비밀번호
			
			// 2-3) DB 연결 정보와 DriverManager 를 이용해서 Connection 객체 생성
			conn = DriverManager.getConnection(type + host + port + dbName, userName, password);
										// jdbc:oracle:thin:@localhost:1521:XE
			
			// 3. SQL 작성
			// 입력받은 급여 -> Scanner 필요
			Scanner sc = new Scanner(System.in);
			
			// int input에 입력받은 급여 담기
			System.out.print("입력할 급여 : ");
			int input = sc.nextInt();
			
			// 4. Statement 객체 생성
			stmt = conn.createStatement();
			
			// 5. Statement 객체를 이용하여 SQL 수행 후 결과 반환 받기
			String sql = "SELECT EMP_ID, EMP_NAME, SALARY FROM EMPLOYEE WHERE SALARY > " + input;
			// executeQuery() : SELECT 실행 후 ResultSet 반환
			// executeUpdate() : DML(INSERT, UPDATE, DELETE) 실행 후 성공한 결과행의 개수(int) 반환
			rs = stmt.executeQuery(sql);
			
			// 6. 조회 결과가 담겨 있는 ResultSet 을 커서(Cursor) 이용하여
			//    1행씩 접근해 각 행에 작성된 컬럼값 얻어오기
			//    -> while 문 안에서 꺼낸 데이터 출력
			//   [콘솔창 예시]
			//	201 / 송중기 / 6000000원
			//	202 / 노옹철 / 3700000원 ...
			while(rs.next()) {
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				int salary = rs.getInt("SALARY");
				
				System.out.printf("%s / %s / %d원\n", empId, empName, salary);
			}

		} catch (ClassNotFoundException e) {
			System.out.println("해당 class를 찾을 수 없습니다");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch(InputMismatchException e) {
			System.out.println("알맞은 형식을 입력하세요");
			e.printStackTrace();
			
		} finally {
			try {
				// 7. 사용 완료된 JDBC 객체 자원 반환 (close) -> 생성된 역순으로 close 하기
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
