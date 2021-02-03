<%@page import="java.io.PrintWriter"%>

<%@page import="work.crypt.*"%>

<%@page import="ch11.logon.LogonDBBean"%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	request.setCharacterEncoding("UTF-8");
	LogonDBBean member = LogonDBBean.getInstance();
	SHA256 sha = SHA256.getInstance();
	request.setCharacterEncoding("UTF-8");
	String code = request.getParameter("code");
	String email = request.getParameter("email");
	
	String encry_email = sha.getSha256(email.getBytes());
	//System.out.println(encry_email);
	//System.out.println(code);
	boolean rightCode = code.equals(encry_email)? true:false; // 이거 참인지 아닌지 잘 확인해야 한다
	if(rightCode == true){
		System.out.println("wowwow");
		member.setUserEmailChecked(email);
	}
	if(rightCode == false){
		System.out.println("이메일로 보내지는 code : " + code);
		System.out.println("데이터베이스에서 가지고온 email : " + email);
	}
%>

