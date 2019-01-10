/**
 * 项目的js工具包
 */

window.baseUtil = {};

/**
 * 后面很多方法会绑定到这个对象上
 */
window.base = {};

baseUtil.redStar = "<span style='color:red'>*</span>";

/**
 * 封装jquery template的使用。
 * 
 * @param templateId:传入模板id，即
 *            <script type="text/template" id="xxx">中的xxx； data：模板数据；
 * @param selection：模板插入的html节点的jquery对象或表达式；append:true/false，
 *            是否以append的方式添加到现有节点内容代码的后面。
 * @return 返回生成节点的html字符串
 */
baseUtil.template = function(templateId, data, selection, append) {
	var template = $("#" + templateId).html();
	$.template("TEMPL_NAME" + templateId, template);
	var dom = $.tmpl("TEMPL_NAME" + templateId, data)
	if (append) {
		if (selection) {
			$(selection).append(dom);
		}
	} else {
		if (selection) {
			$(selection).html(dom);
		}
	}
	return dom.prop("outerHTML");
};

/**
 * JSON format 对象转换为json格式的数据，并且进行缩进格式化，调用方式baseUtil.JF(obj);
 * 注意，仅用于调试使用，发布前必须移除，因为里面的输出方式使用了console.log()，在IE下 会处于阻塞状态。
 */
baseUtil.JF = function(data) {
	function JtreeArrayOrObject(data, root) {
		if (typeof data === "number" || typeof data === "string" || typeof data === "boolean" || data === null) {
			tree = data;
			return;
		}
		if (data instanceof Array) {
			tree += getStep() + getRootStr(root) + "[" + R;
			for ( var i in data) {
				root = "";
				if (i == 0)
					level++;
				if (typeof data[i] === "string") {
					tree += getStep() + "\"" + data[i] + "\"," + R;
				} else if (typeof data[i] === "number" || typeof data[i] === "boolean") {
					tree += getStep() + "" + data[i] + "," + R;
				} else if (typeof data[i] === "function") {
					tree += getStep() + "\"function()\"," + R;
				} else if (data[i] === null) {
					tree += getStep() + "\"null\"," + R;
				} else if (data[i] instanceof Array) {
					JtreeArrayOrObject(data[i], "");
				} else {
					realJtree(data[i], root);
					level--;
					tree += getStep() + "}," + R;
				}
			}
			if (data.length > 0) {
				tree = tree.substring(0, tree.length - 2) + R;
				level--;
			}
			tree += getStep() + "]," + R;
		} else {
			realJtree(data, root);
			level--;
			tree += getStep() + "}," + R;
		}
	}
	function realJtree(data, root) {
		tree += getStep() + getRootStr(root) + "{" + R;
		var count = 0;
		for ( var ii in data) {
			count++;
			if (count == 1)
				level++;
			var d = data[ii];
			if (typeof d === "string") {
				tree += getStep() + "\"" + ii + "\" : \"" + d + "\"," + R;
			} else if (typeof d === "number" || typeof d === "boolean") {
				tree += getStep() + "\"" + ii + "\" : " + d + "," + R;
			} else if (typeof d === "function") {
				tree += getStep() + "\"" + ii + "\" : \"function()\"," + R;
			} else if (d === null) {
				tree += getStep() + "\"" + ii + "\" : \"null\"," + R;
			} else
				JtreeArrayOrObject(d, ii);
		}
		if (count > 0)
			tree = tree.substring(0, tree.length - 2) + R;
		else
			level++;
	}
	function getStep() {
		var currentStep = "";
		for (var i = 0; i < level; i++) {
			currentStep += oneStep;
		}
		return currentStep;
	}
	function getRootStr(root) {
		var rootStr = "";
		if (root === "") {
			return "";
		} else {
			return "\"" + root + "\" : ";
		}
	}
	var tree = "";
	var R = "\n";
	var oneStep = "     ";
	var level = 0;
	var root = "";
	JtreeArrayOrObject(data, root);
	if (typeof data !== "number" && typeof data !== "string" && typeof data !== "boolean" && data !== null) {
		tree = tree.substring(0, tree.length - 2) + R;
	}
	if (window.console) {
		window.console.info(tree);
	} else {
		console.log(tree);
	}
};

