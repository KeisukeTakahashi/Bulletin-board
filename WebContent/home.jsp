<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>ホーム</title>
	<link href="./css/home.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="main-contents">

  <div class="header">
     <c:choose>
        <c:when test="${ loginUser.departmentId == 1 }">
		<a href="post">新規投稿</a>
		<a href="control">管理画面</a>
		<a href="logout">ログアウト</a>
        </c:when>
		<c:otherwise>
		<a href="post">新規投稿</a>
		<a href="logout">ログアウト</a>
		</c:otherwise>
	 </c:choose>
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

  <div class="user_info">
  <p>ログインユーザー情報</p>
  <p>名前&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：<c:out value="${loginUser.name}"></c:out></p>

  <c:forEach items="${branches}" var="branch">
  <c:if test="${loginUser.branchId == branch.branchId}">
  <input type="hidden" value="${loginUser.branchId}" name="branch_id"/>
  <p>支店&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：<c:out value="${branch.branchName}" /></p>
  </c:if>
  </c:forEach>
  <c:forEach items="${departments}" var="department">
  <c:if test="${loginUser.departmentId == department.departmentId}">
  <input type="hidden" value="${loginUser.departmentId}" name="department_id"/>
  <p>部署・役職：<c:out value="${department.departmentName}" /></p>
  </c:if>
  </c:forEach>
  </div><br />



      <div class="search_area">
 		 <form action="./" method="get">
 		 <p>カテゴリー(単語を空白で区切ると複数検索出来ます)</p>
  		 <p><input type="text" name="lists.category" value="${category}" list="categorys" autocomplete="off" id="lists.category"><p>
  			 <datalist id="categorys">
				<c:forEach items="${categorys}" var="lists">
						<option value="${lists.category}" selected>${lists.category}</option>
				</c:forEach>
			</datalist><br>
	     	<p>カレンダー</p>
	     	<p><input type="date" name="start_day" value="${calendar_start}" id="start_day"/> ～
	     	<input type="date" name="end_day" value="${calendar_end}" id="end_day"/></p><br>
		 	<p class="submit"><input type="submit" value="検索"></p>
		 	</form><br>
	     	<form action="./" method="post">
		 	<p class="submit"><input type="submit" value="リセット"></p>
		 </form>
		</div>
		<br /><br /><br />

<div class="messages">
	<c:forEach items="${messages}" var="message">
			<div class="message">
				    <p class="subject">[件名]<c:out value="${message.subject}" /></p>
				    <p class="category">カテゴリー：<c:out value="${message.category}" /></p>
					<p class="name">投稿者&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：<c:out value="${message.name}" /></p>
					<p class="date">投稿日時&nbsp;&nbsp;&nbsp;：<fmt:formatDate value="${message.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" /></p>
				<p><c:forEach var="messageTxet" items="${ fn:split(message.text,'
				') }" >
				<c:out value="${messageTxet}" /><br>
				</c:forEach></p>
                  <c:if test="${ loginUser.id == message.userId && loginUser.departmentId != 2 && loginUser.departmentId != 3 }">
					<form action="DeleteMessage" method="post">
					<input type="hidden" value="${message.id}" name="id"/>
					<p class="del"><input type="submit" value="削除" id="submit_button1" onClick="return confirm('このメッセージを削除しますか？')"/></p>
					</form>
				  </c:if>
				  <c:if test="${ loginUser.departmentId == 2 || loginUser.departmentId == 3 && message.branchId == loginUser.branchId }">
					<form action="DeleteMessage" method="post">
					<input type="hidden" value="${message.id}" name="id"/>
					<p class="del"><input type="submit" value="削除" id="submit_button1" onClick="return confirm('このメッセージを削除しますか？')"/></p>
					</form>
				  </c:if>
			</div>

      <div class="comments">
		<c:forEach items="${comments}" var="comment" varStatus="status">
            <c:if test="${ message.id == comment.postId}">
            <div class="comment">
				<p class="comment_name">投稿者&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;：<c:out value="${comment.name}" /></p>
				<p class="comment_date">投稿日時&nbsp;&nbsp;&nbsp;：<fmt:formatDate value="${comment.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" /></p>
				<p><c:forEach var="commentTxet" items="${ fn:split(comment.text,'
				') }" >
				<c:out value="${commentTxet}" /><br>
				</c:forEach></p>

				<c:if test="${ loginUser.id == comment.userId && loginUser.departmentId != 2 && loginUser.departmentId != 3 }">
				<form action="DeleteComment" method="post">
				<input type="hidden" value="${comment.id}" name="id"/>
				<p class="del"><input type="submit" value="削除" id="submit_button1" onClick="return confirm('このコメントを削除しますか？')"/></p>
				</form>
				</c:if>
				<c:if test="${ loginUser.departmentId == 2 || loginUser.departmentId == 3 && comment.branchId == loginUser.branchId }">
				<form action="DeleteComment" method="post">
				<input type="hidden" value="${comment.id}" name="id"/>
				<p class="del"><input type="submit" value="削除" id="submit_button1" onClick="return confirm('このコメントを削除しますか？')"/></p>
				</form>
				</c:if>
                </div>
                <br>
			</c:if>

		</c:forEach>
		</div>


	 <div class="form-area">
		<form action="newComment" method="post">
			コメント記入欄<br>
			<textarea name="comment" cols="80" rows="4" class="comment-box"></textarea>
			<br>
			<input type="hidden" value="${message.id}" name="id"/>
			<input type="submit" id="submit_button2" value="コメントを投稿">

		</form>
	 </div>
	 <br>
	 </c:forEach>
	 </div>

</div>
</body>
</html>