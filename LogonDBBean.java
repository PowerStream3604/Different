package ch11.logon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import work.crypt.BCrypt;
import work.crypt.SHA256;


public class LogonDBBean {
	//LogonDBBeam 전역 객체 생성 <- 한개의 객체만 생성해서 공유
	private static LogonDBBean instance = new LogonDBBean();
	
	// LogonDBBean 객체를 리턴하는 메소드
	public static LogonDBBean getInstance() {
		return instance;
	}
	
	private LogonDBBean() {
	}
	
	private Connection getConnection() throws Exception{
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/jsptest");
		return ds.getConnection();
	}
	
	// 회원 가입 처리(registerPro.jsp)에서 사용하는 새 레코드 추가 메소드
	public void insertMember(LogonDataBean member) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		SHA256 sha = SHA256.getInstance();
		try {
			conn = getConnection();
			String orgPass = member.getPasswd();
			String shaPass = sha.getSha256(orgPass.getBytes());
			String bcPass = BCrypt.hashpw(shaPass, BCrypt.gensalt());
			pstmt = conn.prepareStatement(
					"insert into member(passwd,name,email,phone_num,register_date,birth_date) values(?,?,?,?,?,?);"
					);
			
			pstmt.setString(1, bcPass);
			pstmt.setString(2,member.getName());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, member.getPhone_num());
			pstmt.setTimestamp(5, member.getReg_date());
			pstmt.setString(6, member.getBirth_date());
			pstmt.executeUpdate();
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(pstmt != null) try{pstmt.close();}catch(SQLException ex) {}
			if(conn != null) try {conn.close();}catch(SQLException ex) {}
		}
	}
	
	// 로그인 폼 처리(loginPro.jsp)페이지의 사용자 인증 처리 및 회원 정보 수정/탈퇴를 사용자 인증(memberCheck.jsp)에서 사용하는 메소드
	public int userCheck(String id, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = -1;
		SHA256 sha = SHA256.getInstance();
		try {
			conn = getConnection();
			String orgPass = passwd;
			String shaPass = sha.getSha256(orgPass.getBytes());
			
			pstmt = conn.prepareStatement("select passwd from member where id = ?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				String dbpasswd = rs.getString("passwd");
				
				if(BCrypt.checkpw(shaPass, dbpasswd))
					x = 1; // 인증 성공
				else
					x = 0; //비밀번호 틀림
			}else//해당 아이디 없으면 수행
				x = -1;
				
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if (rs != null) try {rs.close();}catch(SQLException ex) {}
			if (pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null) try {conn.close();}catch(SQLException ex) {}
		}
		return x;
	}
	
	//이메일 중복확인(우리의 DB안에 중복된 이메일이 존재하는가를 판단하는 메소드이다)
	public int emailCheck(String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		int x = -1;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select id from member where email=?");
			pstmt.setString(1, email);		
			rs = pstmt.executeQuery();
			if(rs.next()) {
				x = 1;
			}else{
				x = -1;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(SQLException ex) {}
			if(rs != null)try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)try{conn.close();}catch(SQLException ex) {}
		}
		return x;
	}
	
	//아이디 검증확인(회원의 아이디가 검증이 되었는지를 확인한다)
	public int isEmailveri(String email) { // 매개변수 email
		int x = -1;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("select isEmailVeri from member where email=?");
			pstmt.setString(1, email);		
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(1 == rs.getInt("isEmailVeri"))
						x = 1;
			}else{
				x = -1;
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(SQLException ex) {}
			if(rs != null)try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)try{conn.close();}catch(SQLException ex) {}
		}
		return x;
	}
	
	//아이디 중복 확인(confirmId.jsp)에서 아이디의 중복 여부를 확인하는 메소드
	public int confirmemail(String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = -1;
		
		try {
			conn = getConnection();
			
			pstmt = conn.prepareStatement("select email from member where email = ?");
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
			
			if (rs.next()) // 아이디 존재
				x=1;// 같은 아이디 있음
			
			else 
				x = -1;
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs != null)try {rs.close();}catch(SQLException ex) {}
			if(rs != null)try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null)try{conn.close();}catch(SQLException ex) {}
		}
		return x;
	}
	// 회원 정보 수저폼(modifyForm.jsp)을 위한 기존 가입 정보를 가져오는 메소드
	public LogonDataBean getMember(String email, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LogonDataBean member = null;
		SHA256 sha = SHA256.getInstance();
		try {
			conn = getConnection();
			
			String orgPass = passwd;
			String shaPass = sha.getSha256(orgPass.getBytes());
			pstmt = conn.prepareStatement("select * from member where email = ?");
			pstmt.setString(1, email);
			rs = pstmt.executeQuery();
		if(rs.next()) {
			String dbpasswd = rs.getString("passwd");
			// 사용자가 입력한 비밀번호와 테이블의 비밀번호가 같으면 수행
			if(BCrypt.checkpw(shaPass, dbpasswd)) {
				member = new LogonDataBean();//데이터 저장빈 객체 생성
				member.setnickName(rs.getString("nickname"));//************nickname은 null값을 혀용하기 때문에 에러가 발생할 수 있기때문에 조심해야함
				member.setName(rs.getString("name"));
				member.setReg_date(rs.getTimestamp("register_date"));
				member.setPhone_num(rs.getString("phone_num"));
				member.setEmail(rs.getString("email"));
				member.setBirth_date(rs.getString("birth_date"));
			}
		}
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null) try {conn.close();}catch(SQLException ex) {}
			
		}
		return member;// 데이터 저장빈 객체 member 리턴
	}
	
	public void setUserEmailChecked(String email) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement("update member set isEmailVeri="+true+" where email = ?");
			pstmt.setString(1, email);
			pstmt.executeUpdate();
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null) try {conn.close();}catch(SQLException ex) {}
		}
	}
	
	// 회원 정보 수정처리(modifyPro.jsp)에서 회원 정보 수정을 처리하는 메소드
	public int updateMember(LogonDataBean member) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = -1;
		SHA256 sha = SHA256.getInstance();
		try {
			conn = getConnection();
			
			String orgPass = member.getPasswd();
			String shaPass = sha.getSha256(orgPass.getBytes());
			pstmt = conn.prepareStatement("select passwd from member where email = ?");
			pstmt.setString(1, member.getEmail());
			rs = pstmt.executeQuery();
			
			if(rs.next()) {// 해당 아이디가 있으면 수행
				String dbpasswd = rs.getString("passwd");
				if(BCrypt.checkpw(shaPass, dbpasswd)) {
					pstmt = conn.prepareStatement("update member set name=?, nickname=?, phone_num=? where email = ?");
					pstmt.setString(1, member.getName());
					pstmt.setString(2, member.getnickName());
					pstmt.setString(3, member.getPhone_num());
					pstmt.setString(4, member.getEmail());
					pstmt.executeQuery();
					x = 1;// 회원 정보 수정 처리 성공
				}else
					x = 0;//회원 정보 수정 처리 실패
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}finally {
			if(rs!=null)try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null) try {conn.close();}catch(SQLException ex) {}
		}
		return x;
		
		
	}
	
	
	//회원 탈퇴 처리(deletePro.jsp)에서 회원 정보를 삭제하는 메소드
	public int deleteMember(String email, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = -1;
		SHA256 sha = SHA256.getInstance();
		try {
			conn = getConnection();
			
			String orgPass = passwd; // 사실 이 부분은 딱히 필요하지 않고 만약에 암호화를 설정할 경우 이를 이용한 키값 비교후 복호화를 사용할 것이다.
			String shaPass = sha.getSha256(orgPass.getBytes());
			pstmt = conn.prepareStatement("select passwd from member where email = ?");
			pstmt.setString(1, email);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String dbpasswd = rs.getString("passwd");
				if(BCrypt.checkpw(shaPass, dbpasswd)) {
					pstmt = conn.prepareStatement("delete from member where email = ?");
					pstmt.setString(1, email);
					pstmt.executeUpdate();
					x = 1;// 회원 탈퇴 처리 성공
				}else
					x = 0; //회원 탈퇴 처리 실패
			}
			
			
			
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}finally {
			if(rs!=null)try {rs.close();}catch(SQLException ex) {}
			if(pstmt != null) try {pstmt.close();}catch(SQLException ex) {}
			if(conn != null) try {conn.close();}catch(SQLException ex) {}
		}
		return x;
	}
	
	
}







