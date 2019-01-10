<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<link rel="icon" href="${ctx }/assets/imgs/title.ico"
	type="image/x-icon" />
<link rel="shortcut icon" href="${ctx }/assets/imgs/title.ico"
	type="image/x-icon" />
<title>${title}</title>
<jsp:include page="css.jsp"></jsp:include>

</head>
<body class="layui-layout-body">
	<div class="layui-layout layui-layout-admin">
		<jsp:include page="top.jsp"></jsp:include>
		<jsp:include page="left.jsp"></jsp:include>
		<jsp:include page="right.jsp"></jsp:include>
	</div>
</body>
<jsp:include page="js.jsp"></jsp:include>
<jsp:include page="upload.jsp"></jsp:include>
<jsp:include page="regionUtil.jsp"></jsp:include>
</html>