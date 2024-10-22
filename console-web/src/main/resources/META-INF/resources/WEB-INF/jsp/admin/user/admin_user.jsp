<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<form class="layui-form layui-form-pane" action="" id="form-query" >
				<div class="layui-form-item">
					<div class="layui-inline ">
						<label class="layui-form-label">姓名</label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" placeholder="姓名"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline ">
						<label class="layui-form-label">邮箱</label>
						<div class="layui-input-inline">
							<input type="text" name="email" id="email" placeholder="邮箱"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline ">
						<label class="layui-form-label">电话</label>
						<div class="layui-input-inline">
							<input type="text" name="mobile" id="mobile" placeholder="电话"
								class="layui-input">
						</div>
					</div>
					<div class="layui-inline ">
						<label class="layui-form-label">状态</label>
						<div class="layui-input-inline" style="width: 190px;">
							<pepper:Enum2Select documentId="status"
								enumClass="com.pepper.common.emuns.Status"
								documentName="status" />
						</div>
					</div>
					<div class="layui-inline ">
						<label class="layui-form-label">角色</label>
						<div class="layui-input-inline" style="width: 190px;">
							<select name="roleId" id="roleId" lay-filter="roleId">
								<option value=""></option>
								<c:forEach var="item" items="${roles}">
									<option value="${item.id }">${item.name }</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>
				<div class="layui-form-item qc-list-bar">
					<div class="layui-inline">
						<button class="layui-btn" id="button_query" type="button"
							data-type="reload">查询</button>
						<pepper:auth code="SYSTEM_SETTINGS_ADMIN_USER_TO_ADD">
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
<script id="selectTmpl" type="text/x-jquery-tmpl">
	{{each(i,d) data}}
		<option value="{{= d.id }}" {{= d.selected }}>{{= d.name }}</option>
	{{/each}}
</script>

<script type="text/html" id="bar">
	<pepper:auth code="SYSTEM_SETTINGS_ADMIN_USER_ROLE_LIST">
		<a class="layui-btn layui-btn-xs" lay-event="opt-role">角色</a>
	</pepper:auth>
	<pepper:auth code="SYSTEM_SETTINGS_ADMIN_USER_TO_VIEW">
		<a class="layui-btn layui-btn-xs" lay-event="view">查看</a>
	</pepper:auth>
	<pepper:auth code="SYSTEM_SETTINGS_ADMIN_USER_TO_EDIT">
		<a class="layui-btn layui-btn-xs" lay-event="update">编辑</a>
	</pepper:auth>
	<pepper:auth code="SYSTEM_SETTINGS_ADMIN_USER_RE_PWD">
		<a class="layui-btn layui-btn-xs" lay-event="rePwd">重置密码</a>
	</pepper:auth>
</script>

<script type="text/html" id="userStatus">
      <input <pepper:noauth code="SYSTEM_SETTINGS_ADMIN_USER_TO_EDIT">disabled</pepper:noauth> data-id="{{d.id}}" lay-filter="status-switch" type="checkbox" lay-skin="switch" lay-text="正常|禁用" {{ d.status == '正常' ? 'checked' : '' }}/>
</script>

<script type="text/javascript">
	$(function() {

		var $ = layui.$;
		var table = layui.table;
		//表格渲染
		table.render({
			elem : '#mainTable',
			url : '${ctx}/console/user/list',
			method : "post",
			cols : [ [ {
				field : 'name',
				title : '姓名'
			}, {
				field : 'account',
				title : '账号'
			}, {
				field : 'email',
				title : '邮箱'
			}, {
				field : 'gender',
				title : '性别'
			}, {
				field : 'mobile',
				title : '电话'
			}, {
				field : 'nickName',
				title : '昵称'
			}, {
				field : 'createUser',
				title : '角色'
			}, {
				field : 'lastLoginTime',
				title : '最后登录'
			}, {
				fixed : 'right',
				width : 100,
				templet : '#userStatus',
				title : '状态'
			}, {
				fixed : 'right',
				width : 250,
				toolbar : '#bar',
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
					"search_LIKE_email" : $('#email').val(),
					"search_LIKE_mobile" : $('#mobile').val(),
					"search_EQ_status" : $('#status').val(),
					"search_EQ_type" : $('#type').val(),
					"search_EQ_roleId" : $('#roleId').val()
				}
			});
		});

		//监听工具条
		table.on('tool(mainTable)', function(obj) { //注：tool是工具条事件名，mainTable是table原始容器的属性 lay-filter="对应的值"
			var data = obj.data; //获得当前行数据
			var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
			var tr = obj.tr; //获得当前行 tr 的DOM对象
			if (layEvent === 'view') { //查看
				page.loadPage("${ctx}/console/user/toView?id=" + data["id"])
			} else if (layEvent === 'del') { //删除
				layui.layer.confirm('确定删除该行？', function(index) {
					obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
					layui.layer.close(index);
					//向服务端发送删除指令
					common.deleteData('${ctx}/console/user/delete', data["id"]);
					table.reload('mainTable');
				});
			} else if (layEvent === 'update') { //修改
				page.loadPage("${ctx}/console/user/toEdit?id=" + data["id"]);
			} else if (layEvent === 'opt-role') { //修改角色
				openRole(data["id"]);
			} else if (layEvent === 'rePwd') { //修改角色
				layui.layer.confirm('确定重置用户" ' + data["name"] + ' "密码？', function(index) {
					layui.layer.close(index);
					baseUtil.AJpost('${ctx}/console/user/rePwd', {
						"userId" : data["id"]
					});
				});
			}
		});

		//新增跳转
		$("#button_add").click(function() {
			page.loadPage("${ctx}/console/user/toAdd");
		})

		//选择用户角色
		function openRole(userId) {
			basePlugin.win('选择角色', 'roleWin', "<form class='layui-form' lay-filter='role-select-form'><div style='margin:30px;height:100px'>" + "<select id='role-select' lay-filter='role-select'><option value=''></option></select></div></form>",
					function() {
						if (!$("#role-select").val()) {
							layui.layer.msg("请选择角色");
							return true;
						}
						baseUtil.AJpost('${ctx}/console/user/saveUserRole', {
							userId : userId,
							roleId : $("#role-select").val()
						});
					}, {
						success : function() {
							baseUtil.AJpost('${ctx}/console/user/roleList', {
								"userId" : userId
							}, function(data) {
								baseUtil.template('selectTmpl', {
									"data" : data.data.list,
									"point" : data.data["roleId"]
								}, $("#role-select"), true);
								layui.form.render('select', 'role-select-form');
								return false;
							});
						}
					})
		}

		//点击状态开关
		layui.form.on('switch(status-switch)', function(data) {
			var status = "NORMAL";//正常
			if (!data.elem.checked) {
				status = "DISABLE";//禁用
			}
			var id = $(data.elem).attr("data-id");
			baseUtil.AJpost("${ctx}/console/user/statusOnOff", {
				id : id,
				status : status
			})
		});
	});
</script>


