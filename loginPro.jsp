<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ page import="ch11.logon.LogonDBBean"%>

<% request.setCharacterEncoding("utf-8"); %>

<%
//����ڰ� �Է��� ���̵�� ��й�ȣ
String id = request.getParameter("id");
String passwd = request.getParameter("passwd");

LogonDBBean manager = LogonDBBean.getInstance();
int check = manager.userCheck(id, passwd);//����� ���� ó�� �޼ҵ�

if(check == 1)//
	session.setAttribute("id",id);

out.println(check);//ó�� ����� ��ȯ

%>