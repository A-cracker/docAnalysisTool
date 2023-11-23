<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>

<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>Webpack开发工具</title>
	<meta name="viewport" content="initial-scale=1, maximum-scale=1">
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<style type="text/css">
		body {
		    width: 100vw;
		    height: 100vh;
		    overflow: hidden;
		    margin:0;
		}
		iframe {
			width: 100%;
			height:100%;
			background: white;
			border: 0;
		}
	</style>
	
</head>
<body>
	<iframe id="iframe" src=""></iframe>	

</body>
<script type="text/javascript">
	/* var iframe = document.getElementById("iframe");
	var params = '&actionUrl=${actionUrl}' +
				'&runToolUrl=${runToolUrl}' +
				'&forwardUrl=${forwardUrl}' +
				'&resourceUrl=${resourceUrl}' +
				'&coreUrl=${openInterfaceUrl}' +
				'&param=${param}'
				
	iframe.src = 'http://127.0.0.1:8000?userID=${userID}' +
						'&accessToken=${accessToken}' +
						'&bandID=${bandID}' +
						'&clientType=${clientType}' +
						'&toolID=${toolID}' +
						'&aid=21419389378723787447' + encodeURIComponent(params); */
	window.g_actToolUrl = '${actionUrl}'; // 数据接口
	window.g_runToolUrl = '${runToolUrl}'; // 工具调用
	window.g_forwardUrl = '${forwardUrl}'; // 跳转接口
	window.g_resourceUrl = '${resourceUrl}'; // 静态资源
	window.g_userId = '${userID}'; // 当前用户账号
	window.g_accessToken = '${accessToken}'; // 当前用户的accessToken
	window.g_bandId = '${bandID}'; // 当前运行的帮区ID
	window.g_param = '${param}'; // 工具运行参数
	window.g_rtParam = '${toolParam}'; // 工具运行参数
	window.g_clientType = '${clientType}'; // 工具当前的运行平台
	window.g_toolId = '${toolID}'; // 工具的ID
    window.g_coreUrl = '${openInterfaceUrl}'; //开放接口
    window.g_aid = '21419389378723787447';
    //加载iframe
    var iframe = document.getElementById("iframe");
	iframe.allow = "fullscreen"
    iframe.src = "http://localhost:8000";
    if (iframe.attachEvent){
        iframe.attachEvent("onload", function(){
        	postData();
        });
    } else {
        iframe.onload = function(){
        	postData();
        };
    }
    function postData(){
        document.getElementById("iframe").contentWindow.postMessage({
        	"actionUrl":window.g_actToolUrl,
        	"runToolUrl":window.g_runToolUrl,
        	"forwardUrl":window.g_forwardUrl,
        	"resourceUrl":window.g_resourceUrl,
        	"userID":window.g_userId,
        	"accessToken":window.g_accessToken,
        	"bandID":window.g_bandId,
        	"param":window.g_param,
        	"rtParam":window.g_rtParam,
        	"clientType":window.g_clientType,
        	"toolID":window.g_toolId,
        	"coreUrl":window.g_coreUrl,
        	"aid":window.g_aid,
        },"*")
    }
</script>
</html>
