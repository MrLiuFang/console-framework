<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<span id="page-title" style="display: none">新增</span>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<div class="layui-card-header">新增</div>
			<div class="layui-card-body">
				<form class="layui-form " action="${ctx}/console/user/add"
					id="form-addOrUpdate" returnType="json">
					<div class="layui-form-item ">
						<label class="layui-form-label">账号<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="account" id="account" maxlength="50"
								lay-verify="account" autocomplete="off" placeholder="账号"
								class="layui-input" />
						</div>
						<div class="layui-form-mid layui-word-aux">用于登录，请保持唯一。</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">姓名<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" lay-verify="required"
								maxlength="50" autocomplete="off" placeholder="姓名"
								class="layui-input" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">邮箱</label>
						<div class="layui-input-inline">
							<input type="text" name="email" id="email" lay-verify="myEmail"
								maxlength="50" autocomplete="off" placeholder="邮箱"
								class="layui-input" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">昵称</label>
						<div class="layui-input-inline">
							<input type="text" name="nickName" id="nickName" maxlength="50"
								autocomplete="off" placeholder="昵称" class="layui-input" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">电话号码</label>
						<div class="layui-input-inline">
							<input type="text" name="mobile" id="mobile" maxlength="50"
								autocomplete="off" placeholder="电话号码" class="layui-input" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">性别<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select noDefault="true" documentId="gender"
								enumClass="com.pepper.common.emuns.Gender"
								documentName="gender" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">状态<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select noDefault="true" documentId="status"
								enumClass="com.pepper.common.emuns.Status"
								documentName="status" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">角色<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<select name="roleId" lay-filter="roleId" lay-verify="required">
								<option value=""></option>
								<c:forEach var="item" items="${roles}">
									<option value="${item.id }">${item.name }</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="layui-inline " style="text-align: center;">
						<label class="layui-form-label"></label>
						<pepper:auth code="SYSTEM_SETTINGS_ADMIN_USER_ADD">
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
			page.loadPage("${ctx}/console/user/index");
		});

		//校验
		layui.form.verify({
			account : function(value, item) {
				value = $.trim(value);
				if (!value) {
					return '必填项不能为空';
				}
				var exist = false;
				baseUtil.AJpost("${ctx}/console/user/checkExist", {
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