/**
 * json字符串转javaScript对象，因为当我们使用EL表达式获取一个后台的json对象，比如${userJSON}，如果
 * userJSON为空，那么页面是会报错的，但是用字符串圈起来就不会，比如"${userJSON}"，所以使用这个方法，我们
 * 就不需要每次先判断json字符串是否为空。
 * 
 * 要注意的是，当我们的json字符串是从JSTL表达式中获取的，那么调用这个方法的时候，参数要注意用两个单引号括起来，
 * 而不是两个双引号，比如baseUtil.json('${obj}')而不是baseUtil.json("${obj}")
 */
baseUtil.json = function(jsonStr) {
	if (jsonStr) {
		return $.parseJSON(jsonStr);
	}
	return "";
};

/**
 * 和baseUtil.json是一样的，只不过有的时候，我们会将后端的json字符串十六进制编码后传回前端，这样前端用
 * el表达式获取的时候就不会出问题。这种情况我们就需要先解密字符串，然后再还原回对象。
 */
baseUtil.jsonDehex = function(jsonStr) {
	if (jsonStr) {
		jsonStr = baseCode.decodeHex(jsonStr);
		return $.parseJSON(jsonStr);
	}
	return "";
};

/**
 * 简单的封装jquery的ajax的常用的操作，此ajax只提供查询数据使用，不用于保存数据。 保存数据请用saveAJ()方法。
 * 
 * @param url
 *            请求url。
 * @param data
 *            请求参数，javaScript对象。
 * @param success
 *            请求成功回调方法。
 * @param method
 *            GET或POST
 * @param async
 *            true为异步请求，false为同步请求。
 * @param complete
 *            不管请求是否成功都调用，比如网络中断了也会调用，主要获取请求返回的状态码。 默认传入XMLHttpRequest,
 *            textStatus这两个参数 XMLHttpRequest.readyState: 状态码： 0 －
 *            （未初始化）还没有调用send()方法 1 － （载入）已调用send()方法，正在发送请求 2 －
 *            （载入完成）send()方法执行完成，已经接收到全部响应内容 3 － （交互）正在解析响应内容 4 －
 *            （完成）响应内容解析完成，可以在客户端调用了 textStatus可能是"success","timeout", "error",
 *            "notmodified" 和 "parsererror"。
 * @param error
 *            ajax在请求的过程中，会自动判断请求的失败，超时等情况，如果出现这些请求，会调用error方法，传入参数XMLHttpRequest,
 *            textStatus, errorThrown，
 *            具体参考jquery文档。我们自己使用的过程中没有必要判断那么细致，反正就是如果ajax请求失败了需要做些什么，你就把要做的事情放到这个参数方法中。
 */
baseUtil.ajax = null,

/**
 * 点击菜单事件
 */

