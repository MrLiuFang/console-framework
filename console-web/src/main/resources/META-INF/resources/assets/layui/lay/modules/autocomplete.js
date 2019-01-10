/**
 * Created by websir on 2018/1/24.
 */
layui.define(function(exports) {

	var autocomplete = {
		v : "1.0.0",
		init : function(config) {
			var AT = new Class(config);
			AT.init();
			return AT;
		}
	};

	var Class = function(config) {
		var $ul = $('<ul class="autocomplate-list layui-hide"></ul>');
		this.config = $.extend({
			textLength : 3,// 触发长度
			delay : 300
		// 触发延迟
		}, config);
		config.elem.after($ul);
		this.$ul = config.elem.siblings(".autocomplate-list");
		// 为控件插入一个隐藏域做提交内容。
		config.elem.attr("autocomplete", "off");
		config.elem.after("<input type='hidden' id='" + config.submitName
				+ "' name='" + config.submitName + "' />");
	};

	// 定位
	Class.prototype.offset = function() {
		var that = this;
		var offset = that.$elem.offset();
		that.$ul.css({
			position : "absolute",
			width : that.$elem.outerWidth()
		});
	};

	// 初始化
	Class.prototype.init = function() {
		var that = this;
		that.$elem = $(that.config.elem);
		var $elem = that.$elem;
		var $ul = that.$ul;
		var longKeyDownTimer = null;// 定时器
		var longKey = false;
		$elem.on("keyup", function(e) {
			longKey = false;
			clearInterval(longKeyDownTimer);
			if (e.keyCode == 38 || e.keyCode == 40) {
				e.preventDefault();
			}
		});

		// 向上选择
		function up() {
			if (!$ul.find(".active").length
					|| $ul.find(".active").prev().length == 0) {
				$ul.find("li:last").addClass("active").siblings().removeClass(
						"active");
				return false;
			}
			$ul.find(".active").prev().addClass("active").siblings()
					.removeClass("active");
		}

		// 向下选择
		function down() {
			if (!$ul.find(".active").length
					|| $ul.find(".active").next().length == 0) {
				$ul.find("li:first").addClass("active").siblings().removeClass(
						"active");
				return false;
			}
			$ul.find(".active").next().addClass("active").siblings()
					.removeClass("active");
		}

		// 用户输入/键盘选择
		$elem.on("keyup", function(e) {
			e.preventDefault();
			if (e.keyCode == 38) {// 向上
				up();
				return false;
			}
			if (e.keyCode == 40) {// 向下
				down();
				return false;
			}
			if (e.keyCode == 13) {// 回车
				$ul.find(".active").trigger("click");
				return false;
			}
			var val = $(this).val();
			if (val && val.length >= that.config.textLength) { // 大于指定的长度开始查找
				// 调用数据
				clearTimeout(that.delayTimer);
				that.delayTimer = setTimeout(function() {
					that.config.callback.data(val, function(data) {
						if (!data || data.length == 0) {
							$ul.addClass("layui-hide");
							return false;
						}
						that.data = data;
						var tpl = "";
						data.map(function(item, index) {
							tpl += '<li data-id="' + index + '" data-value="'
									+ item.value + '">' + item.label + '</li>'
						});
						$ul.html(tpl);
						that.offset();
						$ul.removeClass("layui-hide");
						that.bindDom();
						// 当下拉出来后，用键盘上下键选择选项并按回车确定选项时，光标还在input框上，此时按回车会触发表单提交事件，这种情况应阻止表单提交事件，否则会导致回车无法选定选项。
						that.config.elem.bind("keydown", function() {
							if (event.keyCode == 13) {
								return false;
							}
						});
					});
				}, that.config.delay);
			} else {
				$ul.addClass("layui-hide");
			}
		});

		$elem.on("blur", function() {
			setTimeout(function() {
				$ul.addClass("layui-hide");
				// 当下拉条消失，使用完毕后，恢复元素的回车提交表单事件。
				that.config.elem.unbind("keydown");
			}, 200)
		})
	};

	// 绑定事件
	Class.prototype.bindDom = function() {
		var that = this;
		var $ul = that.$ul;
		$ul.on("mouseenter", "li", liHover);
		$ul.on("click", "li", liClick);

		function liClick(e) {
			var callbackData = that.data[$(this).data("id")];
			if (that.config.callback.selected) {
				that.config.callback.selected(callbackData);
			}
			that.$elem.val(callbackData.label);
			$("#" + that.config.submitName).val(callbackData.value);
			that.$ul.addClass("layui-hide");
			e.stopPropagation();
			// 当下拉条消失，使用完毕后，恢复元素的回车提交表单事件。
			that.config.elem.unbind("keydown");
		}

		function liHover() {
			$(this).addClass("active").siblings().removeClass("active");
		}

	};

	exports('autocomplete', autocomplete);
});