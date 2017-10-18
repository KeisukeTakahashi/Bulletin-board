<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>管理画面</title>
	<link href="./css/control.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main-contents">
		<a href="./">ホーム</a>
		<a href="signup">ユーザー新規登録</a>
</div>
<br />

<c:if test="${ not empty errorMessages }">
	<div class="errorMessages">
		<ul>
			<c:forEach items="${errorMessages}" var="message">
				<li><c:out value="${message}" />
			</c:forEach>
		</ul>
	</div>
	<c:remove var="errorMessages" scope="session"/>
</c:if><br />

<div class="lists">
<table border="1">
	<tr>
	<th>名前</th>
	<th>支店名</th>
	<th>部署・役職</th>
	<th>状態</th>
	<th>編集</th>
	</tr>

	  <tbody>
		<c:forEach items="${users}" var="user">
		  <input type="hidden" value="${user.branchId}" name="branch_id"/>
		  <input type="hidden" value="${user.departmentId}" name="department_id"/>
			<tr>
				<td><span class="name"><c:out value="${user.name}" /></span></td>
				<td><span class="branch_name"><c:out value="${user.branchName}" /></span></td>
				<td><span class="department_name"><c:out value="${user.departmentName}" /></span></td>

				<td>
				 <c:choose>
				  <c:when test="${ user.id != loginUser.id }">
				 	<form action="control" method="post">
						<c:choose>
							<c:when test="${ user.isWorking == 1 }">
							<input type="hidden" value="${user.id}" name="id"/>
							<input type="hidden" value="0" name="is_working"/>
							<input type="submit" value="停止" id="submit_button1" onClick="return confirm('このユーザーを停止しますか？')"/>
							</c:when>
							<c:otherwise>
							<input type="hidden" value="${user.id}" name="id"/>
							<input type="hidden" value="1" name="is_working"/>
							<input type="submit" value="復活" id="submit_button2" onClick="return confirm('このユーザーを復活しますか？')"/>
							</c:otherwise>
						</c:choose>
					</form>
				  </c:when>
				  <c:otherwise>
				  ログイン中
				  </c:otherwise>
				 </c:choose>
				</td>
				 <td>
					 <form action="settings" method="get">
	                   <input type="hidden" value="${user.id}" name="id"/>
					  <input type="submit" value="編集" id="submit_button3"/>
					</form>
				</td>
			</tr>
	    </c:forEach>
	 </tbody>
</table>
</div>

</body>
</html>