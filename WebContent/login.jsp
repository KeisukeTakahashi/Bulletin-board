<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>ログイン</title>
	<link href="./css/login.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="main-contents">

<p class="form-title">ログイン</p>

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

<form action="login" method="post"><br />
	<p>ログインID</p>
	<p class="login_id"><input name="login_id" value="${loginId}" id="login_id"/></p>
	<p>パスワード</p>
	<p class="password"><input name="password" type="password" id="password"/></p>
	<div class="submit"><input type="submit" value="ログイン" /></div>
</form>

</div>
</body>
</html>