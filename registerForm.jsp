<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<meta name="viewport" content="width=device-width,initial-scale=1.0" />
<link rel="stylesheet" href="../css/style.css" />
<script src="../js/jquery-1.11.0.min.js"></script>
<script src="login.js"></script>


<div id="regForm" class="box">
	<ul>
		<li><label for="id">���̵�</label> <input id="id" name="id"
			type="email" size="20" maxlength="50" placeholder="example@kings.com"
			autofocus>
			<button id="checkId">ID�ߺ�Ȯ��</button>
		<li><label for="passwd">��й�ȣ</label> <input id="passwd"
			name="passwd" type="password" size="20" placeholder="6~16�� ����/����"
			maxlength="16">
		<li><label for="repass"> ��ι�ȣ ���Է�</label> <input id="repass"
			name="repass" type="password" size="20" placeholder="��й�ȣ ���Է�"
			maxlength="16">
		<li><label for="name">�̸�</label> <input id="name" name="name"
			type="text" size="20" placeholder="ȫ�浿" maxlength="50">
		<li><label for="address">�ּ�</label> <input id="address"
			name="address" type="text" size="30" placeholder="�ּ� �Է�"
			maxlength="50">
		<li><label for="tel">��ȭ��ȣ</label> <input id="tel" name="tel"
			type="tel" size="20" placeholder="��ȭ��ȣ �Է�" maxlength="20">
		<li class="label2"><button id="process">�����ϱ�</button>
			<button id="cancel"></button>
	</ul>
</div>



