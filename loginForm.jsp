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
	// id 세션 속성의 값을 얻어내서 id변수에 저장
	// 인증된 사용자의 경우 id세션 속성의 값 null 또는 공백이 아님
	id = (String)session.getAttribute("id");
	
%>

<%if(id == null || id.equals("")){// 인증되지 않은 사용자 영역 %>
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
<%}else{// 인증된 사용자 영역 %>

<div id="status">
	<ul>
		<li><b><%=id %></b>님이 로그인 하셨습니다.
		<li class="label2"><button id="logout">로그아웃</button>
			<button id="update">회원 정보 변경</button>
	</ul>
</div>
<%}}catch(Exception e){e.printStackTrace(); } %>



