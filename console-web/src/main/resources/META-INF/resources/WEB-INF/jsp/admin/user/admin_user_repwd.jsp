<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<span id="page-title" style="display: none" class="qc-bread-only">密码设置</span>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<div class="layui-card-header">修改密码</div>
			<div>
				<div class="layui-card-body">
					<form class="layui-form " action="${ctx}/admin/user/changePwd"
						id="form-addOrUpdate" returnType="json">
						<input type="hidden" name="userId" id="userId"
							value="${adminUser.id}" />
						<div class="layui-form-item ">
							<label class="layui-form-label">原密码<span class="qi-req">*</span></label>
							<div class="layui-input-inline">
								<input type="password" name="oldPwd" id="oldPwd"
									lay-verify="required" autocomplete="off" placeholder="原密码"
									class="layui-input" />
							</div>
						</div>
						<div class="layui-form-item ">
							<label class="layui-form-label">新密码<span class="qi-req">*</span></label>
							<div class="layui-input-inline">
								<input type="password" name="newPwd" id="newPwd"
									lay-verify="required" autocomplete="off" placeholder="新密码"
									class="layui-input" />
							</div>
						</div>
						<div class="layui-inline" style="text-align: center;">
							<label class="layui-form-label"></label>
							<button class="layui-btn" lay-submit id="button_addOrUpdate"
								type="submit">保存</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>