baseUtil.AJ = function(url, data, success, method, async, complete, error) {
	// 返回一个ajax执行的状态码，必须是同步才有效，作用是告诉外界当前ajax是否执行成功，因为有的时候，弹窗中中保存内容，如果后台抛异常，应该弹出警示的同时，弹窗不关掉，就可以根据这个变量来告诉弹窗不要关掉。
	var isSuccess = true;
	if (baseUtil.ajax) {
		baseUtil.ajax.abort();
	}
	successMethod = function(_data) {
		var succ = common.handleResult(_data);
		if (!succ) {
			isSuccess = false;
			return false;
		}
		if (success) {
			var go = success(_data);
			// 有些请求只是请求数据，不需要弹出“请求成功”，那么在success参数的最后返回false或者不返回任何东西，就会阻止下面的语句执行，只有返回true，才能弹出服务器返回message。
			if (!go) {
				return false;
			}
		}
		if (_data.message) {
			layui.layer.msg(_data.message);
		}
	};
	if (!error) {
		error = function() {
			isSuccess = false;
			layui.layer.msg("异常错误!");
		};
	}
	var completeMethod = function(XMLHttpRequest, status) {
		// 超时,status还有success,error等值的情况
		if (status == 'timeout') {
			layui.layer.msg("链接超时,请重试!");
			if (baseUtil.ajax) {
				baseUtil.ajax.abort();
			}
		} else {
			if (complete) {
				complete(XMLHttpRequest, status);
			}
		}
	};
	baseUtil.ajax = $.ajax({
		async : async,
		type : method,
		dataType : "json", // 不设此参数会导致返回的内容中文乱码
		url : url,
		data : data,
		timeout : 3000,
		success : successMethod,
		error : error,
		fail : function(err, status) {
			layui.layer.msg("异常错误!");
		},
		// 请求完成后最终执行参数
		complete : completeMethod
	});
	return isSuccess;
};

/**
 * GET方式调用baseUtil.AJ，同步请求。
 */
baseUtil.AJget = function(url, data, success, complete, error) {
	return baseUtil.AJ(url, data, success, "GET", false, complete, error);
};

/**
 * POST方式调用baseUtil.AJ，同步请求。
 */
baseUtil.AJpost = function(url, data, success, complete, error) {
	return baseUtil.AJ(url, data, success, "POST", false, complete, error);
};

/**
 * GET方式调用baseUtil.AJ，异步请求。
 */
baseUtil.AJgetAsync = function(url, data, success, complete, error) {
	return baseUtil.AJ(url, data, success, "GET", true, complete, error);
};

/**
 * POST方式调用baseUtil.AJ，异步请求。
 */
baseUtil.AJpostAsync = function(url, data, success, complete, error) {
	return baseUtil.AJ(url, data, success, "POST", true, complete, error);
};

/**
 * 通过直接在dom元素中绑定事件，比如<div onclick="do()"/>的方式得到的event对象的浏览器兼容，得到触发事件的target对象。
 * evt:事件方法的第一个参数。
 */
baseUtil.getTargetByEvent = function(e) {
	e = window.event || e;
	return e.srcElement || e.target;
};

/**
 * 判断是否为火狐浏览器
 */
baseUtil.isFirefox = function() {
	return /firefox/.test(navigator.userAgent.toLowerCase());
};

/**
 * 判断是否为谷歌浏览器
 */
baseUtil.isChrome = function() {
	return /webkit/.test(navigator.userAgent.toLowerCase());
};

/**
 * 判断是否为Opera浏览器
 */
baseUtil.isOpera = function() {
	return /opera/.test(navigator.userAgent.toLowerCase());
};

/**
 * 判断是否为IE浏览器
 */
baseUtil.isIE = function() {
	return /msie/.test(navigator.userAgent.toLowerCase());
};

/**
 * 判断是否为IE6
 */
baseUtil.isIE6 = function() {
	return 'undefined' == typeof (document.body.style.maxHeight);
};

/**
 * 判断是否为IE6-IE8
 */
baseUtil.isIE68 = function() {
	return !$.support.leadingWhitespace;
};

/**
 * 判断是否为IE9
 */
baseUtil.isIE9 = function() {
	return (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.match(/9./i) == "9.");
};

/**
 * 对数字型数组进行升序 可传["3","2","1"]，也可以传[3,2,1] 结果：["1","2","3"]或[1,2,3]
 */
baseUtil.sortArrayASC = function(array) {
	function NumAscSort(a, b) {
		return a - b;
	}
	return array.sort(NumAscSort);
};

/**
 * 对数字型数组进行升序 可传["1","2","3"]，也可以传[1,2,3] 结果["3","2","1"]或[3,2,1]
 */
baseUtil.sortArrayDESC = function(array) {
	function NumDescSort(a, b) {
		return b - a;
	}
	return array.sort(NumDescSort);
};

/**
 * 判断对象不是空，并且不是"undefined"字符串。
 */
