<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <base href="<%=request.getContextPath()+"/" %>">
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>orders_search welcome</title>
  <script type="text/javascript" src="js/jquery.min.js" ></script>
  <style>
  dl dt {float:left}
  </style>
  <script type="text/javascript">
  $(function (){
	  $('#searchBtn').click(function(){
		  var data = JSON.stringify({"searchName":$('#searchName').val(),"userId":$('#userId').val(),"pageNo":1,"pageSize":10});
		  $.ajax({  
			    url:"search",
			    type:"POST",  
			    dataType:"json",  
			    data:data,  
			    contentType: "application/json;charset=utf-8",
			    headers: {'Content-Type': 'application/json;charset=UTF-8'},  
			    success: function (ret) {  
			    	if(ret.error){
			    		alert(ret.error.errCode+":"+ret.error.errMsg);
			    		return;
			    	}
			    	//alert("共查出记录："+ret.totalCount);
			    	var $tr = $('#listTable tbody').empty();
			    	for(var i in ret.resultList){
			    		var p = ret.resultList[i];
			    		//alert("p:"+p);
			    		$tr.append('<tr> <td>'+p.code+'</td><td>'+p.name+'</td><td>'+p.price+'</td><td>'
			    				+p.storeName+'</td><td>'+p.typeName+'</td><td>'+p.updTime+'</td> </tr>');
			    		
			    	}
			    },
			    error:function(err){
			    	//alert("网络异常："+err.responseText);
			    	alert("网络异常："+err.status);
			    }
		  });
		  
	  });
	  
  });
  </script>
</head>
<body>
	<header>
		<h1 style="text-align:center">欢迎访问 orders-商品搜索模块。</h1>
	</header>
	<br>
	<section style="margin:10px auto;width:80%">
		<select id="userId" >
			<option value="">选择用户</option>
			<option value="1" >用户1</option>
			<option value="2" >用户2</option>
		</select>
		<input id="searchName" type="text" value="" > 
		<button id="searchBtn" >搜索</button>
	</section>
	<div id="content" style="margin:10px auto;width:80%">
	 	<table id="listTable" style="width:100%">
	 		<thead>
	 			<tr>
	 				<th>code</th>
	 				<th>name</th>
	 				<th>price</th>
	 				<th>store name</th>
	 				<th>type name</th>
	 				<th>update time</th>
	 			</tr>
	 		</thead>
	 		<tbody>
	 			
	 		</tbody>
	 	</table>
	</div>
	<div id="pageFoot">
		
	</div>
	
</body>
</html>