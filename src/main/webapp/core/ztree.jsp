<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/core/core.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" href="${ctx }/core/ztree/metroStyle/metroStyle.css" />
</head>
<body style="padding-top: 0px;">
<div style="overflow: auto;">
	<ul id="roleGroupTree" class="ztree"></ul>
</div>
<script type="text/javascript" src="${ctx }/core/ztree/jquery.ztree.all.min.js"></script>
<script type="text/javascript" src="${ctx }/core/ztree/ztreeSetting.js?1"></script>
<script type="text/javascript">
var checkTree;
var nodes = new Array();
var rootNode = {
		'id':-1,
		'pId':0,
		'name':'候选组',
		open:true,
		nocheck:true,
		isParent:1
	};
nodes.push(rootNode);
$.ajax({
	url :  '/workflow/usercenter/role/findRoleGroup.action',
	type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
	traditional:true,
	async : false, // 默认值: true。默认设置下，所有请求均为异步请求
	dataType : 'json', // 预期服务器返回的数据类型
	success:function(data){
		var roles = data.roles;
		for(var i=0;i<roles.length;i++){
			var node = roles[i];
			tree.translateNodesForCheck(node,nodes);
		}
		checkTree = tree.initZtreeForCheck('roleGroupTree',nodes,'',false)
	},
	error:function(data){
		alert("系统请求失败！");
	}
})
</script>
</body>
</html>