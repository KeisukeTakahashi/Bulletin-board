<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>新規投稿画面</title>
	<link href="./css/post.css" rel="stylesheet" type="text/css">
</head>
<body>

<div class="header">
		<a href="./">ホーム</a> <br />
</div>

<div class="main-contents">

<p class="form-title">新規投稿</p>

<c:if test="${ not empty errorMessages }">
	<div class="errorMessages">
		<ul>
			<c:forEach items="${errorMessages}" var="message">
				<li><c:out value="${message}" />
			</c:forEach>
		</ul>
	</div>
	<c:remove var="errorMessages" scope="session"/>
</c:if>

	<div class="form-area">
		<form action="post" method="post"><br />
			<p>件名(30文字以内)</p>
	    	<p class="subject"><input name="subject" value="${subject}" id="subject"/></p>

 		 <p>カテゴリー(10文字以内)</p>
  		 <p class="lists.category"><input type="text" name="lists.category" value="${category}" list="categorys" autocomplete="off" id="lists.category"></p>
  			<datalist id="categorys">
				<c:forEach items="${categorys}" var="lists">
						<option value="${lists.category}" selected>${lists.category}</option>
				</c:forEach>
			</datalist><br />
			<p>本文(1000文字以内)</p>
			<p class="message"><textarea name="message" cols="50" rows="7" class="post-box">${message}</textarea></p>
			<br />
			<div class="submit"><input type="submit" value="投稿"></div>
		</form>
	</div>

</div>
</body>
</html>