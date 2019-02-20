<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<span id="page-title" style="display: none" class="qc-bread-only">我的资料</span>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<div class="layui-card-header">设置我的资料</div>
			<div class="layui-card-body">
				<form class="layui-form " action="${ctx}/console/user/updateUserInfo"
					id="form-addOrUpdate" returnType="json">
					<input name="id" id="id" value="${adminUser.id}"
						style="display: none;">
					<div class="layui-form-item ">
						<label class="layui-form-label">头像</label>
						<button type="button" class="layui-btn layui-btn-primary"
							id="LAY_avatarUpload">
							<i class="layui-icon"></i>上传图片
						</button>
					</div>
					<div class="layui-form-item">
						<div class="layui-upload-list" id="headImage"></div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">帐号<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="account" id="account" autocomplete="off"
								placeholder="帐号" class="layui-input" disabled="disabled"
								style="cursor: not-allowed" value="${adminUser.account}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">姓名<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" lay-verify="required"
								autocomplete="off" placeholder="姓名" class="layui-input"
								value="${adminUser.name}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">邮箱</label>
						<div class="layui-input-inline">
							<input type="text" name="email" id="email" autocomplete="off"
								lay-verify="myEmail" placeholder="邮箱" class="layui-input"
								value="${adminUser.email}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">昵称</label>
						<div class="layui-input-inline">
							<input type="text" name="nickName" id="nickName"
								autocomplete="off" placeholder="昵称" class="layui-input"
								value="${adminUser.nickName}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">电话号码</label>
						<div class="layui-input-inline">
							<input type="text" name="mobile" id="mobile" autocomplete="off"
								placeholder="电话号码" class="layui-input"
								value="${adminUser.mobile}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">性别<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select required="true" documentId="gender"
								enumClass="com.pepper.common.emuns.Gender"
								documentName="gender"
								selectedValue="${empty adminUser.gender?'FEMALE':adminUser.gender}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">状态<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select required="true" documentId="status"
								enumClass="com.pepper.common.emuns.Status" disabled="true"
								documentName="status"
								selectedValue="${empty adminUser.status?'NORMAL':adminUser.status}" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">角色<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<select name="roleId" lay-filter="roleId" disabled="disabled">
								<option value=""></option>
								<c:forEach var="item" items="${roles}">
									<option value="${item.id }" ${item.selected }>${item.name }</option>
								</c:forEach>
							</select>
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

<script type="text/javascript">
	$(document).ready(function() {
		var $ = layui.jquery;

		//头像上传
		uploadUtil.initImg('LAY_avatarUpload', 'headImage', 'photo', '${photos}', null, null, {
			size : 500
		});

		//校验
		layui.form.verify({
			myEmail : function(value, item) {
				value = $.trim(value);
				if (value && !new RegExp("^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$").test(value)) {
					return '邮箱格式不正确';
				}
			}
		});

	});
</script>