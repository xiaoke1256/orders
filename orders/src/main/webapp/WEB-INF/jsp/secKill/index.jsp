<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
  <base href="<%=request.getContextPath()+"/" %>">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>orders</title>
  <script type="text/javascript" src="js/jquery.min.js"></script>
  <script type="text/javascript">
  $(function(){
	  $('.openBtn,.closeBtn').click(
		  function (){
			  var $btn = $(this);
			  var url = "secKill/";
			 // alert($btn.attr("pcode"));
			  if($btn.text()=="CLOSE")
				  url+="close";
			  else if($btn.text()=="OPEN")
				  url+="open";
			  $.post(url+"/"+$btn.attr("pcode"),{},function(result){
				 // alert($btn.html());
				  if(result=="success!"){
					  if($btn.text()=="CLOSE"){
						$btn.text("OPEN");
						$btn.parent().prev().text("NO");
					  }else if($btn.text()=="OPEN"){
						$btn.text("CLOSE");
						$btn.parent().prev().text("YES");
					  }
				  }else{
					  alert("Something wrong happen:"+result);
				  }
					  
			  },"text");
		  }
	  );
  });
  
  function doClose(){
	  
  }
 
  </script>
  
</head>
<body>
	<header>
		<h1 style="text-align:center">秒杀业务测试页面</h1>
	</header>
	<br>
	<div id="content" style="margin:10px auto;width:80%">
		<table border="1" style="width:100%">
			<tr>
				<th>CODE</th>
				<th>PRICE</th>
				<th>IN SECKILL</th>
				<th>OPER</th>
			</tr>
			<c:forEach items="${products}" var="p">
			<tr>
				<td>${p.productCode }</td>
				<td>${p.productPrice }</td>
				<td>${p.inSeckill=="1"?"YES":"NO" }</td>
				<td>
				<c:choose>
					<c:when test="${p.inSeckill eq '1'}">
						<a href="javascript:void(0);" class="openBtn" pcode="${p.productCode}">CLOSE</a>
					</c:when>
					<c:otherwise>
						<a href="javascript:void(0);" class="closeBtn" pcode="${p.productCode}">OPEN</a>
					</c:otherwise>
				</c:choose>
				</td>
			</tr>
			</c:forEach>
			
		</table>
	</div>
	
</body>
</html>