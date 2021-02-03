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
	//LogonDBBeam ���� ��ü ���� <- �Ѱ��� ��ü�� �����ؼ� ����
	private static LogonDBBean instance = new LogonDBBean();
	
	// LogonDBBean ��ü�� �����ϴ� �޼ҵ�
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
	
	// ȸ�� ���� ó��(registerPro.jsp)���� ����ϴ� �� ���ڵ� �߰� �޼ҵ�
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
	
	// �α��� �� ó��(loginPro.jsp)�������� ����� ���� ó�� �� ȸ�� ���� ����/Ż�� ����� ����(memberCheck.jsp)���� ����ϴ� �޼ҵ�
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
					x = 1; // ���� ����
				else
					x = 0; //��й�ȣ Ʋ��
			}else//�ش� ���̵� ������ ����
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
	
	//�̸��� �ߺ�Ȯ��(�츮�� DB�ȿ� �ߺ��� �̸����� �����ϴ°��� �Ǵ��ϴ� �޼ҵ��̴�)
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
	
	//���̵� ����Ȯ��(ȸ���� ���̵� ������ �Ǿ������� Ȯ���Ѵ�)
	public int isEmailveri(String email) { // �Ű����� email
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
	
	//���̵� �ߺ� Ȯ��(confirmId.jsp)���� ���̵��� �ߺ� ���θ� Ȯ���ϴ� �޼ҵ�
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
			
			if (rs.next()) // ���̵� ����
				x=1;// ���� ���̵� ����
			
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
	// ȸ�� ���� ������(modifyForm.jsp)�� ���� ���� ���� ������ �������� �޼ҵ�
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
			// ����ڰ� �Է��� ��й�ȣ�� ���̺��� ��й�ȣ�� ������ ����
			if(BCrypt.checkpw(shaPass, dbpasswd)) {
				member = new LogonDataBean();//������ ����� ��ü ����
				member.setnickName(rs.getString("nickname"));//************nickname�� null���� �����ϱ� ������ ������ �߻��� �� �ֱ⶧���� �����ؾ���
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
		return member;// ������ ����� ��ü member ����
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
	
	// ȸ�� ���� ����ó��(modifyPro.jsp)���� ȸ�� ���� ������ ó���ϴ� �޼ҵ�
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
			
			if(rs.next()) {// �ش� ���̵� ������ ����
				String dbpasswd = rs.getString("passwd");
				if(BCrypt.checkpw(shaPass, dbpasswd)) {
					pstmt = conn.prepareStatement("update member set name=?, nickname=?, phone_num=? where email = ?");
					pstmt.setString(1, member.getName());
					pstmt.setString(2, member.getnickName());
					pstmt.setString(3, member.getPhone_num());
					pstmt.setString(4, member.getEmail());
					pstmt.executeQuery();
					x = 1;// ȸ�� ���� ���� ó�� ����
				}else
					x = 0;//ȸ�� ���� ���� ó�� ����
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
	
	
	//ȸ�� Ż�� ó��(deletePro.jsp)���� ȸ�� ������ �����ϴ� �޼ҵ�
	public int deleteMember(String email, String passwd) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = -1;
		SHA256 sha = SHA256.getInstance();
		try {
			conn = getConnection();
			
			String orgPass = passwd; // ��� �� �κ��� ���� �ʿ����� �ʰ� ���࿡ ��ȣȭ�� ������ ��� �̸� �̿��� Ű�� ���� ��ȣȭ�� ����� ���̴�.
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
					x = 1;// ȸ�� Ż�� ó�� ����
				}else
					x = 0; //ȸ�� Ż�� ó�� ����
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







