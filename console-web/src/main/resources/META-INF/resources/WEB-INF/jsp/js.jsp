<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script src="${ctx}/assets/js/jquery/jquery-3.2.1.min.js"></script>
<script src="${ctx}/assets/js/jquery/jquery.validate.min.js"></script>
<script src="${ctx}/assets/js/jquery/jquery.cookie.js"></script>
<script src="${ctx}/assets/js/jquery/jquery.serializejson.min.js"></script>
<script src="${ctx}/assets/layui/layui.all.js"></script>
<script src="${ctx}/assets/js/json/cycle.js"></script>
<script src="${ctx}/assets/js/json/json_parse_state.js"></script>
<script src="${ctx}/assets/js/json/json_parse.js"></script>
<script src="${ctx}/assets/js/json/json2.js"></script>
<script src="${ctx}/assets/js/baseUtil.js"></script>
<script src="${ctx}/assets/js/basePlugin.js"></script>
<script src="${ctx}/assets/js/baseDateUtil.js"></script>
<script src="${ctx}/assets/js/jquery/jquery.tmpl.js"
	type="text/javascript"></script>
<c:forEach items="${jsLoad}" var="jsUrl">
	<script src="${jsUrl}" type="text/javascript"></script>
</c:forEach>
<script id="ddTmpl" type="text/x-jquery-tmpl">
	{{each(i,d) data}}
		<dd lay-value="{{= d.value }}" class="">{{= d.label }}</dd>
	{{/each}}
</script>

<script type="text/javascript">
	$(function($) {
		$.fn.serializeJson = function() {
			var serializeObj = {};
			var array = this.serializeArray();
			var str = this.serialize();
			$(array).each(function() {
				if (serializeObj[this.name]) {
					if ($.isArray(serializeObj[this.name])) {
						serializeObj[this.name].push($.trim(this.value));
					} else {
						serializeObj[this.name] = [ serializeObj[this.name], $.trim(this.value) ];
					}
				} else {
					serializeObj[this.name] = $.trim(this.value);
				}
			});
			return serializeObj;
		};

		/**
		 * $.cookie(key,value,params)的第三个参数
		 */
		window.cookieParams = {
			path : "${ctx}" + "/"
		};
		/**
		 * 默认的，Jquery.cookie插件读取内容的时候都会经过encodeURIComponent/decodeURIComponent编码，
		 * 如果忽略这一点，要进行如下设置
		 */
		$.cookie.raw = true;
		$.cookie.defaults = {};

		/**
		 * 页面菜单还原
		 */
		//给二级菜单加点击事件，加载页面
		$(".layui-side-scroll .layui-nav-child dd a").click(function() {
			$(this).parent().addClass("layui-this");
			var url = $(this).attr("url");
			if (url) {
				page.loadPage(url);
			}
			//添加二级菜单cookie
			var menuIndex = $(this).attr("menu-index");
			$.cookie("menu-index", menuIndex, cookieParams);
		});
		//判断是否有二级菜单cookie，有则加载，没有自动加载第一个菜单
		if ($.cookie("menu-index")) {
			var menu = $(".layui-side-scroll .layui-nav-child dd a[menu-index=" + $.cookie("menu-index") + "]");
			menu.click();
			menu.closest(".layui-nav-item").addClass("layui-nav-itemed");
		} else {
			$(".layui-side.layui-side-menu .layui-nav-child dd:first a:first ").click();
		}

		//加载layui插件
		layui.extend({
			treeGrid : '${ctx}/assets/layui/lay/modules/treeGrid', // {/}的意思即代表采用自有路径，即不跟随 base 路径
			autocomplete : '${ctx}/assets/layui/lay/modules/autocomplete'
		})

	});
</script>

