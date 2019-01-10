<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="layui-side layui-side-menu">
	<div class="layui-side-scroll">
		<div class="layui-logo" lay-href="" onclick="page.breadClick(0)">
			<span>${title }</span>
		</div>
		<ul class="layui-nav layui-nav-tree">
			<c:forEach var="parentMenu" items="${listMenu}" varStatus="status1">
				<li
					class="layui-nav-item <c:if test="${status1.index==0 }">layui-nav-itemed</c:if> ">
					<a class="menu-l1" href="javascript:;"
					lay-tips="${parentMenu.name}">${parentMenu.name}<span
						class="layui-nav-more"></span></a>
					<dl class="layui-nav-child">
						<c:forEach var="childMenu" items="${parentMenu.child}"
							varStatus="status2">
							<dd>
								<a menu-index="${status1.index }_${status2.index }"
									url="${childMenu.url}" href="javascript:;">${childMenu.name}</a>
							</dd>
						</c:forEach>
					</dl>
				</li>
			</c:forEach>
		</ul>
	</div>
</div>
