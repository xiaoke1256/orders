<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
  <base href="<%=request.getContextPath()+"/" %>">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>orders</title>
  <script type="text/javascript" src="js/jquery.min.js"></script>
  <script type="text/javascript">
  $(function(){
	  $("#biz1Link").click(function(){
		  $.post("deadLock/bussieness1",{},function(result){
			  alert(result);
		  },"text");
	  });
	  
	  $("#biz2Link").click(function(){
		  $.post("deadLock/bussieness2",{},function(result){
			  alert(result);
		  },"text");
	  });
	  
	  $("#deadLink").click(function(){
		  $.post("deadLock/bussieness1",{},function(result){
			  alert("bussieness1:"+result);
		  },"text");
		  
		  $.post("deadLock/bussieness2",{},function(result){
			  alert("bussieness2:"+result);
		  },"text");
	  });
  });
 
  </script>
  
</head>
<body>
	<header>
		<h1 style="text-align:center">测试死锁的页面</h1>
	</header>
	<br>
	
	<div id="content" style="margin:10px auto;width:80%">
	  	<a id="biz1Link" href="javascript:void(0);">业务1</a><br>
	  	<a id="biz2Link" href="javascript:void(0);">业务2</a><br>
	  	<a id="deadLink" href="javascript:void(0);">死锁</a><br>
	</div>
	
	
</body>
</html>