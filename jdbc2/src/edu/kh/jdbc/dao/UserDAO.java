package edu.kh.jdbc.dao;



import static edu.kh.jdbc.common.JDBCTemplate.close;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dto.User;

// DAO (Data Access Object)
// 데이터가 저장된 곳(DB 등)에 접근하는 용도의 객체
// -> DB에 접근하여 Java 에서 원하는 결과를 얻기 위해
//	  SQL를 수행하고 결과를 반환받는 역할

public class UserDAO {
	
	// 필드
	// - DB 접근 관련한 JDBC 객체 참조변수 미리 선언
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	// 메서드
	/** 전달받은 Connection 을 이용해 DB에 접근하여
	 * 전달 받은 ID와 일치하는 USER 정보를 조회하는 메서드
	 * @param conn : Service 에서 생성한 Connction 객체
	 * @param input : View 에서 입력받은 ID
	 * @return 아이디가 일치하는 회원의 User 객체 또는 null
	 */
	public User selectId(Connection conn, String input) {
		
		User user = null;		// 결과 저장용 변수
		
		try {
			// SQL 작성
			String sql ="SELECT * FROM TB_USER WHERE USER_ID = ?";
			
			// PreparedStatement 객체 생성 (생성 시 SQL 가지고옴!)
			pstmt = conn.prepareStatement(sql);
			
			// ?(위치홀더)에 알맞은 값 대입
			pstmt.setString(1, input);
			
			// SQL 수행 후 결과 반환 받기 (pstmt 는 SQL 추가로 넣지 말기!)
			rs = pstmt.executeQuery();
			
			// 조회 결과가 있을 경우
			// -> 중복되는 아이디가 없을 경우 1행만 조회되기 때문에
			//	  while 문 보다는 if 문을 사용하는 것이 효과적임
			if(rs.next()) {			// 첫 행에 데이터가 존재한다면
				// 각 컬럼 값 얻어오기
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				
				// java.sql.Date 활용(현재 SQL이 ENROLL_DATE의 Date 타입 그대로 가져옴!)
				Date enrollDate = rs.getDate("ENROLL_DATE");
				
				// 조회된 컬럼 값을 이용해서 User 객체 생성
				user = new User(userNo, userId, userPw, userName, enrollDate.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			// 사용한 JDBC 객체 자원 반환
			JDBCTemplate.close(rs);
			JDBCTemplate.close(pstmt);
			
			// Connection 객체는 생성된 Service 에서 close 할 것임!!
		}
		
		return user;			// 결과 반환(생성된 User 객체 또는 null)
	}


	/** User 등록 DAO 메서드
	 * @param conn : (Service 에서) DB 연결정보가 담겨있는 Connection 객체
	 * @param user : (view 에서) 입력받은 id, pw, name 이 세팅된 User 객체  
	 * @return INSERT 결과 행의 개수
	 */
	public int insertUser(Connection conn, User user) throws Exception {
		// SQL 수행 중 발생하는 예외를 catch 로 처리하지 않고,
		// throws 를 이용해서 호출부러 던져 처리
		// -> 이 메서드에서는 catch 문이 필요 없음!
		
		// 1. 결과 저장용 변수 선언
		int result = 0;
		
		try {
			// 2. SQL 문 작성
			String sql = """
					INSERT INTO TB_USER 
					VALUES(SEQ_USER_NO.NEXTVAL, ?, ?, ?, DEFAULT)""";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ?(위치홀더)에 알맞은 값 대입
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserPw());
			pstmt.setString(3, user.getUserName());
			
			// 5. SQL 수행 후 결과(삽입된 행의 개수) 반환
			result = pstmt.executeUpdate();
			
		} finally {
			// 6. 사용한 JDBC 객체 자원 반환(close)(import static 구문 필요!)
			close(pstmt);
		}

		// 결과 저장용 변수에 저장된 값 반환
		return result;
	}

	/** User 전체 조회 DAO 메서드
	 * @param conn
	 * @return userList
	 */
	public List<User> selectAll(Connection conn) throws Exception {
		
		// 1. 결과 저장용 변수 선언
		// -> List 같은 컬렉션을 반환하는 경우에는
		//	  변수 선언 시 객체도 같이 생성해두자!
		List<User> userList = new ArrayList<User>();
		
		try {
			// 2. SQL 문 작성
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME, 
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') "ENROLL_DATE"  
					FROM TB_USER
					ORDER BY USER_NO ASC
					""";
			
			// 3. PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// 4. ?(위치홀더)에 알맞은 값 대입 (없으면 패스)
			
			// 5. SQL(SELECT) 수행 후 결과(ResultSet) 반환
			rs = pstmt.executeQuery();
			
			// 6. 조회 결과를 1행씩 접근하여 컬럼값 얻어오기
			// 몇 행이 조회될지 모른다	-> while 문
			// 무조건 1행이 조회된다	-> if 문
			while(rs.next()){
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");
				// java.sql.Date 타입으로 값을 저장하지 않은 이유
				// -> SQL에서 TO_CHAR()를 이용하여 문자열로 변환했음!
				
				// User 객체를 생성해 조회된 값을 담고 userList 에 추가!
				User user = new User(userNo, userId, userPw, userName, enrollDate);
				userList.add(user);
			}
			
		} finally {
			// 7. 사용한 JDBC 객체 자원 반환(close)(import static 구문 필요!)
			close(rs);
			close(pstmt);
		}

		// 조회결과가 담긴 List 반환
		return userList;
	}


	/** USER_NAME에 검색어(input)가 포함된 회원 조회 DAO 메서드
	 * @param conn
	 * @param USER_NAME에서 검색할 input
	 * @return 조회된 User 가 담긴 ArrayList
	 */
	public List<User> selectName(Connection conn, String input) throws Exception{
		// 1. 결과 저장용 변수 선언
		List<User> selectUserList = new ArrayList<User>();
		
		try {
			// 2. SQL 문 작성
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') "ENROLL_DATE"  
					FROM TB_USER
					WHERE USER_NAME LIKE ?		
					ORDER BY USER_NO ASC
					""";			// ? 부분에 이어쓰기도 가능!
			// 3. Statement 또는 PreparedStatement 객체 생성
			pstmt = conn.prepareStatement(sql);

			// 4. ?(위치홀더) 에 알맞은 값 대입
			pstmt.setString(1, "%" + input + "%");
//			pstmt.setString(1, input);		// ? 부분에 '%' || ? || '%' 로 작성한 경우
			
			// 5. SQL 수행(executeQuery, executeUpdate) 후 결과(Resultset, int) 반환
			rs = pstmt.executeQuery();
			
			// 6. (SELECT) 조회 결과를 1행씩 접근하여 컬럼값 얻어오기
			//    (행의 개수에 따라 while문 또는 if문 선택)
			while(rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");
				
				User user = new User(userNo, userId, userPw, userName, enrollDate);
				selectUserList.add(user);
			}
			
		} finally {
			// 7. 사용한 JDBC 객체 자원 반환(close) 역순
			//	  (ResultSet, PreparedStatement 또는 Statement)
			close(rs);
			close(pstmt);
		}
		
		// 8. 결과를 호출한 Service에 반환
		return selectUserList;
	}


	public User selectUser(Connection conn, int input) throws Exception{
		User user = null;
		
		try {
			String sql = """
					SELECT USER_NO, USER_ID, USER_PW, USER_NAME,
					TO_CHAR(ENROLL_DATE, 'YYYY"년" MM"월" DD"일"') "ENROLL_DATE"  
					FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, input);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				int userNo = rs.getInt("USER_NO");
				String userId = rs.getString("USER_ID");
				String userPw = rs.getString("USER_PW");
				String userName = rs.getString("USER_NAME");
				String enrollDate = rs.getString("ENROLL_DATE");
				
				user = new User(userNo, userId, userPw, userName, enrollDate);
			}
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return user;
	}


	public int deleteUser(Connection conn, int input) throws Exception{
		int result = 0;
		
		try {
			String sql = """
					DELETE FROM TB_USER
					WHERE USER_NO = ?
					""";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, input);
			
			result = pstmt.executeUpdate();
		} finally {
			close(pstmt);
		}
		
		return result;
	}


}
