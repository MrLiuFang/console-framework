<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="layui-header">
	<ul class="layui-nav layui-layout-left">
		<li class="layui-nav-item layadmin-flexible" lay-unselect><a
			href="javascript:;" title="侧边伸缩"> <i id="flexible-menu"
				class="layui-icon layui-icon-shrink-right"></i> <span class="split"></span>
				<i id='qc-menu-expand' title="展开菜单" class="iconfont icon-zhankai"></i><span
				class="split"></span><i title="收起菜单"
				class="iconfont icon-yijianshouqi" id='qc-menu-pack-up'></i>
		</a></li>
		<li class="layui-nav-item" lay-unselect=""><a href="javascript:;"
			title="刷新"> <i id='qc-refresh-page'
				class="layui-icon layui-icon-refresh-3"></i>
		</a></li>
		
	</ul>
	<ul class="layui-nav layui-layout-right" style="margin-right: 20px;">
		<li class="layui-nav-item" lay-unselect=""><a href="javascript:;">
				<c:choose>
					
					<c:when test="${adminUser.headPortrait == null || adminUser.headPortrait == ''}">
						<%-- <img src="${ctx }/assets/img/logo.png" class="layui-nav-img" /> --%>
					</c:when>
					<c:otherwise>
						<img src="${adminUser.headPortrait}" class="layui-nav-img" />
					</c:otherwise>
				</c:choose>  ${adminUser.name}
		</a>
			<dl class="layui-nav-child ">
				<dd>
					<a style="cursor: pointer;"
						onclick="page.loadPage('${ctx }/console/user/userInfo?id=${adminUser.id }')">我的资料</a>
				</dd>
				<dd>
					<a style="cursor: pointer;"
						onclick="page.loadPage('${ctx }/console/user/toRePwd?id=${adminUser.id }')">密码设置</a>
				</dd>
				<dd>
					<a href="${ctx}/console/loginOut">退出</a>
				</dd>
			</dl></li>
	</ul>
</div>
<div class="layui-card layadmin-header layui-show">
	<span class="layui-breadcrumb"> </span>
</div>


