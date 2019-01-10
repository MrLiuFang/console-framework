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
<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<form class="layui-form layui-form-pane" action="" id="form-query">
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">角色名称</label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" placeholder="角色名称"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">角色编码</label>
						<div class="layui-input-inline">
							<input type="text" name="code" id="code" placeholder="角色编码"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">作用域</label>
						<div class="layui-input-inline" style="width: 190px;">
							<pepper:Enum2Select documentId="scope"
								enumClass="com.qicloud.common.emuns.model.Scope"
								documentName="scope" />
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">状态</label>
						<div class="layui-input-inline" style="width: 190px;">
							<pepper:Enum2Select documentId="status"
								enumClass="com.qicloud.common.emuns.model.Status"
								documentName="status" />
						</div>
					</div>
				</div>
				<div class="layui-form-item qc-list-bar">
					<div class="layui-inline">
						<button class="layui-btn" id="button_query" type="button">查询</button>
						<pepper:auth code="SYSTEM_SETTINGS_ADMIN_ROLE_ADD">
							<button class="layui-btn layui-btn-normal" id="button_add"
								type="button">新增</button>
						</pepper:auth>
					</div>
				</div>
			</form>
		</div>

		<div class="layui-card layui-form">
			<table class="layui-hide" id="mainTable" lay-filter="mainTable"></table>
		</div>

	</div>
</div>

<script type="text/html" id="barDemo">
	<pepper:auth code="SYSTEM_SETTINGS_ADMIN_ROLE_UPDATE">
		<a class="layui-btn layui-btn-xs" lay-event="opt-resource">权限</a>
	</pepper:auth>
	<pepper:auth code="SYSTEM_SETTINGS_ADMIN_ROLE_TOVIEW">
		<a class="layui-btn layui-btn-xs" lay-event="view">查看</a>
	</pepper:auth>
	<pepper:auth code="SYSTEM_SETTINGS_ADMIN_ROLE_TOEDIT">
		<a class="layui-btn layui-btn-xs" lay-event="update">编辑</a>
	</pepper:auth>
</script>

<script type="text/javascript">
	$(function() {
		var $ = layui.$;
		var table = layui.table;
		//表格渲染
		table.render({
			elem : '#mainTable',
			url : '${ctx}/admin/role/list',
			method : "post",
			cols : [ [ {
				field : 'name',
				title : '角色',
				width : 250
			}, {
				field : 'code',
				title : '编码',
				width : 250
			}, {
				field : 'scopeLabel',
				title : '角色作用域',
				width : 150
			}, {
				field : 'statusLabel',
				title : '状态',
				width : 100
			}, {
				field : 'remarks',
				title : '描述'
			}, {
				fixed : 'right',
				width : 200,
				toolbar : '#barDemo',
				title : '操作'
			} ] ],
			request : {
				pageName : 'pageNo',
				limitName : 'pageSize'
			},
			response : {
				statusName : 'status',
				statusCode : 200,
				msgName : 'message',
				countName : 'totalRow',
				dataName : 'results'
			},
			page : {
				layout : [ 'count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip' ]
			}
		});

		//点击“搜索”
		$('#button_query').on('click', function() {
			table.reload('mainTable', {
				page : {
					curr : 1
				},
				where : {
					"search_LIKE_name" : $('#name').val(),
					"search_LIKE_code" : $('#code').val(),
					"search_EQ_scope" : $('#scope').val(),
					"search_EQ_status" : $('#status').val()
				}
			});
		});

		//监听工具条
		table.on('tool(mainTable)', function(obj) { //注：tool是工具条事件名，mainTable是table原始容器的属性 lay-filter="对应的值"
			var data = obj.data; //获得当前行数据
			var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			var tr = obj.tr; //获得当前行 tr 的DOM对象
			if (layEvent === 'view') { //查看
				page.loadPage("${ctx}/admin/role/toView?id=" + data["id"])
			} else if (layEvent === 'del') { //删除
				layui.layer.confirm('确定删除该行？', function(index) {
					obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
					layui.layer.close(index);
					//向服务端发送删除指令
					common.deleteData('${ctx}/admin/role/delete', data["id"]);
					table.reload('mainTable');
				});
			} else if (layEvent === 'update') { //修改
				page.loadPage("${ctx}/admin/role/toEdit?id=" + data["id"]);
			} else if (layEvent === 'opt-resource') { //修改角色权限
				openResource(data.scope, data.id);
			}
		});

		//新增跳转
		$("#button_add").click(function() {
			page.loadPage("${ctx}/admin/role/toAdd");
		})

		//弹出权限树窗口
		function openResource(scope, roleId) {
			basePlugin.win("选择权限（点击行即可选中）", "resourceWin", "<table class='layui-hidden' id='treeTable' lay-filter='treeTable'></table>", function() {
				//checkbox保存
				var resIds = [];
				$("#resourceWin table [type=checkbox]:checked").each(function() {
					resIds.push($(this).attr("cbid"));
				});
				baseUtil.AJpost('${ctx}/admin/role/saveRoleMenu', {
					roleId : roleId,
					resourceIds : resIds.join(";")
				})
			}, {
				area : [ '50%', '80%' ]
			});

			//权限选择树加载完成后操作。
			function done(res) {
				$("#resourceWin table").bind("click", function(event, a, b) {
					//点击行即选中
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
					url : '${ctx}/admin/role/roleMenu?search_EQ_scope=' + scope + "&roleId=" + roleId,
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