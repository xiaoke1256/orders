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
  var intervalCode = "";
  $(function(){
	  //开启或关闭秒杀业务
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
	  
	  //秒杀
	  $("#doSecKill").click(function(){
		  var curNum = $("[name=curNum]").val();
		  intervalCode = setInterval(function(){submitSomeOrder(curNum/1000)},1);
		  //submitOrder();
	  });//
  });
  
  
  var times = 1000;
  function submitSomeOrder(num){
	  for(var i = 0  ; i<num; i++){
		  submitOrder();
	  }
	  times--;
	  if(times==0)
		  clearInterval(intervalCode);
  }
  
  function submitOrder(){
	  var productCode = $("[name=productCode]").val();
	  var productMap = {};
	  productMap[productCode]=1;
	  var data = {"payerNo":"QE31223","carriageAmt":10,"productMap":productMap};
	  /*
	  $.post("secKill/place",data,function (ret) {    
	        if (ret.status == 1) {      
	        	 $("#console").append("A order has complete!<br>")
	        } else {      
	            alert(ret.message);    
	        }  
	    },"json");
	  */
	  $.ajax({  
		    url:"secKill/place",
		    type:"post",  
		    dataType:"json",  
		    data:JSON.stringify(data),  
		    contentType: "application/json; charset=utf-8",
		    headers: {'Content-Type': 'application/json'},  
		    success: function (ret) {    
		        if (ret.payOrderNo) {      
		        	 $("#console").append("<font color=\"green\">A order has created! OrderNo : "+ret.payOrderNo+"</font><br>")
		        } else {      
		        	 $("#console").append("<font color=\"red\">"+ret.errMsg.errCode+":"+ret.errMsg.errMsg+"</font><br>");    
		        }  
		    }
		});
	  
	  $("#console").append("Submit a order!<br>")
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
				<th>STOCK NUM</th>
				<th>IN SECKILL</th>
				<th>OPER</th>
			</tr>
			<c:forEach items="${products}" var="p">
			<tr>
				<td>${p.productCode }</td>
				<td>${p.productPrice }</td>
				<td>${p.stockNum }</td>
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
		
		<br>
		
		<span>商品：</span>
		<select name="productCode">
			<c:forEach items ="${products}" var="p">
			<option value="${p.productCode}">${p.productCode}</option>
			</c:forEach>
		</select>
	
		<span>并发量：</span>
		<select name="curNum">
			<option value="1000">1000</option>
			<option value="5000">5000</option>
			<option value="10000">10000</option>
			<option value="15000">15000</option>
			<option value="20000">20000</option>
		</select>
		&nbsp;&nbsp;
		<span><a id="doSecKill" href="javascript:void(0);" >秒杀</a></span>
		
		<div id="console" style="width:100%"></div>
	</div>
	
</body>
</html>