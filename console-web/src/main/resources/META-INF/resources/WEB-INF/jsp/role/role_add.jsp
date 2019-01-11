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

<span id="page-title" style="display: none">新增</span>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<div class="layui-card-header">新增</div>
			<div class="layui-card-body">
				<form class="layui-form " action="${ctx}/console/role/add"
					id="form-addOrUpdate" returnType="json">
					<div class="layui-form-item ">
						<label class="layui-form-label">角色名称<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" lay-verify="required"
								maxlength="100" autocomplete="off" placeholder="角色名称"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">编码<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="code" id="code" autocomplete="off"
								maxlength="100" lay-verify="required" placeholder="编码"
								class="layui-input">
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">作用域<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select required="true" documentId="scope"
								enumClass="com.qicloud.common.emuns.model.Scope"
								selectedValue="1" documentName="scope" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">状态<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select required="true" documentId="status"
								enumClass="com.qicloud.common.emuns.model.Status"
								selectedValue="0" documentName="status" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">权限</label>
						<div class="layui-input-inline">
							<button type="button" class="layui-btn layui-btn-primary"
								id="selectResource">选择权限</button>
						</div>
						<input type="hidden" name="resourceIds" id="resourceIds"></input>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">备注</label>
						<div class="layui-input-inline">
							<textarea placeholder="请输入内容" id="remarks" name="remarks"
								maxlength="250" class="layui-textarea"></textarea>
						</div>
					</div>
					<div class="layui-inline" style="text-align: center;">
						<label class="layui-form-label"></label>
						<pepper:auth code="SYSTEM_SETTINGS_ADMIN_ROLE_ADD">
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
	$(function() {
		$("#button_return").click(function() {
			page.loadPage("${ctx}/console/role/index")
		});

		$("#selectResource").click(function() {
			openResource();
		})

		//弹出权限树窗口
		function openResource() {
			if (!$("#scope").val()) {
				layui.layer.msg("请先选择作用域!");
				return false;
			}
			basePlugin.win("选择权限（点击行即可选中）", "resourceWin", "<table class='layui-hidden' id='treeTable' lay-filter='treeTable'></table>", function() {
				//checkbox保存
				var resIds = [];
				$("#resourceWin table [type=checkbox]:checked").each(function() {
					resIds.push($(this).attr("cbid"));
				});
				$("#resourceIds").val(resIds.join(";"));
			}, {
				area : [ '50%', '80%' ]
			});

			//权限选择树加载完成后操作。
			function done() {
				$("#resourceWin table").click(function(event, a, b) {
					var target = event.target;
					if (!$(target).is(".layui-form-checkbox i") && !$(target).is(".layui-icon.layui-tree-head")) {
						var cb = $(target).closest("tr").find("[type=checkbox]");
						if (cb.length == 1) {
							if (cb[0].checked) {
								cb[0].checked = false;
							} else {
								cb[0].checked = true;
							}
							if (b == "check") {
								cb[0].checked = true;
							}
							if (b == "nocheck") {
								cb[0].checked = false;
							}
							if (cb[0].checked) {
								$(cb[0]).next("div.layui-form-checkbox").addClass("layui-form-checked");
							} else {
								$(cb[0]).next("div.layui-form-checkbox").removeClass("layui-form-checked");
							}
						}
					}
					//如果选中：点击节点选中它的所有子孙节点，并且选中它的所有父节点。
					//如果不选中：点击节点不选中它的所有子孙节点，父节点保留状态。
					if (!$(target).is(".layui-icon.layui-tree-head")) {
						var cb = $(target).closest("tr").find("[type=checkbox]");
						if (cb[0].checked == true) {
							if (cb.attr("cb-isleaf") == "0") {
								var downCb = $("[cb-parent=" + cb.attr("cbid") + "]").not(":checked").closest("tr");
								if (a != "up") {
									downCb.trigger("click", [ "down", "check" ]);
								}
							}
							if (a != "down") {
								$("[cbid=" + cb.attr("cb-parent") + "]").not(":checked").closest("tr").trigger("click", [ "up", "check" ]);
							}
						} else {
							if (cb.attr("cb-isleaf") == "0") {
								var downCb = $("[cb-parent=" + cb.attr("cbid") + "]:checked").closest("tr");
								if (a != "up") {
									downCb.trigger("click", [ "down", "nocheck" ]);
								}
							}
						}
					}
				});
				//异步回显
				setTimeout(function() {
					//checkbox回显
					var resIds = $("#resourceIds").val();
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
					url : '${ctx}/console/role/roleMenu?search_EQ_scope=' + $("#scope").val(),
					//树形id字段名称
					treeId : 'id',
					//树形父id字段名称
					treeUpId : 'parentId',
					//以树形式显示的字段
					treeShowName : 'name',
					cols : [ [ {
						field : 'id',
						templet : function(d) {
							return '<input cbid="'+d.id+'" cb-parent="'+d.parentId+'" cb-isleaf="'+d.isLeaf+'" type="checkbox" lay-skin="primary" />';
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