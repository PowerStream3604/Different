var status = true;

$(document).ready(function(){
	//[ID중복확인]버튼을 클릭하면 자동실행
	//입력한 아이디 값을 갖고 confirmId.jsp페이지 실행
	$("#checkId").click(function(){
	  if($("#id").val()){
		//아이디를 입력하고 [ID중복확인]버튼을 클릭한 경우
		var query = {id:$("#id").val()};
		
	    $.ajax({
	    	type:"post",//요청방식
	    	url:"confirmId.jsp",//요청페이지
	    	data:query,//파라미터
	    	success:function(data){//요청페이지 처리에 성공시
	    		if(data == 1){//사용할 수 없는 아이디
	    			alert("사용할 수 없는 아이디");
	    	    	$("#id").val("");
	    	     }else if(data == -1)//사용할 수 있는 아이디
	    	  	    alert("사용할 수 있는 아이디");
	 	    }
	    });
	  }else{//아이디를 입력하지 않고 [ID중복확인]버튼을 클릭한 경우
		  alert("사용할 아이디를 입력");
		  $("#id").focus();
	  }
	});

	//[가입하기]버튼을 클릭하면 자동실행
	//사용자가 가입폼인 registerForm.jsp페이지에 입력한 내용을 갖고
	//registerPro.jsp페이지 실행
	$("#register").click(function() {
		
			   alert($("#email").val());
		   if(status){
			  var query = {id:$("#id").val(), 
					  passwd:$("#password").val(),
				      name:$("#name").val(),
				      email:$("#email").val(),
				      phone_num:$("#phone_num").val(),
				      birth_date:$("#birth_date").val()};
			  
			  $.ajax({
			      type:"post",
			      url:"registerPro.jsp",
			      data:query,
			      success:function(data){
			    	  window.location.href("main.jsp");
			 	  }
			  });
		   }
		});
		
	checkIt(); // 입력폼에 입력한 상황 체크 -- 그리고 함수에서 보안관련 issue들을 체크합니다.
	if ($("#email").val()) {// 아이디에 값이 있으면
		
		var query = {
			email : $("#email").val()
		};
		$.ajax({
			type : "post",// 요청방식
			url : "confirmEmail.jsp",// 요청페이지
			data : query,// 파라미터
			success : function(data) {// 요청페이지 처리에 성공시
				if (data == 1) {// 사용할 수 없는 아이디
					alert("사용할 수 없는 아이디");
					$("#id").val("");
				} else if (data == -1) {// 사용할 수 있는 아이디
					alert("사용할 수 있는 아이디");

					var query_again = {
						id : $("#id").val(),// 여기 손질해야한다 id값 없고 이제는 email이 id를 대체한다
						passwd : $("#password").val(),
						name : $("#name").val(),
						email : $("#email").val(),
						phone_num : $("#phone_num").val(),
						birth_date : $("#birth_date").val()
					};
					$.ajax({
						type : "post",
						url : "registerPro.jsp",
						data : query_again,
						success : function(data) {
							alert("registration success")
							var query_a_g = {email:$("#email").val()};
							$.ajax({
								type : "post",
								url : "emailSendAction.jsp",
								data : query_a_g,
								success: function(data){
									alert("email is sent");
									window.location.href("emailSent.html");
								}
							})
							
						}
					});
				}
			}
		});
	}
	

	// [취소]버튼을 클릭하면 자동실행
	$("#cancel").click(function(){
		window.location.href("main.jsp");
	});
});	
//사용자가 입력폼에 입력한 상황을 체크
function checkIt() {
	status = true;
	
/*    if(!$("#id").val()) {//아이디를 입력하지 않으면 수행
        alert("아이디를 입력하세요");
        $("#id").focus();
        status = false;
        return false;//사용자가 서비스를 요청한 시점으로 돌아감
    }*/
    
    if(!$("#password").val()) {//비밀번호를 입력하지 않으면 수행
        alert("비밀번호를 입력하세요");
        $("#password").focus();
        status = false;
        return false;
    }
    //비밀번호와 재입력비밀번호가 같지않으면 수행
    if($("#password").val() != $("#password_conf").val()){
        alert("비밀번호를 동일하게 입력하세요");
        $("#password_conf").focus();
        status = false;
        return false;
    }
    
    if(!$("#name").val()) {//이름을 입력하지 않으면 수행
        alert("사용자 이름을 입력하세요");
        $("#name").focus();
        status = false;
        return false;
    }
    
    if(!$("#email").val()) {//주소를 입력하지 않으면 수행
        alert("이메일를 입력하세요");
        $("#address").focus();
        status = false;
        return false;
    }
    
    if(!$("#phone_num").val()) {//전화번호를 입력하지 않으면 수행
        alert("전화번호를 입력하세요");
        $("#phone_num").focus();
        status = false;
        return false;
    
    }  
}
