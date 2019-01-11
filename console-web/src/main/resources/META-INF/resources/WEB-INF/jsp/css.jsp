<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="${ctx}/assets/layui/css/layui.css">
<link rel="stylesheet" href="${ctx}/assets/layui-pro/console.css">
<link rel="stylesheet" href="${ctx}/assets/layui/css/autocomplete.css">
<link rel="stylesheet" href="${ctx}/assets/iconfont/iconfont.css">
<c:forEach items="${cssLoad}" var="cssUrl">
	<link href="${cssUrl}" rel="stylesheet">
</c:forEach>