baseUtil.isNoEmpty = function(obj) {
	if (obj && "undefined" != obj) {
		return true;
	}
	return false;
};

/**
 * 与baseUtil.isNoEmpty相反
 */
baseUtil.isEmpty = function(obj) {
	return !baseUtil.isNoEmpty(obj);
};

/**
 * 判断字符串中字母的个数
 */
baseUtil.getAlphaCharLength = function(str) {
	var count = 0;
	for (var i = 0; i < str.length; i++) {
		if (str.charCodeAt(i) >= 65 && str.charCodeAt(i) <= 90 || str.charCodeAt(i) >= 97 && str.charCodeAt(i) <= 122) {
			count++;
		}
	}
	return count;
};

/**
 * 判断字符串中数字个数
 */
baseUtil.getNumLength = function(str) {
	var count = 0;
	for (var i = 0; i < str.length; i++) {
		if (str.charCodeAt(i) >= 48 && str.charCodeAt(i) <= 57) {
			count++;
		}
	}
	return count;
};

/**
 * 判断字符串中特殊字符个数。
 */
baseUtil.getSpecialCharLength = function(str) {
	var count = 0;
	for (var i = 0; i < str.length; i++) {
		if (str.charCodeAt(i) < 48 || str.charCodeAt(i) > 57 && str.charCodeAt(i) < 65 || str.charCodeAt(i) > 90 && str.charCodeAt(i) < 97
				|| str.charCodeAt(i) > 122) {
			count++;
		}
	}
	return count;
};

/**
 * 判断字符串是否包含大小写。
 */
baseUtil.containBigAndSmall = function(str) {
	var big = false;
	var small = false;
	for (var i = 0; i < str.length; i++) {
		if (str.charCodeAt(i) >= 65 && str.charCodeAt(i) <= 90) {
			big = true;
			break;
		}
	}
	for (var i = 0; i < str.length; i++) {
		if (str.charCodeAt(i) >= 97 && str.charCodeAt(i) <= 122) {
			small = true;
			break;
		}
	}
	if (big && small) {
		return true;
	}
	return false;
};

/**
 * 和java字符串的endWith一样。
 * 
 * @param s
 * @returns {Boolean}
 */
String.prototype.endWith = function(s) {
	if (!s || !this || s.length > this.length) {
		return false;
	}
	if (this.substring(this.length - s.length) == s) {
		return true;
	} else {
		return false;
	}
};

/**
 * 和java字符串的startWith一样。
 * 
 * @param s
 * @returns {Boolean}
 */
String.prototype.startWith = function(s) {
	if (!s || !this || s.length > this.length) {
		return false;
	}
	if (this.substr(0, s.length) == s) {
		return true;
	} else {
		return false;
	}
};

/**
 * 判断字符串是否匹配某个正则表达式。 rgx：正则表达式，形式为"\\d*"。
 */
String.prototype.mth = function(rgx) {
	if ("" == this.replace(new RegExp(rgx), "")) {
		return true;
	}
	return false;
};

/**
 * 在表单中用回车键提交。
 * 
 * @param areaSelector
 *            在哪个范围下点击回车会触发提交，比如一个页面有多个表单，每个表单都有提交按钮，
 *            我们这里需要指定一个表单范围，或者一个dom节点范围，当光标在这个范围的输入框中，
 *            输入完内容，点击回车，就会触发btnSelector按钮的click事件。
 * @param btnSelector
 *            要触发的按钮，jquery表达式或者jquery对象。
 */
baseUtil.enterToSubmit = function(areaSelector, btnSelector) {
	$(areaSelector).keydown(function(e) {
		var e = e || event, keycode = e.which || e.keyCode;
		if (keycode == 13) {
			$(btnSelector).trigger("click");
		}
	});
};

