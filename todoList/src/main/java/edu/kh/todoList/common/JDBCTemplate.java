package edu.kh.todoList.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {

	// 필드
	private static Connection conn = null;
	// -> static 메서드에서 사용가능한 필드로는 static 만 가능(선언 시 유의)
	
	// 메서드
	/** 호출 시 Connection 객체를 생성해서 반환하는 메서드 + AutoCommit 끄기
	 * @return conn
	 */
	public static Connection getConnection() {
		
		try {
			// 이전에 참조하던 Connection 객체가 존재하고 아직 close()된 상태가 아니라면
			// 새로 만들지 않고 기존 Connection 반환
			if(conn != null && !conn.isClosed()) return conn;
			
			/* driver.xml 파일 내용 읽어오기 
			 * 
			 * 이유 1 : 보안 상의 이유 (Github에 DB 연결정보 등 올리면 해킹하라는 뜻...)
			 * 		--> .gitignore 파일에 driver.xml 작성하여 git이 관리X
			 * 
			 * 이유 2 : 혹시라도 DB 연결 정보가 변경될 경우 Java 코드가 아닌 
			 * 			읽어오는 파일의 내용을 수정하면 되기때무네 Java 코드 수정 X
			 * 		--> 추가로 재컴파일 할 필요 X
			 * 
			 * */
			
			// 1. Properties 객체 생성
			// - Map의 자식 클래스
			// - K, V가 모두 String 타입
			// - xml 파일 입출력을 쉽게할 수 있는 메서드 제공
			// storeToXML() -> xml 파일 만들기
			// loadFromXML() -> xml 파일 읽어오기
			Properties prop = new Properties();
			
			// 2. Properties 메서드를 이용해서 driver.xml 파일 내용을 읽어오기
			
			String filePath = JDBCTemplate.class.getResource("/xml/driver.xml").getPath();
			// resources 폴더의 파일을 읽어오는 방법
			// -> 빌드 시 컴파일된 JDBCTemplate.class 파일의 위치에서 
			//	  /xml/driver.xml 파일을 찾아 실제 경로를 얻어오는 방법
			//	  (src/main/resources 폴더 기준으로 경로 작성)
			
//			System.out.println(filePath); // 실제 경로 확인용 나중에 주석화하기
//			/C:/workspace/04_JDBC/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/todoList/WEB-INF/classes/xml/driver.xml
			
			prop.loadFromXML(new FileInputStream(filePath));
			
			// prop에 저장된 값(driver.xml 에서 읽어온 값)을 이용해서 Connection 객체 생성
			// prop.getProperty("key") : key 가 일치하는 value 반환
			Class.forName(prop.getProperty("driver"));
			
			String url = prop.getProperty("url");
			String userName = prop.getProperty("userName");
			String password = prop.getProperty("password");
			
			conn = DriverManager.getConnection(url, userName, password);
			
			// 만들어진 Connection 객체에서 AutoCommit 끄기
			conn.setAutoCommit(false);
			
		} catch (Exception e) {
			System.out.println("커넥션 생성 중 예외 발생...");
			e.printStackTrace();
		}
		
		return conn;
	}
	
	/** 전달 받은 커넥션에서 수행한 SQL를 COMMIT 하는 메서드
	 * @param conn
	 */
	public static void commit(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.commit();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 전달 받은 커넥션에서 수행한 SQL를 ROLLBACK 하는 메서드
	 * @param conn
	 */
	public static void rollback(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.rollback();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//-----------------------------------------------
	/** 전달받은 커넥션을 close(자원 반환)하는 메서드 
	 * @param conn
	 * 아래 전부 close()인데 오버로딩되어 메서드 선언 가능!
	 */
	public static void close(Connection conn) {
		try {
			if(conn != null && !conn.isClosed()) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 전달받은 Statement + PreparedStatement 둘 다 close() 
	 * (다형성 중 업캐스팅)
	 * -> PreparedStatement는 Statement 자식
	 * @param stmt
	 */
	public static void close(Statement stmt) {
		try {
			if(stmt != null && !stmt.isClosed()) stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 전달받은 ResultSet을 close()하는 메서드
	 * @param rs
	 */
	public static void close(ResultSet rs) {
		try {
			if(rs != null && !rs.isClosed()) rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
