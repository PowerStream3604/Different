<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>

<meta name="viewport" content="width=device-width,initial-scale=1.0" />
<script src="../js/jquery-1.11.0.min.js"></script>


<link rel="stylesheet" href="css/style.css">
<link href="https://fonts.googleapis.com/css?family=Ubuntu"
	rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<link rel="stylesheet" href="Login.css">
<script src="login.js"></script>
<title>Sign in</title>

<%
String id = "";

try{
	// id ���� �Ӽ��� ���� ���� id������ ����
	// ������ ������� ��� id���� �Ӽ��� �� null �Ǵ� ������ �ƴ�
	id = (String)session.getAttribute("id");
	
%>

<%if(id == null || id.equals("")){// �������� ���� ����� ���� %>
<div id="status">


	<body>
		<div class="main">
			<p class="sign" align="center">Sign in</p>
			<div class="form1">
				<input id="id" class="username" type="text" align="center"
					placeholder="Username"> <input id="password"
					class="password" type="password" align="center"
					placeholder="Password"> <a id="login" class="submit"
					align="center">Sign in</a>
				<p class="forgot" align="center">
					<a href="#">Forgot Password?
				</p>


			</div>
		</div>
	</body>

</div>
<%}else{// ������ ����� ���� %>

<div id="status">
	<ul>
		<li><b><%=id %></b>���� �α��� �ϼ̽��ϴ�.
		<li class="label2"><button id="logout">�α׾ƿ�</button>
			<button id="update">ȸ�� ���� ����</button>
	</ul>
</div>
<%}}catch(Exception e){e.printStackTrace(); } %>



