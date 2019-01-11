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
				<form class="layui-form " action="${ctx}/console/menu/update"
					id="form-addOrUpdate" returnType="json" refreshElement="">
					<input name="id" id="id" value="${menu.id}" style="display: none;">
					<!-- 菜单类型 -->
					<input type="hidden" name="menuType" value="${menu.menuType}">
					<input type="hidden" name="isLeaf" value="${menu.isLeaf}">
					<div class="layui-form-item  qc-select-group">
						<label class="layui-form-label">父菜单<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select required="true" documentId="scope"
								enumClass="com.qicloud.common.emuns.model.Scope"
								documentName="scope" selectedValue="${menu.scope}" />
						</div>
						<c:choose>
							<c:when test="${menu.menuType eq 1}">
								<div class="layui-input-inline">
									<select name="rootMenu" id="rootMenu" lay-filter="rootMenu"
										lay-verify="required">
										<option value=""></option>
									</select>
								</div>
								<div class="layui-input-inline">
									<select name="parentId" id="menuId" lay-filter="menuId"
										lay-verify="required">
										<option value=""></option>
									</select>
								</div>
							</c:when>
							<c:otherwise>
								<div class="layui-input-inline">
									<select name="parentId" id="rootMenu" lay-filter="rootMenu">
										<option value=""></option>
									</select>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">菜单名称<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" lay-verify="required"
								autocomplete="off" placeholder="菜单名称" class="layui-input"
								value="${menu.name}" maxlength="100" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">编码<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="code" id="code" lay-verify="required"
								autocomplete="off" placeholder="编码" class="layui-input"
								value="${menu.code}" maxlength="100" />
						</div>
					</div>
					<div
						class="layui-form-item <c:choose><c:when test="${menu.menuType eq 0 && menu.parentId eq '0'}">layui-hide</c:when></c:choose>"
						id="add-url-item">
						<input type="hidden" name="url" id="url" lay-verify="url" />
						<div class='layui-form-item '>
							<label class="layui-form-label">url<span class="qi-req">*</span></label>
							<div class="layui-input-inline">
								<input type="text" autocomplete="off" maxlength="200"
									placeholder="url" class="layui-input url">
							</div>
							<button class="layui-btn layui-btn-warm" id="add-url"
								type="button">添加url</button>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">排序<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="sort" id="sort"
								lay-verify="required|number" autocomplete="off" placeholder="排序"
								class="layui-input" value="${menu.sort}">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">状态<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select required="true" documentId="status"
								enumClass="com.qicloud.common.emuns.model.Status"
								documentName="status"
								selectedValue="${empty menu.status?'NORMAL':menu.status}" />
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">备注</label>
						<div class="layui-input-inline">
							<textarea placeholder="请输入内容" name="remarks" id="remarks"
								class="layui-textarea" maxlength="250">${menu.remarks}</textarea>
						</div>
					</div>
					<div class="layui-inline" style="text-align: center;">
						<label class="layui-form-label"></label>
						<pepper:auth code="SYSTEM_SETTINGS_MENU_UPDATE">
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

<script id="addUrlTmpl" type="text/x-jquery-tmpl">
<div class="layui-form-item ">
	<label class="layui-form-label"></label>
	<div class="layui-input-inline">
		<input type="text" autocomplete="off" value="{{= url }}"
			maxlength="200" placeholder="url" class="layui-input url">
	</div>
	<button class="layui-btn layui-btn-danger del-url" type="button">删除url</button>
</div>
</script>

