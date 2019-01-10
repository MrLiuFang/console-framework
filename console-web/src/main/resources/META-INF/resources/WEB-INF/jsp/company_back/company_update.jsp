<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="pepper" uri="http://pepper.com"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<span id="page-title" style="display: none">新增</span>

<div class="layui-fluid">
	<div class="layui-row layui-col-space15">
		<div class="layui-card">
			<div class="layui-card-header">新增</div>
			<div class="layui-card-body">
				<form class="layui-form " action="${ctx}/admin/company/update"
					id="form-addOrUpdate" returnType="json">
					<input type="hidden" id="id" name="id" value="${company.id}">
					<div class="layui-form-item ">
						<label class="layui-form-label">所属公司</label>
				      <div class="layui-input-inline">
				      	<input type="hidden" name="parentId" id="parentId" value="${company.parentId}"  maxlength="50" readonly="readonly" class="layui-input" />
				        <input type="text" name="parentName" id="parentName" value="${parentCompanyName}" maxlength="50" readonly="readonly" class="layui-input" placeholder="请选择所属公司" style="cursor:pointer;" />
				      </div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">公司名称<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="name" id="name" maxlength="50" value="${company.name}"
								lay-verify="required" autocomplete="off" placeholder="公司名称"
								class="layui-input" />
						</div>
						<div class="layui-form-mid layui-word-aux">请保持唯一。</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">公司编码<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<input type="text" name="code" id="code" lay-verify="required" value="${company.code}"
								maxlength="50" autocomplete="off" placeholder="公司编码"
								class="layui-input" />
						</div>
						<div class="layui-form-mid layui-word-aux">请保持唯一。</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">联系人</label>
						<div class="layui-input-inline">
							<input type="text" name="contact" id="contact" value="${company.contact}"
								maxlength="50" autocomplete="off" placeholder="联系人"
								class="layui-input" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">联系电话</label>
						<div class="layui-input-inline">
							<input type="text" name="telephone" id="telephone" maxlength="50" value="${company.telephone}"
								autocomplete="off" placeholder="联系电话" class="layui-input" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">地址</label>
						<div class="layui-input-inline">
							<input type="text" name="address" id="address" maxlength="50" value="${company.address}"
								autocomplete="off" placeholder="地址" class="layui-input" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">邮编</label>
						<div class="layui-input-inline">
							<input type="text" name="zipCode" id="zipCode" maxlength="6" value="${company.zipCode}"
								autocomplete="off" placeholder="邮编" class="layui-input" />
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">备注</label>
						<div class="layui-input-inline">
							<textarea rows="5" cols="" name="remark" id="remark" style="width: 100%;">${company.zipCode}</textarea>
						</div>
					</div>
					<div class="layui-form-item ">
						<label class="layui-form-label">状态<span class="qi-req">*</span></label>
						<div class="layui-input-inline">
							<pepper:Enum2Select noDefault="true" documentId="status" selectedValue="${company.status}"
								enumClass="com.qicloud.common.emuns.model.Status"
								documentName="status" />
						</div>
					</div>
					<div class="layui-inline " style="text-align: center;">
						<label class="layui-form-label"></label>
						<pepper:auth code="SYSTEM_SETTINGS_ADMIN_USER_ADD">
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
			page.loadPage("${ctx}/admin/company/index");
		});

regionUtil.initRegionSelect("regionSelect");
		
		$("#parentName").click(function(){
			layer.open({
					type : 1,
					title : '请选择所属公司',
					area : [ '50%', '70%' ],
					shade : 0.5,
					maxmin : true,
					content : "<form id='formTreeTable' class='layui-form layui-form-pane'><div class='layui-card layui-form'><table class='layui-hide' id='treeTable' lay-filter='treeTable'></table></div></form>",
					btn : [ '关闭' ],
					yes : function() {
						layer.closeAll();
					},
					zIndex : layer.zIndex,
					success : function(layero) {
						layer.setTop(layero);
					}
			});
			
			layui.use('treeGrid', function() {
				var treeGrid = layui.treeGrid; //很重要
				var treeTable = treeGrid.render({
					elem : '#treeTable',
					method : "post",
					url : '${ctx}/admin/company/list',
					treeId : 'id'//树形id字段名称
					,
					treeUpId : 'parentId'//树形父id字段名称
					,
					treeShowName : 'name'//以树形式显示的字段
					,
					cols : [ [{
						field : 'id',
						templet : function(d) {
							return '<input disabled="disabled" id="companyId" name="companyId" companyName="'+d.name+'" cbid="'+d.id+'" cb-parent="'+d.parentId+'"type="checkbox" lay-skin="primary" />';
						},
						width : 50
					},  {
						field : 'name',
						title : '公司名称'
					} ] ],
					done : function (res){
						$("input[name='companyId']").each(function() {
							if($(this).attr("cbid") == $("#parentId").val()){
								$(this).attr("checked", true);
							}
						});
						layui.form.render();
						$("#formTreeTable table").bind("click", function(event) {
							var target = event.target;
							var cb = $(target).closest("tr").find("[type=checkbox]");
							if($("#id").val()==$(cb).attr("cbid")){
								$(cb).attr('checked',false);
								layui.layer.msg("不能选择《"+$(cb).attr("companyName")+"》作为所属公司");
								return false;
							}
							
							if ($(cb).is(":checked")) {
								$(cb).attr('checked',false);
								$("#parentName").val($(cb).attr(""));
								$("#parentId").val($(cb).attr(""));
							} else {
								$(cb).attr('checked',true);
								$("#parentName").val($(cb).attr("companyName"));
								$("#parentId").val($(cb).attr("cbid"));
							}
							$("input[name='companyId']").each(function() {
								if($(this).attr("cbid") != $(cb).attr("cbid")){
									$(this).attr("checked", false);
								}
							});
							layui.form.render();
						});
					},
					page : false,
				});				
			});
		});
	});
</script>