/**
 * 由于网页加载的过程中，有的时候比较慢，css还没来得及渲染，内容已经出来了，内容散布变形，
 * 等页面加载完成后，页面才恢复正常，所以我们会在这些变形的页面中加display:none，然后最后 才将他们渐现，这样用户体验会好些。
 * 
 * IE8下不支持animo()方法的回调函数。
 * 
 * selection：需要渐现的dom节点的jquery对象或者jquery表达式。 duration：（单位：秒）动画持续时间，不输入默认为1秒
 * callback:回调函数
 */
baseUtil.fadeIn = function(selection, duration, callback) {
	if (!duration) {
		duration = 1;
	}
	$(selection).show();
	$(selection).css("visibility", "visible");
	$(selection).animo({
		animation : "fadeIn",
		duration : duration,
		keep : false
	});
	if (callback) {
		setTimeout(callback, duration * 1000);
	}
};

/**
 * 与baseUtil.fadeIn()相反
 */
baseUtil.fadeOut = function(selection, duration, callback) {
	if (!duration) {
		duration = 1;
	}
	$(selection).animo({
		animation : "fadeOut",
		duration : duration,
		keep : false
	});
	setTimeout(function() {
		$(selection).hide();
		if (callback) {
			callback();
		}
	}, duration * 1000);
};

/**
 * 让滚动条滚动到指定目标。
 * 
 * @param scrollContext
 *            滚动条所在的节点的jquery对象或者表达式或dom
 * @param scrollTarget
 *            滚动到目标节点的jquery对象或者表达式或dom
 * @param during
 *            滚动历时，默认0毫秒
 */
baseUtil.scrollTo = function(scrollContext, scrollTarget, during) {

	// alert("滚动目标相对于浏览器top："
	// + $(scrollTarget).offset().top
	// + " - 滚动窗口相对于浏览器top："
	// + $(scrollContext).offset().top
	// + " + 滚动条距离："
	// + $(scrollContext).scrollTop()
	// + " = 滚动条位置："
	// + ($(scrollTarget).offset().top - $(scrollContext).offset().top + $(
	// scrollContext).scrollTop()) * 1);

	if (!during) {
		during = 0;
	}
	$(scrollContext).animate({
		scrollTop : ($(scrollTarget).offset().top - $(scrollContext).offset().top + $(scrollContext).scrollTop()) * 1
	}, during);
};

/**
 * 让滚动条滚动到指定目标。
 * 当滚动条所在的dom是html节点，也就是滚动条属于整个浏览器窗口时使用这个方法，此时的scrollContext必须是$("html")
 * 
 * @param scrollTarget
 *            滚动到目标节点的jquery对象或者表达式或dom
 * @param during
 *            滚动历时，默认0毫秒
 */
baseUtil.scrollToInHtmlTag = function(scrollTarget, during) {

	// alert("滚动目标相对于浏览器top：" + $(scrollTarget).offset().top + " = 滚动条位置");

	if (!during) {
		during = 0;
	}
	$("html").animate({
		scrollTop : $(scrollTarget).offset().top * 1
	}, during);
};

/**
 * 回到页面顶部
 * 
 * @param during
 *            滚动历时，默认0毫秒
 */
baseUtil.scrollTop = function(during) {
	if (!during) {
		during = 0;
	}
	$("html, body").animate({
		scrollTop : 0
	}, during);
}

/**
 * 点击某个dom，回到页面顶部
 * 
 * @param selection
 *            被点击的dom的jquery对象或者表达式或者dom
 * @param showDistance
 *            滚动条滑动到什么距离才显示被点击的dom（同时少于这个距离也会隐藏该dom)
 * @param during
 *            滚动到顶部历时，默认0毫秒
 */
baseUtil.clickToTop = function(selection, showDistance, during) {
	$(window).scroll(function() {
		if ($(window).scrollTop() > 200 && $(selection).is(":hidden")) {
			baseUtil.fadeIn($(selection), 0.4);
		} else if ($(window).scrollTop() <= 200 && $(selection).is(":visible")) {
			$(selection).hide();
		}
	})
	$(selection).click(function() {
		baseUtil.scrollTop(showDistance);
	})
}
