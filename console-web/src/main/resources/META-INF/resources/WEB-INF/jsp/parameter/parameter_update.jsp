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
				<form class="layui-form " action="${ctx}/console/parameter/update"
					id="form-addOrUpdate" returnType="json">
					<input name="id" id="id" value="${parameter.id}"
						style="display: none;">
					<div class="layui-form-item ">
						<label class="layui-form-label">编码<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="code" id="code" lay-verify="required"
								maxlength="100" disabled="disabled" autocomplete="off"
								placeholder="编码" class="layui-input qc-cursor"
								value="${parameter.code}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">值<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="value" id="value" lay-verify="required"
								maxlength="250" autocomplete="off" placeholder="值"
								class="layui-input" value="${parameter.value}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">备注</label>
						<div class="layui-input-inline">
							<textarea placeholder="请输入内容" id="remarks" name="remarks"
								maxlength="250" class="layui-textarea">${parameter.remarks}</textarea>
						</div>
					</div>
					<div class="layui-inline" style="text-align: center;">
						<label class="layui-form-label"></label>
						<pepper:auth code="SYSTEM_SETTINGS_PARAMETER_UPDATE">
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
			page.loadPage("${ctx}/console/parameter/index")
		});

		//action=view时只读
		if ("view" == "${action}") {
			$("#button_addOrUpdate").remove();
			$("input, select, textarea").attr("disabled", "disabled");
			$("input, select, textarea").addClass("qc-cursor");
			layui.form.render();
		}
	});
</script>