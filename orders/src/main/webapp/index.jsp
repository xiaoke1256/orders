<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
  <base href="<%=request.getContextPath()+"/" %>">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <title>trans_test welcome</title>
  <style>
  dl dt {float:left}
  </style>
</head>
<body>
	<header>
		<h1 style="text-align:center">欢迎访问 orders</h1>
	</header>
	<br>
	<section style="margin:10px auto;width:80%">
		orders 将来可实现的计划如下：
	</section>
	<div id="content" style="margin:10px auto;width:80%">
	  <dl>
		<dt>1</dt>
		<dd>引入Maven（OK）</dd>
		<dt>2</dt>
		<dd>用Redis处理秒杀业务。<a href="secKill/">GO</a></dd>
		<dt>3</dt>
		<dd>后台定时任务将用zookeeper协调。</dd>
		<dt>4</dt>
		<dd>引入Spring-boot/Spring-cloud</dd>
	  </dl>
	</div>
	
	
</body>
</html>