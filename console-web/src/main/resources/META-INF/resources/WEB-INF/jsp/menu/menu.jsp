<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>

<style>
.layui-table-body {
	overflow-x: hidden;
}
</style>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<form class="layui-form layui-form-pane qc-search-one-line" action=""
				id="form-query">
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">作用域</label>
						<div class="layui-input-inline">
							<pepper:Enum2Select required="true" documentId="scope"
								enumClass="com.pepper.common.emuns.Scope"
								documentName="scope" selectedValue="1" noDefault="true" />
						</div>
					</div>
					
					<div class="layui-inline qc-list-bar">
						<button class="layui-btn" id="button_query" type="button"
							data-type="reload">查询</button>
						<button class="layui-btn layui-btn-normal" id="button_add"
							type="button">新增</button>
					</div>
				</div>
			</form>
		</div>
		<div class="layui-card">
			<table class="layui-hidden" id="treeTable" lay-filter="treeTable"></table>
		</div>

	</div>
</div>

<script type="text/html" id="barDemo">
	<pepper:auth code="SYSTEM_SETTINGS_MENU_TO_EDIT">
		<a class="layui-btn layui-btn-xs" lay-event="update"> 编辑</a>
	</pepper:auth>
	<pepper:auth code="SYSTEM_SETTINGS_MENU_DELETE">
		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"> 删除</a>
	</pepper:auth>
</script>

<script type="text/javascript">
	$(function($) {

		var $ = layui.$;
		layui.use('treeGrid', function() {
			var treeGrid = layui.treeGrid; //很重要
			var treeTable = treeGrid.render({
				elem : '#treeTable',
				method : "post",
				height : 710,
				url : '${ctx}/console/menu/list',
				treeId : 'id'//树形id字段名称
				,
				treeUpId : 'parentId'//树形父id字段名称
				,
				treeShowName : 'name'//以树形式显示的字段
				,
				cols : [ [ {
					field : 'name',
					title : '菜单名称',
					width : 300
				}, {
					field : 'code',
					title : '编码',
					width : 350
				}, {
					field : 'url',
					title : 'url'
				}, {
					field : 'sort',
					width : 60,
					title : '排序'
				}, {
					field : 'status',
					width : 60,
					title : '状态'
				}, {
					width : 150,
					toolbar : '#barDemo',
					title : '操作'
				} ] ],
				page : false,
				done : function() {
					//新增和修改的时候记录滚动条位置，操作回来后还是之前的位置，方便对比
					var scroll = $.cookie("menu-scroll");
					if (scroll) {
						$(".layui-table-body").scrollTop(scroll * 1);
						$.cookie("menu-scroll", "");
					}
				},
				where : {
					"search_EQUAL_scope" : $('#scope').val()
				}
			});
			
			

			//点击“搜索”
			$('#button_query').on('click', function() {
				treeGrid.reload('treeTable', {
					where : {
						"search_EQUAL_scope" : $('#scope').val()
					}
				});
			});

			$("#button_add").click(function() {
				$.cookie("menu-scroll", $(".layui-table-body").scrollTop());
				layui.layer.msg('请选择要新增的选项！', {
					time : 10000, //20s后自动关闭
					btn : [ '菜单', '资源', '取消' ],
					yes : function(index, layero) {
						page.loadPage("${ctx}/console/menu/toAdd?type=0");
						layui.layer.close(index);
					},
					btn2 : function(index, layero) {
						page.loadPage("${ctx}/console/menu/toAdd?type=1");
						layui.layer.close(index);
					},
					btn3 : function(index, layero) {
						layui.layer.close(index);
					}
				});
			});
			//监听工具条
			treeGrid.on('tool(treeTable)', function(obj) { //注：tool是工具条事件名，mainTable是table原始容器的属性 lay-filter="对应的值"
				var data = obj.data; //获得当前行数据
				var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
				var tr = obj.tr; //获得当前行 tr 的DOM对象
				if (layEvent === 'del') { //删除
					layui.layer.confirm('确定删除该行？', function(index) {
						obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						$.cookie("menu-scroll", $(".layui-table-body").scrollTop());
						layui.layer.close(index);
						//向服务端发送删除指令
						common.deleteData('${ctx}/console/menu/delete', data["id"]);
						treeGrid.reload('treeTable');
					});
				} else if (layEvent === 'update') { //修改
					$.cookie("menu-scroll", $(".layui-table-body").scrollTop());
					page.loadPage("${ctx}/console/menu/toEdit?id=" + data["id"]);
				}
			});
		})
	});
</script>