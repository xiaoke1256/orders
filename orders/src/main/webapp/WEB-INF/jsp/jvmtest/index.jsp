<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
  <base href="<%=request.getContextPath()+"/" %>">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>trans_test welcome</title>
  <script type="text/javascript" src="js/jquery.min.js"></script>
  <script type="text/javascript">
  $(function(){
	  $("#bizLink").click(function(){
		  $.post("oom/someBussiness",{},function(){
			  alert("success!");
		  },"text");
	  });
  });
 
  </script>
  
</head>
<body>
	<header>
		<h1 style="text-align:center">测试内存泄漏的页面</h1>
	</header>
	<br>
	
	<div id="content" style="margin:10px auto;width:80%">
	  	<a id="bizLink" href="javascript:void(0);">内存泄漏</a>
	</div>
	
	
</body>
</html>