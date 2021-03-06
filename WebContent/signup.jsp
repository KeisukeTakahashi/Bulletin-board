<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>新規ユーザー登録</title>
<link href="./css/signup.css" rel="stylesheet" type="text/css">
</head>
<body>

   <div class="header">
			<a href="./">ホーム</a>
			<a href="control">管理画面</a><br />
		</div>

	<div class="main-contents">
		<p class="form-title">新規ユーザー登録</p>

		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">
				<ul>
					<c:forEach items="${errorMessages}" var="message">
						<li><c:out value="${message}" />
					</c:forEach>
				</ul>
			</div>
			<c:remove var="errorMessages" scope="session" />
		</c:if>

        <div class="form-data">
		<form action="signup" method="post"><br />
		    <p>ログインID(半角英数字で6～20文字以内)</p>
			<p class="login_id"><input name="login_id"	value="${login_id}" id="login_id" /></p>
			<p>パスワード(半角文字で6～20文字以内)</p>
			<p class="password1"><input name="password1" type="password" id="password1" /></p>
			<p>確認用パスワード(半角文字で6～20文字以内)</p>
			<p class="password2"><input name="password2" type="password" id="password2" /></p>
			<p>名前(1～10文字以内)</p>
			<p class="name"><input name="name" value="${name}" id="name" /></p>

			<p>支店名</p>
			<p class="branch_id">
			<select name="branch_id" id="branch_id">
				<c:forEach items="${branches}" var="branch">
					<c:if test="${branch_id == branch.branchId}">
						<option value="${branch.branchId}" selected>${branch.branchName}</option>
					</c:if>
					<c:if test="${branch_id != branch.branchId}">
						<option value="${branch.branchId}">${branch.branchName}</option>
					</c:if>
				</c:forEach>
			</select></p>
			<p>部署・役職</p>
			<p class="department_id">
			<select name="department_id" id="department_id">
				<c:forEach items="${departments}" var="department">
					<c:if test="${department_id == department.departmentId}">
						<option value="${department.departmentId}" selected>${department.departmentName}</option>
					</c:if>
					<c:if test="${department_id != department.departmentId}">
						<option value="${department.departmentId}">${department.departmentName}</option>
					</c:if>
				</c:forEach>
			</select></p><br />
		   <div class="submit"><input type="submit" value="登録" /></div>
		</form>
		</div>

	</div>
</body>
</html>