<script type="text/javascript">
	$(function() {

		//点击"添加url"按钮
		$("#add-url").click(function() {
			baseUtil.template('addUrlTmpl', {
				url : ""
			}, $("#add-url-item"), true);
			//点击"删除url"按钮
			$(".del-url").unbind().click(function() {
				$(this).closest(".layui-form-item").remove();
			})
		});

		//校验，并且将url组装
		layui.form.verify({
			url : function(value, item) {
				if (!("${menu.menuType}" == 0 && !$("#rootMenu").val())) {
					var urls = [];
					$(".url").each(function() {
						if ($.trim(this.value)) {
							if (this.value.startWith("/")) {
								urls.push(this.value);
							} else {
								urls.push("/" + this.value);
							}
						}
					});
					if (urls.length > 0) {
						$("#url").val(urls.join(";"));
					} else {
						return 'url必填';
					}
				}
			}
		});

		//回显url
		if ("${menu.url}") {
			var urls = "${menu.url}".split(";");
			for (i = 0; i < urls.length; i++) {
				if (i != 0) {
					$("#add-url").click();
				}
				$(".url:last").val(urls[i]);
			}
		}

		function initChildMenuOption(value) {
			if (typeof (value) != "undefined") {
				var scope = $("#scope").val();
				$.ajax({
					type : 'POST',
					url : "${ctx}/console/menu/getMenuList",
					data : {
						scope : scope,
						parentId : value
					},
					success : function(data, status) {
						var childMenu = "${menu.parentId}";
						var selected = " selected='selected' ";
						var optionstring = "<option value=''></option>";
						$(data.data.list).each(function(i, item) {
							//debugger;
							if (childMenu == item.id) {
								optionstring += "<option value=\"" + item.id + "\"" + selected + ">" + item.name + "</option>";
							}
							if (childMenu != item.id) {
								optionstring += "<option value=\"" + item.id + "\" >" + item.name + "</option>";
							}

						});
						$("#menuId").html(optionstring);
						layui.form.render('select');
					},
					fail : function(err, status) {
						console.log(err)
					},
					dataType : "json"
				});
			}
			if (typeof (value) == "undefined") {
				$("#menuId").html("<option value=''></option>");
				layui.form.render('select');
			}
		}

		function initRootMenuOption(value) {
			if (typeof (value) != "undefined") {
				$.ajax({
					type : 'POST',
					url : "${ctx}/console/menu/getMenuList",
					data : {
						scope : value
					},
					success : function(data, status) {
						var rootMenu = "${menu.parentId}";
						if ("${menu.menuType}" == 1) {
							rootMenu = "${menuFirstId}";
						}
						var selected = " selected='selected' ";
						var optionstring = "<option value=''></option>";
						$(data.data.list).each(function(i, item) {
							//debugger;
							if (rootMenu == item.id) {
								optionstring += "<option value=\"" + item.id + "\"" + selected + ">" + item.name + "</option>";
							}
							if (rootMenu != item.id) {
								if ("${menu.id}" == item.id) {
									optionstring += "<option disabled='disabled'  value=\"" + item.id + "\" >" + item.name + "</option>";
								} else {
									optionstring += "<option value=\"" + item.id + "\" >" + item.name + "</option>";
								}

							}
						});
						$("#rootMenu").html(optionstring);
						layui.form.render('select');
					},
					fail : function(err, status) {
						console.log(err)
					},
					dataType : "json"
				});
			}
			if (typeof (value) == "undefined") {
				$("#rootMenu").html("<option value=''></option>");
				layui.form.render('select');
			}
		}

		$("#button_return").click(function() {
			page.loadPage("${ctx}/console/menu/index")
		});
		layui.form.on('select(scope)', function(data) {
			var value = data.value;
			initRootMenuOption(value);
		});
		layui.form.on('select(rootMenu)', function(data) {
			//debugger;
			var value = data.value;
			initChildMenuOption(value);
			if ("${menu.menuType}" == 0) {
				//如果添加菜单，点击父菜单，如果选中才出来url
				if (value) {
					$("#add-url-item").removeClass("layui-hide");
				} else {
					$("#add-url-item").addClass("layui-hide");
				}
			}
		});

		initRootMenuOption("${menu.scope}");
		initChildMenuOption("${menuFirstId}");
		layui.form.render('select');
	});
</script>