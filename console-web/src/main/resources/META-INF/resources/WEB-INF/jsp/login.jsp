<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html class="" lang="zh-cn">
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="icon" href="${ctx }/assets/img/title.ico" type="image/x-icon" />
<link rel="shortcut icon" href="${ctx }/assets/img/title.ico"
	type="image/x-icon" />
<title>${title}</title>
<link rel="stylesheet" href="${ctx}/assets/login/icon/style.css">
<link rel="stylesheet" href="${ctx}/assets/login/login.css">

<style type="text/css">
.checkbox:checked {
	background: url('${ctx}/assets/login/imgs/gou.png');
}
</style>
</head>

<script src="${ctx}/assets/js/jquery/jquery-3.2.1.min.js"></script>
<script src="${ctx}/assets/layer/layer.js"></script>
<script src="${ctx}/assets/js/jquery/jquery.cookie.js"></script>
<script src="${ctx}/assets/js/jquery/md5.js"></script>
<script src="${ctx}/assets/js/jquery/jquery.backstretch.min.js"
	type="text/javascript"></script>
<body>
	<div class="box">
		<footer class="header"></footer>
		<form class="login-form" method="post" id="loginForm"
			action="${ctx}/admin/login">
			<section class="login">
				<div class='message-top'>
					<img alt="" src="${ctx}/assets/img/login-logo.png">
				</div>
				<div class="login-title">账号登录</div>
				<div class="login-name login-input">
					<span class="login-icon icon-name" id="icon-name"></span><input
						id="account" autocomplete="off" name="account" class="input"
						type="text" placeholder="请输入账号名" />
				</div>
				<div class="login-password login-input">
					<span class="login-icon icon-password" id="icon-pws"></span><input
						autocomplete="off" class="input" id="password" name="password"
						type="password" placeholder="请输入密码" />
				</div>
				<div class="login-code login-input">
					<span class="login-icon icon-code" id="icon-code"></span><input
						maxlength=6 autocomplete="off" class="input input-code" id="vcode"
						name="vcode" type="text" placeholder="请输入验证码" /> <img
						class="imgs-code" src="data:image/png;base64,${vcodeImage }"
						id="vcode-image" />
				</div>
				<div class="login-code login-input">
					<input class="checkbox" type="checkbox" id="rememberMe" /><label
						class="checkbox-text" for="rememberMe">记住密码</label>
				</div>
				<div class="login-button" id="forbidden">登录</div>
				<footer class="footer">
					<div class="copyright">
						Copyright © 2018 <a href="https://www.qi-cloud.com/">Qi-cloud</a>
						All rights reserved.
					</div>
				</footer>
			</section>
		</form>
	</div>
</body>

<script type="text/javascript">
	$(function() {

		//单个输入框验证
		$(".input:not([type=checkbox])").blur(function() {
			if (!this.value) {
				$(this).addClass("input-error");
			} else {
				$(this).removeClass("input-error");
			}
		});

		var cookieParams1 = {
			path : "${ctx}" + "/admin/"
		}
		var cookieParams2 = {
			path : "${ctx}" + "/"
		}
		if ($.cookie("rem_name") && $.cookie("rem_pwd")) {
			$("#account").val($.cookie("rem_name"));
			$("#password").val($.cookie("rem_pwd"));
			$("#rememberMe")[0].checked = true;
		}
		$("#forbidden").click(function() {
			$(".input:not([type=checkbox])").blur();
			if ($(".input-error").length == 0) {
				if ($("#rememberMe").is(":checked")) {
					$.cookie("rem_name", $("#account").val(), cookieParams1);
					$.cookie("rem_pwd", $("#password").val(), cookieParams1);
					$.cookie("rem_name", $("#account").val(), cookieParams2);
					$.cookie("rem_pwd", $("#password").val(), cookieParams2);
				} else {
					$.cookie("rem_name", "", cookieParams1);
					$.cookie("rem_pwd", "", cookieParams1);
					$.cookie("rem_name", "", cookieParams2);
					$.cookie("rem_pwd", "", cookieParams2);
				}
				$("#password").val($.md5($("#password").val()));
				$("#loginForm").submit();
			}
		});

		//背景
		$.backstretch([ "${ctx}/assets/img/3.jpg", "${ctx}/assets/img/2.jpg", "${ctx}/assets/img/1.jpg" ], {
			fade : 1000,
			duration : 8000
		});

		var colors = {
			"0" : "10px 5px 70px #0d957a",
			"1" : "10px 5px 70px #ABB3A6",
			"2" : "10px 5px 70px #002842",
		};
		$(window).on("backstretch.before", function(e, instance, index) {
			$(".login").css("box-shadow", colors[index]);
		});

		//显示后台校验错误
		if (($.trim('${message }'))) {
			//由于layer.msg在火狐下，宽度有点兼容问题，可能是加载顺序导致，因此做一定延迟
			setTimeout(function() {
				layer.msg('${message }')
			}, 30)
		}

		//刷新验证码
		$("#vcode-image").click(function() {
			var that = $(this);
			$.ajax({
				type : "POST",
				url : "${ctx}/admin/flashVCode",
				data : {},
				success : function(data) {
					that.attr("src", "data:image/png;base64," + data.data.vcodeImage);
				}
			});
		});
	});
</script>
</html>