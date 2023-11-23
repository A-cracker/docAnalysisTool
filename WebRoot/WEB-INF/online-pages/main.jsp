<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no,minimal-ui">
<meta name="screen-orientation" content="portrait">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<meta name="full-screen" content="yes">
<meta name="x5-fullscreen" content="true">
<link rel="stylesheet"
	href="https://cdn.bootcss.com/material-design-icons/3.0.1/iconfont/material-icons.css">
<title></title>
<style>
html {
	width: 100%;
	height: 100%;
}

body {
	width: 100%;
	height: 100%;
}

#app {
	width: 100%;
	height: 100%;
}
</style>
<link href="${resourceUrl}/css/main.css" rel="stylesheet">
</head>
<body>
	<iframe src="http://lsjz.zfcxjst.gd.gov.cn/lsjz-ui/#/"></iframe>
	<script type="text/javascript">
		window.g_runToolUrl = '${actionUrl}'; // 数据接口
		window.g_callToolUrl = '${runToolUrl}'; // 工具调用
		window.g_forwardUrl = '${forwardUrl}'; // 跳转接口
		window.g_resourceUrl = '${resourceUrl}'; // 静态资源
		window.g_userId = '${userID}'; // 当前用户账号
		window.g_accessToken = '${accessToken}'; // 当前用户的accessToken
		window.g_rtParam = '${toolParam}'; // 工具运行参数
		window.g_bandId = '${toolParam}'; // 当前运行的帮区ID
		window.g_clientType = '${clientType}'; // 工具当前的运行平台
		window.g_thisToolId = '${toolID}'; // 工具的ID
		window.g_coreUrl = '${openInterfaceUrl}';
		window.g_userAccount = '${userAccount}';
		window.g_userName = '${userName}';
		window.g_aid = '21419389378723787447';
	</script>
</body>
</html>
