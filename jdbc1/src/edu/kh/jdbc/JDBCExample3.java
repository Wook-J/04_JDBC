package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample3 {

	public static void main(String[] args) {

		// 입력받은 최소 급여 이상, 최대 급여 이하를 받는
		// 사원의 사번, 이름, 급여를 급여 내림차순으로 조회
		// -> 이클립스 콘솔에 출력
		
		// [실행화면]
		// 최소 급여 : 1000000
		// 최대 급여 : 3000000
		// (사번) / (이름) / (급여) 
		// (사번) / (이름) / (급여)
		// ...
		
		// 1. JDBC 객체 참조 변수 선언
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;						// -> SQL 문이 SELECT 일 때
		
		try {
			// 2. Driver Manager 객체를 이용해서 Connection 객체 생성
			Class.forName("oracle.jdbc.driver.OracleDriver");

			String type = "jdbc:oracle:thin:@";		// 드라이버의 종류
			String host = "localhost";				// DB 서버 컴퓨터의 IP 또는 도메인 주소 (localhost == 현재컴퓨터)
			String port = ":1521";					// 프로그램 연결을 위한 port 번호
			String dbName = ":XE";					// DBMS 버전 이름(XE == eXpress Edition)
			// jdbc:oracle:thin:@localhost:1521:XE

			String userName = "kh_jwj";				// 사용자 계정명
			String password = "kh1234";				// 계정 비밀번호
			
			conn = DriverManager.getConnection(type+host+port+dbName, userName, password);
			
			// 3. SQL 작성
			Scanner sc = new Scanner(System.in);
			System.out.print("최소 급여 : ");
			int min = sc.nextInt();
			System.out.print("최대 급여 : ");
			int max = sc.nextInt();
			
			/*
			String sql = "SELECT EMP_ID, EMP_NAME, SALARY FROM EMPLOYEE "
					+ "WHERE SALARY BETWEEN " + min +" AND " + max
					+ " ORDER BY SALARY DESC";
			 * -> 이렇게 하려면 space를 모두 고려해야함..!
			 * */
			
			// 위의 방식 대신 SQL문법을 활용할 수 있는 방법...!(맨앞과 맨뒤에 쌍따옴표 3개씩 작성)
			String sql = """
					SELECT EMP_ID, EMP_NAME, SALARY FROM EMPLOYEE
					WHERE SALARY BETWEEN
					""" + min + " AND " + max
					+ " ORDER BY SALARY DESC";
			
			// 4. Statement 객체 생성
			stmt = conn.createStatement();
			
			// 5. SQL 수행 후 결과 반환 받기
			rs = stmt.executeQuery(sql);
			
			// 6. 1행씩 접근해서 컬럼값 얻어오기
			int count = 0;
			
			while(rs.next()) {
				count++;
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				int salary = rs.getInt("SALARY");
				
				System.out.printf("%s / %s / %d\n", empId, empName, salary);
			}
			
			System.out.println("총원 : " + count + "명");
			
		} catch (Exception e) {
			e.printStackTrace();					// 예외 추적
			
		} finally {
			
			// 7. 사용완료한 JDBC 객체 자원 반환(close)
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
