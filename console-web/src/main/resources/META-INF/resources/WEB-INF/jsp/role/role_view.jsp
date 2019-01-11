<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>

<style>
#resourceWin .layui-table-body {
	overflow: hidden;
}

.layui-layer-page .layui-layer-content {
	overflow: auto !important;
}
</style>

<span id="page-title" style="display: none">查看</span>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<div class="layui-card-header">查看</div>
			<div class="layui-card-body">
				<form class="layui-form " action="${ctx}/console/role/update"
					id="form-addOrUpdate" returnType="json">
					<input name="id" id="id" value="${role.id}" style="display: none;">
					<div class="layui-form-item ">
						<label class="layui-form-label">角色名称</label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" lay-verify="required"
								autocomplete="off" placeholder="角色名称" class="layui-input"
								value="${role.name}">
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">编码</label>
						<div class="layui-input-inline">
							<input type="text" name="code" id="code" autocomplete="off"
								lay-verify="required" placeholder="编码" class="layui-input"
								value="${role.code}">
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">作用域</label>
						<div class="layui-input-inline">
							<pepper:Enum2Select required="true" documentId="scope"
								enumClass="com.qicloud.common.emuns.model.Scope"
								documentName="scope"
								selectedValue="${empty role.scope?'1':role.scope}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">状态</label>
						<div class="layui-input-inline">
							<pepper:Enum2Select required="true" documentId="status"
								enumClass="com.qicloud.common.emuns.model.Status"
								documentName="status"
								selectedValue="${empty role.status?'0':role.status}" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">权限</label>
						<div class="layui-input-inline">
							<button type="button" class="layui-btn layui-btn-primary"
								id="selectResource">已选权限</button>
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">备注</label>
						<div class="layui-input-inline">
							<textarea placeholder="请输入内容" id="remarks" name="remarks"
								maxlength="250" class="layui-textarea">${role.remarks }</textarea>
						</div>
					</div>
					<div class="layui-inline" style="text-align: center;">
						<label class="layui-form-label"></label>
						<button class="layui-btn layui-btn-normal" id="button_return"
							type="button">返回</button>
					</div>
				</form>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(function() {
		$("#button_return").click(function() {
			page.loadPage("${ctx}/console/role/index")
		});

		$("#button_addOrUpdate").remove();
		$("input, select, textarea").attr("disabled", "disabled");
		$("input, select, textarea").addClass("qc-cursor");
		layui.form.render();

		$("#selectResource").click(function() {
			openResource();
		})

		//弹出权限树窗口
		function openResource() {
			if (!$("#scope").val()) {
				layui.layer.msg("请先选择作用域!");
				return false;
			}
			basePlugin.win("已选权限", "resourceWin", "<table class='layui-hidden' id='treeTable' lay-filter='treeTable'></table>", function() {
			}, {
				area : [ '50%', '80%' ],
				btn : [ "取消" ]
			});

			//权限选择树加载完成后操作。
			function done(res) {
				//异步回显
				setTimeout(function() {
					//checkbox回显
					var resIds = res.extData;
					if (resIds) {
						resIds = resIds.split(";");
						for ( var i in resIds) {
							$("[cbid=" + resIds[i] + "]")[0].checked = true;
							$("[cbid=" + resIds[i] + "]").next("div.layui-form-checkbox").addClass("layui-form-checked");
						}
					}
				}, 1);
			}
			//初始化弹出框权限选择树
			layui.use([ 'treeGrid' ], function() {
				var treeGrid = layui.treeGrid;
				var opt = {
					elem : '#treeTable',
					method : "post",
					url : '${ctx}/console/role/roleMenu?search_EQ_scope=' + $("#scope").val() + "&roleId=${role.id}",
					//树形id字段名称
					treeId : 'id',
					//树形父id字段名称
					treeUpId : 'parentId',
					//以树形式显示的字段
					treeShowName : 'name',
					cols : [ [ {
						field : 'id',
						templet : function(d) {
							return '<input cbid="'+d.id+'" type="checkbox" lay-skin="primary" disabled="disabled" />';
						},
						width : 50
					}, {
						field : 'name',
						title : '权限名称'
					} ] ],
					done : done,
					page : false
				};
				var treeTable = treeGrid.render(opt);
			});
		}
	});
</script>