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
							<select id="companyId" name="companyId">
								<c:forEach var="company" items="${companyList}">
									<option value="${company.id}">${company.name}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="layui-inline ">
						<label class="layui-form-label">部门名称</label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" placeholder="部门名称"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline ">
						<label class="layui-form-label">部门编码</label>
						<div class="layui-input-inline">
							<input type="text" name="code" id="code" placeholder="部门编码"
								class="layui-input">
						</div>
					</div>
					
				</div>
				<div class="layui-form-item qc-list-bar">
					<div class="layui-inline">
						<button class="layui-btn" id="button_query" type="button"
							data-type="reload">查询</button>
						<pepper:auth code="BASIC_INFORMATION_DEPARTMENT_ADD">
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
	<pepper:auth code="BASIC_INFORMATION_DEPARTMENT_TO_EDIT">
		<a class="layui-btn layui-btn-xs" lay-event="update"> 编辑</a>
	</pepper:auth>
	<pepper:auth code="BASIC_INFORMATION_DEPARTMENT_USER">
		<a class="layui-btn layui-btn-xs" lay-event="user">人员</a>
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
				height : 600,
				url : '${ctx}/console/department/list',
				treeId : 'id'//树形id字段名称
				,
				treeUpId : 'parentId'//树形父id字段名称
				,
				treeShowName : 'name'//以树形式显示的字段
				,
				cols : [ [ {
					field : 'name',
					title : '部门名称'
				}, {
					field : 'code',
					title : '编码'
				},{
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
					"name" : $('#name').val(),
					"code" : $('#code').val(),
					"companyId" : $('#companyId').val()
				}
			});

			//点击“搜索”
			$('#button_query').on('click', function() {
				treeGrid.reload('treeTable', {
					where : {
						"name" : $('#name').val(),
						"code" : $('#code').val(),
						"companyId" : $('#companyId').val()
					}
				});
			});

			$("#button_add").click(function() {
				page.loadPage("${ctx}/console/department/toAdd");
			});
			//监听工具条
			treeGrid.on('tool(treeTable)', function(obj) { //注：tool是工具条事件名，mainTable是table原始容器的属性 lay-filter="对应的值"
				var data = obj.data; //获得当前行数据
				var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
				var tr = obj.tr; //获得当前行 tr 的DOM对象
				if (layEvent === 'user') { //删除
					layui.layer.confirm('确定删除该行？', function(index) {
						obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
						layui.layer.close(index);
						//向服务端发送删除指令
						common.deleteData('${ctx}/console/department/delete', data["id"]);
						treeGrid.reload('treeTable');
					});
				} else if (layEvent === 'update') { //修改
					page.loadPage("${ctx}/console/department/toEdit?id=" + data["id"]);
				}
			});
			
			
		})
	});
	
	function getUser(){
		
	}
</script>