<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="ch11.logon.LogonDBBean"%>

<% request.setCharacterEncoding("utf-8"); %>

<%
//사용자가 입력한 아이디와 비밀번호
String id = request.getParameter("id");
String passwd = request.getParameter("passwd");

LogonDBBean manager = LogonDBBean.getInstance();
int check = manager.userCheck(id, passwd);//사용자 인증 처리 메소드

if(check == 1)//
	session.setAttribute("id",id);

out.println(check);//처리 결과를 반환

%>