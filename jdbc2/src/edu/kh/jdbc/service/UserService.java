package edu.kh.jdbc.service;

// (★★) import static : 지정된 경로에 존재하는 static 구문을 모두 얻어와
// 클래스명.메서드명()이 아닌 메서드명()만 작성해도 호출가능하게 하는 구문
import static edu.kh.jdbc.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import edu.kh.jdbc.common.JDBCTemplate;
import edu.kh.jdbc.dao.UserDAO;
import edu.kh.jdbc.dto.User;

public class UserService {
	
	// 필드
	private UserDAO dao = new UserDAO();

	//메서드
	/** 전달받은 ID와 일치하는 USER 정보를 반환하는 서비스
	 * @param input (입력된 아이디)
	 * @return 아이디가 일치하는 회원 정보(User 타입), 없으면 null
	 */
	public User selectId(String input) {
		// 커넥션 생성(서비스 단에서 이루어짐!!)
		Connection conn = JDBCTemplate.getConnection();
		
		// DAO 메서드 호출 후 결과 반환 받기
		User user = dao.selectId(conn, input);
		
		// 다 쓴 커넥션 닫기
		JDBCTemplate.close(conn);
		
		// DB에서 조회한 결과를 반환
		return user;
	}

	/** 1. User 등록 서비스
	 * @param user : 입력 받은 id, pw, name 이 세팅된 객체
	 * @return 삽입 성공환 결과 행의 개수
	 */
	public int insertUser(User user) throws Exception {
		
		// 1. 커넥션 생성
		Connection conn = getConnection();
		
		// 2. 데이터 가공 (할 거 없으면 넘어감)
		
		// 3. DAO 메서드 호출(INSERT) 후 결과 반환(삽입 성공한 행 개수, int) 받기
		int result = dao.insertUser(conn, user);
		
		// 4. INSERT 수행결과에 따라 트랜젝션 제어 처리
		if(result > 0) commit(conn);		// INSERT 성공 시
		else rollback(conn);				// INSERT 실패 시
		
		// 5. Connection 반환하기
		close(conn);
		
		// 6. 결과 반환
		return result;
	}

	/** 2. User 전체 조회 서비스
	 * @return 조회된 User 가 담긴 List
	 */
	public List<User> selectAll() throws Exception {
		// 1. 커넥션 생성
		Connection conn = getConnection();
		
		// 2. 데이터 가공 (할 거 없으면 넘어감)
		
		// 3. DAO 메서드 호출(SELECT) 후 결과(List<User>) 반환
		List<User> userList = dao.selectAll(conn);
		
		// 4. DML 인 경우 트랜젝션 제어 처리 (여긴 SELECT이므로 안해도 됨!)
		
		// 5. Connection 반환
		close(conn);
		
		// 6. 결과 반환
		return userList;
	}

	/** 3. User 중 이름에 검색어가 포함된 회원 조회 서비스
	 * @param USER_NAME를 검색할 input
	 * @return 조회된 User 가 담긴 ArrayList(비어있는 경우 포함)
	 */
	public List<User> selectName(String input) throws Exception{
		// 1. 커넥션 생성
		Connection conn = getConnection();
		
		// 2. 데이터 가공 (할 거 없으면 pass)
		
		// 3. DAO 메서드 호출(SELECT) 후 결과(List<User>) 반환
		List<User> selectUserList = dao.selectName(conn, input);
		
		// 4. DML 인 경우 트랜젝션 제어 처리 (SELECT 이므로 pass)
		
		// 5. Connection 반환
		close(conn);
		
		// 6. 결과 반환
		return selectUserList;
	}

	/** 4. USER_NO를 입력 받아 일치하는 User 조회 서비스
	 * @param input : USER_NO
	 * @return 조회된 User 객체 또는 null(해당 USER_NO 없는 경우)
	 */
	public User selectUser(int input) throws Exception{
		Connection conn = getConnection();
		
		User user = dao.selectUser(conn, input);
		
		close(conn);
		
		return user;
	}

