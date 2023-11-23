var value = 0;

$(function() {
	$("#startDeamon").click(function(){
		var time = new Date().getTime();
		var delay = $("input[name='startTime']").val();
		var stopTime = $("input[name='stopTime']").val();
		
		stopTime = stopTime == -1 ? -1 : Number(time) + Number(stopTime);
		
		$.ajax({
			url:"deamon",
			type:"POST",
			data:{
				"deamonMode":"BACKGROUND",
				"param":$("input[name='param']").val(),
				"startTime":Number(time) + Number(delay),
				"stopTime":stopTime,
				"interval":$("input[name='interval']").val()
			},
			success:function(data){
				alert(data.msg);
			}
		})
	});
	$("#stopDeamon").click(function(){
		$.ajax({
			url:"deamon",
			type:"DELETE",
			success:function(data){
				alert(data.msg);
			}
		})
	});
});

function declarative(action) {
	$.get(actionUrl, {
		action : action,
		returnType : "1"
	}, function(data) {
		alert(data);
	});
}


function getDBConn() {
	$.get(actionUrl, {
		action : "getDBConn",
		returnType : "1"
	}, function(data) {
		alert(data);
	});
}

function getMongoClient() {
	$.get(actionUrl, {
		action : "getMongoClient",
		returnType : "1"
	}, function(data) {
		alert(data);
	});
}

function getConfig() {
	$.get(actionUrl, {
		action : "getConfig",
		returnType : "1"
	}, function(data) {
		alert(data);
	});
}

function readFile() {
	$.getJSON(actionUrl, {
		action : "readFile",
		returnType : "1"
	}, function(data) {
		alert(data);
	});
}

function invokeToolByID(){
	$.get(actionUrl, {
		action : "invokeToolByID"
	}, function(data) {
		alert(data);
	});
}

function invokeToolByAlias(){
	$.get(actionUrl, {
		action : "invokeToolByAlias"
	}, function(data) {
		alert(data);
	});
}