<script type="text/javascript">
	var common = {
		/**
		 * 删除事件
		 */
		deleteData : function(url, id) {
			layui.layer.closeAll();
			baseUtil.AJpost(url, {
				id : id
			})
		},

		/**
		 * AJXA请求公共处理返回结果
		 */
		handleResult : function(data) {
			// layui.layer.closeAll();
			if (data.status == -400) {
				layui.layer.msg("登录超时！请重新登录！", {
					icon : 5
				});
				setTimeout("location.replace(location.href)", 3000);
				return false;
			}
			if (data.status == -600) {
				layui.layer.msg("权限不足！请与系统管理员联系！", {
					icon : 5
				});
				return false;
			}
			if (data.status == -300 || data.status == -200) {
				if (data.exceptionMessage) {
					layui.layer.msg(data.exceptionMessage, {
						icon : 5
					});
				} else if (data.message) {
					layui.layer.msg(data.message, {
						icon : 5
					});
				}
				return false;
			}
			if (data.loadUrl) {
				if (data.loadUrl == "/") {
					//如果ajax方式提交后需要跳回首页，不能使用page.loadPage()，因为用执行page.loadPage("${ctx}/")会加载首页的内容，首页的内容又有自动点击菜单的代码，所以会循环加载。
					setTimeout(function() {
						location.href = "${ctx}";
					}, 1000)
				} else {
					setTimeout("page.loadPage('${ctx}" + (data.loadUrl) + "')", 1000);
				}
			}
			page.isLoad = false;
			return true;
		}
	};

	var page = {
		isLoad : null,
		/**
		 * 点击菜单事件
		 */
		loadPage : function(url) {
			if (page.isLoad) {
				layui.layer.msg("正在加载中！请稍等！");
				return false;
			}
			page.isLoad = true;
			//把之前的socket关闭，谷哥浏览器达到六个socket连接就不能正常使用了
			$.ajax({
				type : 'POST',
				url : url,
				timeout : 10000,
				data : {
					"timestamp" : new Date().getTime()
				},
				success : function(data, status) {
					layui.layer.closeAll();
					$("#right-content").html(data);
					page.isLoad = false;
					/**
					 * 图片弹出层
					 */
					layui.layer.photos({
						photos : '#right-content',
						anim : 5
					// 0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
					});
					/**
					 * page.loadPage()方法执行后，如果跳转到二级菜单，那么选中该二级菜单，同时打开该二级菜单的一级菜单。
					 */
					var thisMenu = $(".layui-side-menu .layui-nav-child a[url='" + url.split("?")[0] + "']");
					if (thisMenu.length == 1 && thisMenu.closest("dd").not("layui-this")) {
						$(".layui-side-menu .layui-this").removeClass("layui-this");
						thisMenu.closest("dd").addClass("layui-this");
						$(".layui-side-menu .layui-this").closest(".layui-nav-item").addClass("layui-nav-itemed");
						//添加二级菜单cookie
						var menuIndex = thisMenu.attr("menu-index");
						$.cookie("menu-index", menuIndex, cookieParams);
					}
					//刷新面包屑
					page.showBread();
					//渲染新页面的from
					layui.form.render();
					page.formAction();
					//更新还原当前页面的url
					$.cookie("this-url", url, cookieParams);
					//初始化历史查询
					page.hisSearch(url);
				},
				fail : function(err, status) {
					layui.layer.closeAll();
					page.isLoad = false;
				},
				error : function(jqXHR, error, errorThrown) {
					page.isLoad = false;
					layui.layer.closeAll();
					if (error == 'timeout') {//超时,status还有success,error等值的情况
						layui.layer.msg("链接超时,正在尝试刷新页面!");
						setTimeout("window.location.reload(true)", 2000);
					} else if (jqXHR.status == "404") {
						page.loadPage("${ctx}/404");
					} else {
						page.loadPage("${ctx}/500");
					}
				},
				dataType : "html"
			});
		},

		formAction : function() {
			$("#right-content form").submit(function() {
				//如果表单中想自己组装提交数据，自己写ajax提交，可以在表单的action中写#，阻止底层默认监听
				if ($(this).attr("action") == "#") {
					return false;
				}
				if (page.isLoad) {
					layui.layer.closeAll();
					layui.layer.msg("正在提交数据！请稍等！");
					return false;
				}
				page.isLoad = true;
				var data = $(this).serializeJson();

				var tempCheckboxName = {};
				$("input:checkbox:checked").each(function() { // 遍历checkbox的多选框
					var checkboxName = $(this).attr("name");
					if (!tempCheckboxName.hasOwnProperty(checkboxName)) {
						tempCheckboxName[checkboxName] = checkboxName;
						data[checkboxName] = new Array();
					}
					data[checkboxName].push($(this).val());
				});

				data.timestamp = new Date().getTime();
				var returnType = $(this).attr("returnType");
				var refreshElement = $(this).attr("refreshElement");
				var loadUrl = $(this).attr("loadUrl");
				var pageNo = $("#pageNo").val();
				var onClickSource = $("#onClickSource").val();
				if (typeof (onClickSource) != "undefined" && onClickSource == "navigator") {
					if (typeof (pageNo) != "undefined") {
						data.pageNo = pageNo;
					}
				}
				$.ajax({
					type : 'POST',
					url : this.action,
					data : data,
					timeout : 10000,
					success : function(data, status) {
						layui.layer.closeAll();
						if (returnType == "html") {
							$("#" + refreshElement).html(data);
							layui.form.render();
						}
						if (returnType == "json") {
							if (data.status == 200 && data.message) {
								layui.layer.msg(data.message);
							}
							common.handleResult(data);
						}
						page.isLoad = false;
					},
					fail : function(err, status) {
						layui.layer.closeAll();
						layui.layer.msg("异常错误!", {
							icon : 5
						});
						page.isLoad = false;
					},
					error : function(jqXHR, error, errorThrown) {
						layui.layer.closeAll();
						if (error == 'timeout') {//超时,status还有success,error等值的情况
							layui.layer.msg("链接超时,请重试!");
							//setTimeout("location.replace(location.href)", 4000)
						} else {
							layui.layer.msg("异常错误!", {
								icon : 5
							});
						}
						page.isLoad = false;
					},
					dataType : returnType
				});
				return false;
			});
		},
		//历史查询
		hisSearch : function(url) {
			var btnQuery = $('.qc-list-bar #button_query');
			if (btnQuery.length > 0) {
				btnQuery.closest("form").attr("lay-filter", "form-query");
				btnQuery.addClass("has-his-search");
				//初始化组件元素
				btnQuery.after('<div class="layui-unselect layui-form-select layui-form-selected qc-his-search "><span title="最近搜索" class=" layui-icon layui-icon-triangle-d" id="his-search-icon">'
						+ '</span><dl class="layui-anim layui-anim-upbit layui-hide" id="his-search-dl"></dl></div>');
				btnQuery.bind("click", function() {
					//点击“查询”按钮更新历史搜索项
					var searchJson = $('#form-query').serializeJSON();
					var searchLabel = [];
					$('#form-query').find(".layui-inline").each(function() {
						var _this = $(this);
						if (_this.find(".layui-form-label").length > 0) {
							var labelText = _this.find(".layui-form-label").text();
							var labelValue = [];
							_this.find("input.layui-input").each(function() {
								labelValue.push($(this).val());
							})
							if ($.trim(labelValue.join(""))) {
								searchLabel.push(labelText + "：" + labelValue.join(" / "));
							}
						}
					});
					var hisSearchStr = $.cookie("hs_" + url);
					var newHisSearchStr = [];
					var labels = searchLabel.join("；");
					if ($.trim(labels)) {
						//没有填写搜索项的搜索不记录
						var lastSearch = {
							"label" : labels,
							"value" : JSON.stringify(searchJson)
						};
						newHisSearchStr.push(lastSearch)
						if (hisSearchStr) {
							var lastSearchStr = JSON.stringify(lastSearch);
							hisSearchStr = hisSearchStr.replace(lastSearchStr + ",", "").replace("," + lastSearchStr, "").replace(lastSearchStr, "");
							var hisSearchList = baseUtil.json(hisSearchStr);
							for (var i = 0; i < 7; i++) {
								if (hisSearchList[i]) {
									newHisSearchStr.push(hisSearchList[i]);
								}
							}
						}
						$.cookie("hs_" + url, JSON.stringify(newHisSearchStr));
					}
				});

				//历史搜索下拉渲染
				var hisSearchDl = $("#his-search-dl")[0];
				var tipsi;
				$(document).unbind().click(function(e) {
					var _target = e.target;
					if (_target.id == "his-search-icon") {
						if ($(hisSearchDl).is(".layui-hide")) {
							var hisSearchStr = $.cookie("hs_" + url);
							if (hisSearchStr) {
								baseUtil.template("ddTmpl", {
									"data" : baseUtil.json(hisSearchStr)
								}, $(hisSearchDl), false);
								$(hisSearchDl).find("dd").unbind().hover(function() {
									var that = this;
									var text = $(that).text();
									if (text) {
										tipsi = layui.layer.tips(text, that, {
											time : 0,
											tips : 4
										});
									}
								}, function() {
									layui.layer.close(tipsi);
								});
							} else {
								baseUtil.template("ddTmpl", {
									"data" : [ {
										"label" : "无最近搜索",
										"value" : ""
									} ]
								}, $(hisSearchDl), false);
							}
							$(hisSearchDl).removeClass("layui-hide").addClass("layui-show");
						} else {
							$(hisSearchDl).addClass("layui-hide").removeClass("layui-show");
						}
						return false;
					}
					if (!(hisSearchDl.contains(_target))) {
						$(hisSearchDl).addClass("layui-hide").removeClass("layui-show");
					} else if (hisSearchDl.contains(_target) && _target.tagName == "DD") {
						var searchJsonStr = $(_target).attr("lay-value");
						if (searchJsonStr) {
							layui.form.val("form-query", baseUtil.json(searchJsonStr));
							$("#button_query").click();
						}
						$(hisSearchDl).addClass("layui-hide").removeClass("layui-show");
					}
				});
			}
		},
		//刷新当前页
		reflesh : function() {
			var url = $.cookie("this-url");
			if (url) {
				page.loadPage(url);
			}
		},
		/**
		 * 点击面包屑
		 * @param level 层级，0：首页，1：一级菜单，2：二级菜单，3：操作页
		 */
		breadClick : function(level, url) {
			if (level == 0) {
				//如果是首页，关闭所有二级菜单，打开第一个一级菜单，点击第一个一级菜单的第一个子菜单
				$(".layui-side-menu .layui-this").removeClass("layui-this");
				$(".layui-side-menu .layui-nav-item:first").addClass("layui-nav-itemed");
				$(".layui-side-menu .layui-nav-item:first .layui-nav-child a:first").click();
			} else if (level == 1) {
				//如果是一级菜单，然后关闭其他一级菜单，打开当前二级菜单所在的一级菜单，如果是侧边栏收缩状态，展开侧边栏
				$(".layui-side-menu .layui-nav-itemed").removeClass("layui-nav-itemed");
				$(".layui-side-menu .layui-this").closest(".layui-nav-item").addClass("layui-nav-itemed");
				if ($("#flexible-menu").is(".layui-icon-spread-left")) {
					$("#flexible-menu").click();
				}
			} else if (level == 2) {
				//如果是二级菜单，跳转页面，打开当前二级菜单所在的一级菜单。
				page.loadPage(url);
				$(".layui-side-menu .layui-this").closest(".layui-nav-item").addClass("layui-nav-itemed");
			} else if (level == 3) {
				//如果是操作页，刷新当前页就好。
				$("#qc-refresh-page").click();
			}
		},
		// 显示面包屑
		showBread : function() {
			var title = $("#page-title");
			var pageTitle = title.text();
			// 如果有#page-title但没有指定标题，面包屑隐藏，比如“我的资料”，“密码修改”这些公共页面
			if (title.length > 0 && !pageTitle) {
				$(".layadmin-header").removeClass("layui-show");
				$(".layadmin-header").addClass("layui-hide");
				// 因为隐藏掉面包屑了，所以内容体需要往上以一些
				$("#right-content .layui-fluid").addClass("no-bread");
				return;
			} else {
				$(".layadmin-header").addClass("layui-show");
				$(".layadmin-header").removeClass("layui-hide");
			}
			var menuThis = $(".layui-side-scroll .layui-this a");
			var pageTitleBread, menuThisBread, parentMenuBread, homeBread;
			if (pageTitle) {
				pageTitleBread = '<span lay-separator="">/</span><a style="cursor: pointer;" onclick="page.breadClick(3)" >' + pageTitle + '</a>';
			}
			menuThisBread = "<span lay-separator=''>/</span><a style='cursor: pointer;' onclick=\"page.breadClick(2,'" + menuThis.attr("url") + "')\" >" + menuThis.text() + "</a>";
			parentMenuBread = '<span lay-separator="">/</span><a style="cursor: pointer;" onclick="page.breadClick(1)" >' + menuThis.closest("li").find("a:first").text() + '</a>';
			homeBread = "<a style='cursor: pointer;' onclick=\"page.breadClick(0)\"><i class='layui-icon layui-icon-home' style='margin-right:5px'></i>首页</a>";
			if (title.is(".qc-bread-only")) {
				//如果#page-title元素有qc-bread-only这个class，表示它虽然想显示面包屑，但是它不知道它的二级面包屑是谁，比如“我的资料”，“密码修改”这些公共页面，任何地方都可以直接跳过来。
				$(".layadmin-header .layui-breadcrumb").html(homeBread + pageTitleBread);
			} else {
				$(".layadmin-header .layui-breadcrumb").html(homeBread + parentMenuBread + menuThisBread + pageTitleBread);
			}
		}
	};