	/** 5. USER_NO를 입력 받아 일치하는 User 삭제 서비스
	 * @param input : 삭제할 USER_NO
	 * @return 삭제 성공 시 1, 실패 시 0
	 */
	public int deleteUser(int input) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.deleteUser(conn, input);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}

	/** 6-1. ID, PW가 일치하는 회원 조회 서비스
	 * @param user : USER_ID, USER_PW 가 들어있는 상태
	 * @return SELECT 된 행이 있으면 User 객체 1개, 없으면 null
	 */
	public User selectUser(User user) throws Exception{
		Connection conn = getConnection();
		User selectUser = dao.selectUser(conn, user);
		
		close(conn);
		
		return selectUser;
	}

	/** 6-2. ID, PW가 일치하는 회원이 있을 경우 이름 수정 서비스
	 * @param user : USER_NO, USER_ID, USER_PW, USER_NAME, ENROLL_DATE 들어있는 상태 
	 * @return 수정 성공하면 성공한 행의 개수(1), 실패하면 0
	 */
	public int updateName(User user) throws Exception{
		Connection conn = getConnection();
		
		int result = dao.updateName(conn, user);
		
		if(result > 0) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return result;
	}

	/** (강사님 풀이) 30. User 중 이름에 검색어가 포함된 회원 조회 서비스
	 * @param keyword : 입력한 키워드
	 * @return searchList : 조회된 회원 리스트
	 */
	public List<User> selectNameTeacher(String keyword) throws Exception {
		// 1. 커넥션 생성
		Connection conn = getConnection();
		
		// 2. 데이터 가공 (없으면 패스)
		
		// 3. DAO 메서드 호출 후 결과 반환
		List<User> searchList = dao.selectNameTeacher(conn, keyword);
		
		// 4. DML(INSERT, UPDATE, DELETE)인 경우 트랜젝션 처리 (SELECT는 패스)
		
		// 5. Connection 반환
		close(conn);
		
		// 6. 결과 반환
		return searchList;
	}

	/** (강사님 풀이) 40. USER_NO를 입력 받아 일치하는 User 조회 서비스
	 * @param input 입력한 사용자번호
	 * @return User (조회된 회원 정보 객체 또는 null)
	 */
	public User selectUserTeacher(int input) throws Exception {
		// 1. 커넥션 생성
		Connection conn = getConnection();
		
		// 2. 데이터 가공 (없으면 패스)
		
		// 3. DAO 메서드 호출 후 결과 반환
		User user = dao.selectUserTeacher(conn, input);
		
		// 4. DML(INSERT, UPDATE, DELETE)인 경우 트랜젝션 처리 (SELECT는 패스)
		
		// 5. Connection 반환
		close(conn);
		
		// 6. 결과 반환
		return user;
	}

	/** (강사님 풀이) 50. USER_NO를 입력받아 USER 삭제 서비스
	 * @param input
	 * @return result
	 */
	public int deleteUserTeacher(int input) throws Exception{
		Connection conn = getConnection();					// 1. 커넥션 생성
		
		int result = dao.deleteUserTeacher(conn, input);	// 3. DAO 메서드 호출 후 결과 반환
		
		if(result > 0) commit(conn);						// 4. 결과에 따른 트랜젝션 처리
		else rollback(conn);
		
		close(conn);										// 5. Connection 반환
		
		return result;
	}

	/** (강사님 풀이) 61. ID, PW가 일치하는 회원(SELECT)의 USER_NO 조회 서비스
	 * @param userId
	 * @param userPw
	 * @return
	 */
	public int selectUserNoTeacher(String userId, String userPw) throws Exception {
		Connection conn = getConnection();				// 커넥션 생성
		
		int userNo = dao.selectUserTeacher(conn, userId, userPw);	// DAO 호출 후 결과 반환 받기
		
		close(conn);									// 커넥션 객체 자원 반환
		return userNo;									// 결과 반환
	}

	/** (강사님 풀이) 62. userNo가 일치하는 User의 이름 수정 서비스
	 * @param userName
	 * @param userNo
	 * @return result (수정된 행의 개수)
	 */
	public int updateNameTeacher(String userName, int userNo) throws Exception{
		Connection conn = getConnection();				// 커넥션 생성
		
		int result = dao.updateNameTeacher(conn, userName, userNo);	// DAO 호출 후 결과 반환 받기
		
		if(result > 0) commit(conn);					// 트랜젝션 제어
		else rollback(conn);
		
		close(conn);									// 커넥션 객체 자원 반환
		return result;									// 결과 반환
	}

	/** 7-1. 아이디 중복 확인 서비스(SELECT 이용)
	 * @param userId
	 * @return count(중복이면 1, 아니면 0)
	 */
	public int idCheck(String userId) throws Exception{
		Connection conn = getConnection();
		int count = dao.idCheck(conn, userId);
		close(conn);
		return count;
	}

	/** 8. userList에 있는 모든 user 를 INSERT 하는 서비스
	 * @param userList
	 * @return result : 삽입된 행의 개수
	 */
	public int multiInsertUser(List<User> userList) throws Exception {
		Connection conn = getConnection();
		
		// 다중 INSERT 방법
		// 1) SQL을 이용한 다중 INSERT (나중에,,,스프링부트, 마이바티스?)
		// 2) Java 반복문을 이용한 다중 INSERT(이번에...!)
		int count =0;				// 삽입 성공한 행의 개수 count
		
		// 1행씩 삽입
		for(User user : userList) {
			int result = dao.insertUser(conn, user);
			count += result;		// 삽입 성공한 행의 개수를 count에 누적
		}
		
//		count--;					// 강제 실패처리하고 결과 확인용
		
		// 트랜젝션 제어처리
		// 전체 삽입 성공 시 commit, 아니면(일부 삽입 or 전체 실패) rollback
		if(count == userList.size()) commit(conn);
		else rollback(conn);
		
		close(conn);
		
		return count;
	}

	
	
	
}
