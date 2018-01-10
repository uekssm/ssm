<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>全部角色</title>
<!--  
	SpringMVC 处理静态资源:
	1. 为什么会有这样的问题:
	优雅的 REST 风格的资源URL 不希望带 .html 或 .do 等后缀
	若将 DispatcherServlet 请求映射配置为 /, 
	则 Spring MVC 将捕获 WEB 容器的所有请求, 包括静态资源的请求, SpringMVC 会将他们当成一个普通请求处理, 
	因找不到对应处理器将导致错误。
	2. 解决: 在 SpringMVC 的配置文件中配置 <mvc:default-servlet-handler/>
-->
<script type="text/javascript" src="../scripts/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
	$(function(){
		$(".delete").click(function(){
			var href = $(this).attr("href");
			$("form").attr("action", href).submit();			
			return false;
		});
	});
</script>
</head>
<body>

	<form action="" method="post">
		<input type="hidden" name="_method" value="DELETE" />
	</form>

	<c:choose>
		<c:when test="${empty requestScope.ALLROLE}">
			没有角色信息
		</c:when>
		<c:otherwise>
			<table border="1">
				<tr>
					<th>编号</th>
					<th>角色名称</th>
					<th>描述</th>
					<th>操作</th>
				</tr>
				<c:forEach items="${requestScope.ALLROLE }" var="RoleInfo">
					<tr>
						<td>&nbsp;${RoleInfo.id }&nbsp;</td>
						<td>&nbsp;${RoleInfo.name }&nbsp;</td>
						<td>${RoleInfo.about }</td>
						<td><a class="delete"
							href="${pageContext.request.contextPath }/role/delete/${RoleInfo.id}">删除</a>&nbsp;&nbsp;&nbsp;&nbsp;<a
							href="${pageContext.request.contextPath }/role/detail/${RoleInfo.id}">更新</a></td>
					</tr>
				</c:forEach>
			</table>
		</c:otherwise>
	</c:choose>
</body>
</html>