</script>

<script type="text/javascript">
	/**
	 * 顶栏工具按钮实现。
	 */
	$(function() {
		//侧边栏收缩
		var tipsi;
		$("#flexible-menu").click(function() {
			if ($(this).is(".layui-icon-shrink-right")) {
				//收缩侧边栏
				$("body.layui-layout-body").addClass("layadmin-side-shrink");
				$(this).removeClass("layui-icon-shrink-right").addClass("layui-icon-spread-left");
				$(".menu-l1").hover(function() {
					var that = this;
					tipsi = layer.tips($(that).attr("lay-tips"), that, {
						time : 0
					});
				}, function() {
					layer.close(tipsi);
				});
				//收缩状态后点击任意菜单展开
				$('.menu-l1').click(function() {
					$(".layui-side-menu .layui-nav-itemed").removeClass("layui-nav-itemed");
					$(this).closest(".layui-nav-item").addClass("layui-nav-itemed");
					$("#flexible-menu").click();
				});
				//收窄面包屑边距
				$(".layadmin-header .layui-breadcrumb").addClass("short");
				//刷新页面
				setTimeout(function() {
					page.reflesh();
				}, 200);
			} else {
				//展开侧边栏
				$("body.layui-layout-body").removeClass("layadmin-side-shrink");
				$(this).removeClass("layui-icon-spread-left").addClass("layui-icon-shrink-right");
				$('.menu-l1').unbind();
				layer.close(tipsi);
				//还原面包屑边距
				$(".layadmin-header .layui-breadcrumb").removeClass("short");
				//刷新页面
				setTimeout(function() {
					page.reflesh();
				}, 200);
			}
		})
		//刷新按钮
		$("#qc-refresh-page").click(function() {
			page.reflesh();
		})
		//展开菜单
		$("#qc-menu-expand").click(function() {
			$(".layui-side-menu .layui-nav-item").addClass("layui-nav-itemed");
		});
		//收起菜单
		$("#qc-menu-pack-up").click(function() {
			$(".layui-side-menu .layui-nav-itemed").removeClass("layui-nav-itemed");
		});
	});
</script>

