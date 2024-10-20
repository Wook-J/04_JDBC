package edu.kh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBCExample4 {

	public static void main(String[] args) {
		
		// 부서명을 입력받아 해당 부서에 근무하는 사원의
		// 사번, 이름, 부서명, 직급명을
		// 직급코드 오름차순으로 조회
		
		// 부서명 입력 : 총무부
		// 200 / 선동일 / 총무부 / 대표
		// 202 / 노옹철 / 총무부 / 부사장
		// 201 / 송종기 / 총무부 / 부사장
		
		// hint : SQL에서 문자열은 양쪽에 ''(홑따옴표) 필요
		// ex) 총무부 입력 -> '총무부'
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			// Driver Manager 객체를 이용해서 Connection 객체 생성
			Class.forName("oracle.jdbc.driver.OracleDriver");

//			String type = "jdbc:oracle:thin:@";		// 드라이버의 종류
//			String host = "localhost";				// DB 서버 컴퓨터의 IP 또는 도메인 주소 (localhost == 현재컴퓨터)
//			String port = ":1521";					// 프로그램 연결을 위한 port 번호
//			String dbName = ":XE";					// DBMS 버전 이름(XE == eXpress Edition)
			String url = "jdbc:oracle:thin:@localhost:1521:XE";

			String userName = "kh_jwj";				// 사용자 계정명
			String password = "kh1234";				// 계정 비밀번호
			
			conn = DriverManager.getConnection(url, userName, password);
			
			// SQL 작성
			Scanner sc = new Scanner(System.in);
			System.out.print("부서명 입력 : ");
			String input = sc.nextLine();
			
			String sql = """
					SELECT EMP_ID, EMP_NAME, NVL(DEPT_TITLE, '없음') "DEPT_TITLE", JOB_NAME
					FROM EMPLOYEE e
					LEFT JOIN DEPARTMENT d ON (e.DEPT_CODE = d.DEPT_ID)
					JOIN JOB j ON(e.JOB_CODE = j.JOB_CODE)
					WHERE DEPT_TITLE = '""" + input + "' ORDER BY j.JOB_CODE";
								// 여기서 밑으로 내리면 공백이 한칸 더 인식해서 일치하는 부서 없다고 나옴..!
			
			// Statement 객체 생성한 후, SQL 수행 및 결과 반환
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			
			boolean flag = true;			// 조회결과 있다면 false, 없으면 true
			
			// 1행씩 접근하여 결과 출력
			while(rs.next()) {
				flag = false;
				
				String empId = rs.getString("EMP_ID");
				String empName = rs.getString("EMP_NAME");
				String deptTitle = rs.getString("DEPT_TITLE");
				String jobName = rs.getString("JOB_NAME");
				
				System.out.printf("%s / %s / %s / %s\n", empId, empName, deptTitle, jobName);
			}
			
			if(flag) {
				System.out.println("일치하는 부서가 없습니다!");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
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
