<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<form class="layui-form layui-form-pane" action="" id="form-query"
				returnType="html">
				<div class="layui-form-item">
					<div class="layui-inline">
						<label class="layui-form-label">编码</label>
						<div class="layui-input-inline">
							<input type="text" name="code" id="code" placeholder="编码"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline">
						<label class="layui-form-label">值</label>
						<div class="layui-input-inline">
							<input type="text" name="value" id="value" placeholder="值"
								class="layui-input">
						</div>
					</div>
				</div>
				<div class="layui-form-item qc-list-bar">
					<div class="layui-inline">
						<button class="layui-btn" id="button_query" type="button"
							data-type="reload">查询</button>
						<pepper:auth code="SYSTEM_SETTINGS_PARAMETER_TOADD">
							<button class="layui-btn layui-btn-normal" id="button_add"
								type="button">新增</button>
						</pepper:auth>
					</div>
				</div>
			</form>
		</div>
		<div class="layui-card">
			<table class="layui-hide" id="mainTable" lay-filter="mainTable"></table>
		</div>
	</div>
</div>

<script id="selectTmpl" type="text/x-jquery-tmpl">
	{{each(i,d) data}}
		<option value="{{= d.id }}" {{if d.id == point}}selected{{/if}}>{{= d.name }}</option>
	{{/each}}
</script>

<script type="text/html" id="barDemo">
	<pepper:auth code="SYSTEM_SETTINGS_PARAMETER_TOEDIT">
		<a class="layui-btn layui-btn-xs" lay-event="update">编辑</a>
	</pepper:auth>
	<pepper:auth code="SYSTEM_SETTINGS_PARAMETER_DELETE">
		<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
	</pepper:auth>
</script>

<script type="text/javascript">
	$(function($) {

		if (!$.cookie("param_warn")) {
			//提醒谨慎操作
			layui.layer
					.open({
						type : 1,
						title : false //不显示标题栏
						,
						closeBtn : false,
						area : '300px;',
						shade : 0.8,
						id : 'LAY_layuipro' //设定一个id，防止重复弹出
						,
						resize : false,
						btn : [ '明白' ],
						btnAlign : 'c',
						moveType : 1 //拖拽模式，0或者1
						,
						content : '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">温馨提示：</br>此模块为系统参数模块，请认真阅读系统操作文档后谨慎操作。</div>',
						success : function() {
							$.cookie("param_warn", "0", {
								expires : 1
							});
						}
					});
		}

		var $ = layui.$;
		var table = layui.table;

		//表格渲染
		table.render({
			elem : '#mainTable',
			url : '${ctx}/admin/parameter/list',
			method : "post",
			cols : [ [ {
				field : 'code',
				title : '编码'
			}, {
				field : 'value',
				title : '值'
			}, {
				field : 'remarks',
				title : '备注'
			}, {
				fixed : 'right',
				width : 150,
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
					"search_LIKE_code" : $('#code').val(),
					"search_LIKE_value" : $('#value').val()
				}
			});
		});

		//监听工具条
		table.on('tool(mainTable)', function(obj) { //注：tool是工具条事件名，mainTable是table原始容器的属性 lay-filter="对应的值"
			var data = obj.data; //获得当前行数据
			var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			var tr = obj.tr; //获得当前行 tr 的DOM对象
			if (layEvent === 'del') { //删除
				layui.layer.confirm('确定删除该行？', function(index) {
					obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
					layui.layer.close(index);
					//向服务端发送删除指令
					common.deleteData('${ctx}/admin/parameter/delete', data["id"]);
				});
			} else if (layEvent === 'update') { //修改
				page.loadPage("${ctx}/admin/parameter/toEdit?id=" + data["id"]);
			}
		});

		//新增跳转
		$("#button_add").click(function() {
			page.loadPage("${ctx}/admin/parameter/toAdd");
		})
	});
</script>

