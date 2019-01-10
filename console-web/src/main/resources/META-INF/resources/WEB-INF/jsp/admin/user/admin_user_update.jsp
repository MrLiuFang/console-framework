<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<span id="page-title" style="display: none">修改</span>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<div class="layui-card-header">修改</div>
			<div class="layui-card-body">
				<form class="layui-form " action="${ctx}/admin/user/update"
					id="form-addOrUpdate" returnType="json">
					<input name="id" id="id" value="${adminUser.id}" type="hidden" />
					<div class="layui-form-item ">
						<label class="layui-form-label">账号<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="account" id="account" maxlength="50"
								lay-verify="account" autocomplete="off" placeholder="账号"
								class="layui-input qc-cursor" value="${adminUser.account }" disabled="disabled"/>
						</div>
						<div class="layui-form-mid layui-word-aux">用于登录，请保持唯一。</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">姓名<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" lay-verify="required"
								maxlength="50" autocomplete="off" placeholder="姓名"
								class="layui-input" value="${adminUser.name }" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">邮箱</label>
						<div class="layui-input-inline">
							<input type="text" name="email" id="email" lay-verify="myEmail"
								maxlength="50" autocomplete="off" placeholder="邮箱"
								class="layui-input" value="${adminUser.email }" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">昵称</label>
						<div class="layui-input-inline">
							<input type="text" name="nickName" id="nickName" maxlength="50"
								autocomplete="off" placeholder="昵称" class="layui-input"
								value="${adminUser.nickName }" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">电话号码</label>
						<div class="layui-input-inline">
							<input type="text" name="mobile" id="mobile" autocomplete="off"
								maxlength="50" placeholder="电话号码" class="layui-input"
								value="${adminUser.mobile }" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">性别<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select noDefault="true" documentId="gender"
								enumClass="com.qicloud.common.emuns.model.Gender"
								documentName="gender" selectedValue="${adminUser.gender}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">状态<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select noDefault="true" documentId="status"
								enumClass="com.qicloud.common.emuns.model.Status"
								documentName="status" selectedValue="${adminUser.status}" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">角色<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<select name="roleId" lay-filter="roleId" lay-verify="required">
								<option value=""></option>
								<c:forEach var="item" items="${roles}">
									<option value="${item.id }" ${item.selected }>${item.name }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="layui-inline " style="text-align: center;">
						<label class="layui-form-label"></label>
						<pepper:auth code="SYSTEM_SETTINGS_ADMIN_USER_UPDATE">
							<button class="layui-btn" lay-submit id="button_addOrUpdate"
								type="submit">保存</button>
						</pepper:auth>
						<button class="layui-btn layui-btn-normal" id="button_return"
							type="button">返回</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {

		//返回
		$("#button_return").click(function() {
			page.loadPage("${ctx}/admin/user/index");
		});

		//action=view时只读
		if ("view" == "${action}") {
			$("#button_addOrUpdate").remove();
			$("input, select, textarea").attr("disabled", "disabled");
			$("input, select, textarea").addClass("qc-cursor");
			layui.form.render();
		}

		//校验
		layui.form.verify({
			account : function(value, item) {
				value = $.trim(value);
				if ("${adminUser.account }" === value) {
					return false;
				}
				if (!value) {
					return '必填项不能为空';
				}
				var exist = false;
				baseUtil.AJpost("${ctx}/admin/user/checkExist", {
					account : value
				}, function(d) {
					exist = d.data.exist;
				});
				if (exist) {
					return '账号已存在';
				}
			},
			myEmail : function(value, item) {
				value = $.trim(value);
				if (value && !new RegExp("^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$").test(value)) {
					return '邮箱格式不正确';
				}
			}
		});
	});
</script>