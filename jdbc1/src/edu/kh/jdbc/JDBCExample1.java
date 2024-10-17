package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// ctrl + F11 : Run(실행)
public class JDBCExample1 {
	public static void main(String[] args) {
		/* JDBC(Java DataBase Connectivity)
		 * 
		 * - Java 에서 DB 에 연결할 수 있게 해주는 Java API (Java 에서 제공하는 코드)
		 *   -> java.sql 패키지에 있음!
		 * */
		
		// Java 코드를 이용해 EMPLOYEE 테이블에서
		// 사번, 이름, 부서코드, 직급코드, 급여, 입사일 조회 후
		// 이클립스 콘솔에 출력!
		
		/* 1. JDBC 객체 참조용 변수 선언 */
		
		// java.sql.Connection : 특정 DBMS와 연결하기 위한 정보를 저장한 객체
		// == DBeaver 에서 사용하는 DB 연결과 같은 역할의 객체
		//    (DB의 서버주소(local host), 포트번호, DB 이름(XE), 계정명, 비밀번호)
		Connection conn = null;
		
		// java.sql.Statement
		// 1) 만들어진 SQL를 Java -> DB로 전달
		// 2) DB에서 SQL 수행결과를 반환받아옴! (DB -> Java)
		Statement stmt = null;
		
		// java.sql.ResultSet
		// - SELECT 조회 결과를 저장하는 객체
		// - INSERT, UPDATE, DELETE 할때는 성공한 행의 개수를 반환하므로 int형이면 충분!
		ResultSet rs = null;
		
		try {
			
			/* 2. DriverManager 객체를 이용해서 Connection 객체 생성하기 */
			
			// java.sql.DriverManager
			// - DB 연결정보와 JDBC 드라이버를 이용해서 원하는 DB와 연결할 수 있는
			//   Connection 객체를 생성하는 객체
			
			// 2-1) Oracle JDBC Driver 객체를 메모리에 로드(적재)하기
			//      DBeaver에서 Connect to a database - Driver Settings 에서 Class Name
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// Class.forName("패키지명+클래스명") : static method인 경우 class이름.메서드명 으로 호출
			// - 해당 클래스를 읽어 메모리에 적재
			// -> JVM이 프로그램 동작에 사용할 객체를 생성하는 구문
			
			// oracle.jdbc.driver.OracleDriver
			// - Oracle DBMS 연결 시 필요한 코드가 담겨있는 클래스 (Oracle 에서 만들어서 준 클래스임)
			
			// 2-2) DB 연결 정보 작성 : 
			// DBeaver에서 Connect to a database - Driver Settings URL Template에 있는 정보를 다 써야함!
			String type = "jdbc:oracle:thin:@";		// 드라이버의 종류
			String host = "localhost";				// DB 서버 컴퓨터의 IP 또는 도메인 주소 (localhost == 현재컴퓨터)
			String port = ":1521";					// 프로그램 연결을 위한 port 번호
			String dbName = ":XE";					// DBMS 버전 이름(XE == eXpress Edition)
			String userName = "kh_jwj";				// 사용자 계정명
			String password = "kh1234";				// 계정 비밀번호
			
			// 2-3) DB 연결 정보와 DriverManager 를 이용해서 Connection 객체 생성
			conn = DriverManager.getConnection(type+host+port+dbName, userName, password);
													// DB에 대한 정보, 계정명, 비밀번호
			
			// Connection 객체가 잘 생성되었는지 확인 (객체 주소 반환)
			// == DB 연결 정보에 오타가 없는지 확인
			// -> DB 연결 정보가 잘못되거나 객체 생성에 문제가 생기면 SQLException 발생
			System.out.println(conn);				// oracle.jdbc.driver.T4CConnection@397fbdb

			/* 3. SQL 작성 */
			// !! 주의사항 !!
			// -> JDBC 코드에서 SQL 작성 시 sql 구문 안에 세미콜론(;)을 작성하면 안된다...!!
			// -> "sql 명령어가 올바르게 종료되지 않았습니다" 예외 발생
			
			// DBeaver 에서 수행 후 오류가 안뜨면 세미콜론(;)만 빼고 복사, 붙여넣기
			// enter 누를 때 공백(space)을 항상 고려하기!
			String sql = "SELECT EMP_ID, EMP_NAME, DEPT_CODE, JOB_CODE, SALARY, HIRE_DATE "
					+ "FROM EMPLOYEE";
			
			/* 4. Statement 객체 생성 */
			// 연결된 DB에 SQL을 전달하고 결과를 반환받을 Statement 객체를 생성
			stmt = conn.createStatement();
			
			/* 5. Statement 객체를 이용해서 SQL 수행 후 결과 반환 받기 */
			// 1) ResultSet Statement.executeQuery(sql);
			//    -> 전달한 SQL이 SELECT 일 때 결과로 java.sql.ResultSet 을 반환

			// 2) int Statement.executeUpdate(sql);
			//    -> 전달한 SQL이 DML(INSERT, UPDATE, DELETE) 일 때 결과로 int(성공한 행의 개수) 반환
			rs = stmt.executeQuery(sql);
			
			/* 6. 조회 결과가 담긴 ResultSet을 커서(Cursor)를 이용해 1행씩 접근해 각 행에 작성된 컬럼 값 얻어오기 */
			
			// rs.next() : 커서를 다음 행으로 이동시킨 후, 이동된 행에 값이 있으면 true, 없으면 false 반환
			// - 맨 처음 호출 시 1행부터 시작
			while(rs.next()) {
				// rs.get자료형(컬럼명 | 순서);
				// - 현재 행에서 지정된 컬럼의 값을 얻어와 반환
				// -> 지정된 자료형 형태로 값이 반환됨 (자료형을 잘못 지정하면 예외 발생)
				
				// 얻으려는 형태 : 200	선동일	D9	J1	8000000	1990-02-06 00:00:00.000
				
				// [java]						[DB]
				// String					CHAR, VARCHAR2
				// int, long				NUMBER (정수만 저장된 컬럼)
				// float, double			NUMBER (정수 + 실수)
				// java.sql.Date			DATE
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptCode = rs.getString("DEPT_CODE");
				String jobCode = rs.getString("JOB_CODE");
				int salary = rs.getInt("SALARY");
				Date hireDate = rs.getDate("HIRE_DATE");
				
				System.out.printf("사번 : %s / 이름 %s / 부서코드 : %s / 직급코드 : %s / 급여 : %d / 입사일 : %s\n",
										empId, empName, deptCode, jobCode, salary, hireDate.toString());
			}

		}catch (ClassNotFoundException e) {
			System.out.println("해당 Class를 찾을 수 없습니다.");
			e.printStackTrace();
			
		}catch (SQLException e) {				// SQLException : DB 연결과 관련된 모든 예외의 최상위 부모
			e.printStackTrace();
			
		}finally {
			
			/* 7. 사용완료된 JDBC 객체 자원 반환 */
			// -> 이를 수행하지 않으면 DB와 연결된 Connection 이 그대로 남아 있어서 
			//    다른 클라이언트가 추가적으로 연결되지 못하는 문제가 발생할 수 있음..!!
			try {								// 만들어진 역순으로 close 수행하는 것을 권장!
				
				// if 문은 NullPointerException 방지용 구문
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(conn != null) conn.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
	}
}














