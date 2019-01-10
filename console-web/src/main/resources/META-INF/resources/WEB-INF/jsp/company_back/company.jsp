<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<form class="layui-form layui-form-pane" action="" id="form-query">
				<div class="layui-form-item">
					<div class="layui-inline ">
						<label class="layui-form-label">公司名称</label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" placeholder="公司名称"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline ">
						<label class="layui-form-label">公司编码</label>
						<div class="layui-input-inline">
							<input type="text" name="code" id="code" placeholder="公司编码"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline ">
						<label class="layui-form-label">联系人</label>
						<div class="layui-input-inline">
							<input type="text" name="contact" id="contact" placeholder="联系人"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline ">
						<label class="layui-form-label">联系人电话</label>
						<div class="layui-input-inline" style="width: 190px;">
							<input type="text" name="telephone" id="telephone" placeholder="联系人电话"
								class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item qc-list-bar">
					<div class="layui-inline">
						<button class="layui-btn" id="button_query" type="button"
							data-type="reload">查询</button>
						<pepper:auth code="BASIC_INFORMATION_COMPANY_ADD">
							<button class="layui-btn layui-btn-normal" id="button_add"
								type="button">新增</button>
						</pepper:auth>

					</div>
				</div>
			</form>
		</div>

		<div class="layui-card layui-form">
			<table class="layui-hide" id="treeTable" lay-filter="treeTable"></table>
		</div>
	</div>
</div>
<script type="text/html" id="barDemo">
	<pepper:auth code="BASIC_INFORMATION_COMPANY_TO_EDIT">
		<a class="layui-btn layui-btn-xs" lay-event="update"> 编辑</a>
	</pepper:auth>
	<!-- <pepper:auth code="BASIC_INFORMATION_COMPANY_DELETE">
		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"> 删除</a>
	</pepper:auth>-->
</script>

<script type="text/javascript">
	$(function($) {

		var $ = layui.$;
		layui.use('treeGrid', function() {
			var treeGrid = layui.treeGrid; //很重要
			var treeTable = treeGrid.render({
				elem : '#treeTable',
				method : "post",
				height : 600,
				url : '${ctx}/admin/company/list',
				treeId : 'id'//树形id字段名称
				,
				treeUpId : 'parentId'//树形父id字段名称
				,
				treeShowName : 'name'//以树形式显示的字段
				,
				cols : [ [ {
					field : 'name',
					title : '公司名称'
				}, {
					field : 'code',
					title : '编码'
				}, {
					field : 'contact',
					title : '联系人'
				}, {
					field : 'telephone',
					title : '联系人电话'
				}, {
					field : 'address',
					title : '地址'
				}, {
					field : 'zipCode',
					title : '邮编'
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
					"search_LIKE_name" : $('#name').val(),
					"search_LIKE_code" : $('#code').val(),
					"search_LIKE_contact" : $('#contact').val(),
					"search_LIKE_telephone" : $('#telephone').val()
				}
			});

			//点击“搜索”
			$('#button_query').on('click', function() {
				treeGrid.reload('treeTable', {
					where : {
						"search_LIKE_name" : $('#name').val(),
						"search_LIKE_code" : $('#code').val(),
						"search_LIKE_contact" : $('#contact').val(),
						"search_LIKE_telephone" : $('#telephone').val()
					}
				});
			});

			$("#button_add").click(function() {
				page.loadPage("${ctx}/admin/company/toAdd");
			});
			//监听工具条
			treeGrid.on('tool(treeTable)', function(obj) { //注：tool是工具条事件名，mainTable是table原始容器的属性 lay-filter="对应的值"
				var data = obj.data; //获得当前行数据
				var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
				var tr = obj.tr; //获得当前行 tr 的DOM对象
				if (layEvent === 'del') { //删除
					layui.layer.confirm('确定删除该行？', function(index) {
						obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						layui.layer.close(index);
						//向服务端发送删除指令
						common.deleteData('${ctx}/admin/company/delete', data["id"]);
						treeGrid.reload('treeTable');
					});
				} else if (layEvent === 'update') { //修改
					page.loadPage("${ctx}/admin/company/toEdit?id=" + data["id"]);
				}
			});
		})
	});
</script>