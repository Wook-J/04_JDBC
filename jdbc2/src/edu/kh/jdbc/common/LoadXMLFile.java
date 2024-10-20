package edu.kh.jdbc.common;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class LoadXMLFile {
	public static void main(String[] args) {
		
		// XML 파일 읽어오기 (Properties, FileInputStream)
		try {
			Properties prop = new Properties();
			
			// driver.xml 파일을 읽기 위한 InputStream 객체 생성
			FileInputStream fis = new FileInputStream("driver.xml");
			
			// 연결된 driver.xml 파일에 있는 내용을 모두 읽어와
			// Properties 객체에 K:V 형식으로 저장
			prop.loadFromXML(fis);
			
			// Property : 속성(데이터)
			// prop.getProperty("key") : key 가 일치하는 속성값을 얻어옴
			String driver = prop.getProperty("driver");
			String url = prop.getProperty("url");
			String userName = prop.getProperty("userName");
			String password = prop.getProperty("password");
			
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, userName, password);
			
			System.out.println(conn);	// oracle.jdbc.driver.T4CConnection@7b98f307
			
			/*
			 * 왜 XML 파일을 이용해서 JDBC를 진행하는가?
			 * 
			 * 1. DB 연결정보 / 드라이버 정보 등 코드중복 제거 
			 * 2. 보안 측면에서 별도 관리 필요!
			 * 3. 재컴파일을 진행하지 않기 위해서
			 * 4. XML 파일에 작성된 문자열 형태를 그대로 읽어오기 때문에
			 * 	  xml 파일에 SQL 문 작성 시 좀더 편리해짐!
			 * 
			 * */
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
