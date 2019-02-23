<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="layadmin-tabsbody-item layui-show">
	<div class="layui-fluid">
		<div class="layadmin-tips">
			<i class="layui-icon" face=""></i>

			<div class="layui-text" style="font-size: 20px;">
				<c:if test="${not empty message}">${message}</c:if>
				<c:if test="${empty message}">好像出错了呢！</c:if>
			</div>
		</div>
	</div>
</div>