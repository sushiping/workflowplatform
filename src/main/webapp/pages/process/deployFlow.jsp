<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/core/core.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>众齐工作流演示平台</title>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" style="color: #fff" href="#">众齐工作流演示平台</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav navbar-right">
            	 <li>
            		<a style="color: #fff" href="#"><i class="glyphicon glyphicon-user"></i>&nbsp;[<sec:authentication property="principal.orgObj.name" />]-<sec:authentication property="principal.username" />&nbsp;欢迎您！</a>
            	 </li>
                <li><a href="javascript:void(0)" onclick="logout()">退出</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="container-fluid">
    <div class="row">
        <div class="col-sm-3 col-md-2 sidebar">
            <ul class="nav nav-sidebar">
                <li><a href="${ctx}/process/preShowFlowDefine.action">流程定义</a></li>
                <li class="active"><a href="${ctx}/process/preDeployFlow.action">流程发布<span
                        class="sr-only">(current)</span></a></li>
                <li><a href="${ctx}/process/preStartFlow.action">流程启动</a></li>
            </ul>
            <ul class="nav nav-sidebar">
                <li><a href="#">待办列表</a></li>
                <li><a href="#">已办列表</a></li>
            </ul>
        </div>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
            <h2 class="sub-header">流程发布</h2>
            <!-- 
            <div style="text-align: right;">
            	<button type="button" class="btn btn-primary">发布</button>
            </div>
            <hr style="margin-top: 10px;"/>
            -->
		      <div class="row">
					  <div class="col-xs-2">
					    	<input type="text" class="form-control" id="processName_search" placeholder="流程名称">
					  </div>
					  <button id="search" class="btn btn-default" type="button">查询</button>
		      </div>
            <div class="table-responsive" style="margin-top: 10px;">
                <div id="gridContainerDeploy" class="dlshouwen-grid-container"></div>
                <div id="gridToolBarContainerDeploy" class="dlshouwen-grid-toolbar-container"></div>
            </div>
        </div>
    </div>
</div>

<!-- 版本管理modal -->
<div class="modal fade bs-example-modal-lg" id="versionModal" tabindex="-1" role="dialog" aria-labelledby="versionModalLabel">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="versionModalLabel">流程版本</h4>
      </div>
      <!-- 
      <div style="text-align: right;padding-right: 15px;padding-top: 10px;">
            <button type="button" class="btn btn-xs btn-primary" onclick="preShowDetailModal()">新建流程版本</button>
            <button type="button" class="btn btn-xs btn-primary" onclick="deleteDetailVersion()">删除流程版本</button>
      </div>
       -->
      <div class="modal-body">
      		<!-- <hr style="margin-top: 0px;"/>-->
		      <div class="row">
					  <div class="col-xs-2 input-group-sm">
					    	<input type="text" class="form-control input-sm" id="detailName_search" placeholder="流程名称">
					  </div>
					  <button id="search_detail" class="btn btn-sm btn-default" type="button">查询</button>
		      </div>
      		<div class="table-responsive" style="margin-top: 10px;">
      			 <input type="hidden" id="defineId">
                <div id="gridContainerVersion" class="dlshouwen-grid-container"></div>
                <div id="gridToolBarContainerVersion" class="dlshouwen-grid-toolbar-container"></div>
            </div>
      </div>
      <div class="modal-footer"></div>
    </div>
  </div>
</div>
<script type="text/javascript" src="${ctx}/core/js/deployFlow.js"></script>
</body>
</html>