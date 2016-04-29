var processGridColumns = [
	{id:'operation', title:'操作', type:'string', columnClass:'text-center', resolution:function(value, record, column, grid, dataNo, columnNo){
	        var content = '';
	        content += '<button title="编辑" class="btn btn-xs btn-default" onclick="preModifyProcess('+record.id+');"><i class="fa fa-edit"></i> 编辑</button>';
	        content += '  ';
	        content += '<button title="版本管理" class="btn btn-xs btn-primary" onclick="preVersionManager('+record.id+');"><i class="glyphicon glyphicon-list"></i> 版本</button>';
	        content += '  ';
	        content += '<button title="删除流程" class="btn btn-xs btn-danger" onclick="deleteProcess('+record.id+');"><i class="fa fa-trash-o"></i> 删除</button>';
	        return content;
	 }},
	{id:'id', title:'记录Id', type:'number', columnClass:'text-center', hide:true},
    {id: 'processName', title: '流程名称', type: 'string', columnClass: 'text-center'},
    {id: 'key', title: '流程标识', type: 'string', columnClass: 'text-center'},
    {id: 'd', title: '流程描述', type: 'string', columnClass: 'text-center'},
    {id: 'version', title: '当前版本', type: 'number', format: '#,###.0', columnClass: 'text-center'},
    {id: 'status', title: '状态', type: 'number', columnClass: 'text-center',resolution:function(value, record, column, grid, dataNo, columnNo){
    	var content = '未发布';
    	if(value == 1)
    		content = '已发布';
    	return content;
    }}
];
var processGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    check : true,
    loadURL: ctx + '/process/processShowFlowDefine.action',
    exportFileName: '用户列表',
    columns: processGridColumns,
    gridContainer: 'gridContainer_2_1_1',
    toolbarContainer: 'gridToolBarContainer_2_1_1',
    tools: '',
    pageSize: 15,
    pageSizeLimit: '15',
    onRowClick : function(value, record, column, grid, dataNo, columnNo, cell, row, extraCell, e){
    	if(column.id == 'operation' || column.title == '操作') {
    		var targetElObj = $(e.target);
			if (targetElObj.hasClass('list-edit-btn')){
				addwork.updateaddworkUI(targetElObj.data('record'));
			}
			return;
    	}
    	var $checkBox = row.find("input[type='checkbox']");
    	var status = $checkBox.prop("checked");
    	$checkBox.prop("checked", !status);
    }
};
var processGrid = $.fn.dlshouwen.grid.init(processGridOption);
$(function () {
	processGrid.load();
	$('#search').click(search);
});
//自定义查询
function search(){
    var pName = $('#processName_search').val();
    processGrid.parameters = new Object();
    processGrid.parameters['pName'] = pName;
    //第二个参数用于表示是否为查询
    processGrid.refresh(true, true);
}
//弹出新增流程界面
function showSaveFlowModal(){
	$(".form-horizontal .form-control").val('');
	$('#saveProcessName').tooltip('destroy')
	$('#processNameGroup').removeClass('form-group has-error').addClass('form-group')
	$('#saveProcessKey').tooltip('destroy')
	$('#processKeyGroup').removeClass('form-group has-error').addClass('form-group')
	$('#saveOrUpdate_btn').off('click');
	$('#saveOrUpdate_btn').on('click',saveProcess);
	$('#oldProcessId').val('');
	var options = {}
	options.backdrop = 'static';
	$('#myModal').modal(options)
}
//该方法用于弹出编辑界面相关方法
function showUpdateFlowModal(define){
	$(".form-horizontal .form-control").val('');
	$('#saveProcessName').tooltip('destroy')
	$('#processNameGroup').removeClass('form-group has-error').addClass('form-group')
	$('#saveProcessKey').tooltip('destroy')
	$('#processKeyGroup').removeClass('form-group has-error').addClass('form-group')
	$('#saveProcessName').val(define.processName);
	$('#saveProcessKey').val(define.key);
	$('#saveProcessDes').val(define.description==null ? '' : define.description);
	$('#myModalLabel').html('编辑流程');
	$('#oldProcessId').val(define.id);
	$('#saveOrUpdate_btn').off('click');
	$('#saveOrUpdate_btn').on('click',realUpdateProcess);
	var options = {}
	options.backdrop = 'static';
	$('#myModal').modal(options)
}
//该方法用于保存流程定义
function saveProcess() {
	var processName = $('#saveProcessName').val();
	var processKey = $('#saveProcessKey').val();
	var description = $('#saveProcessDes').val();
	var options = {}
	if(processName == '') {		
		options.placement = 'top';
		options.title = '流程名称不能为空'
		$('#saveProcessName').tooltip(options)
		$('#processNameGroup').removeClass('form-group').addClass('form-group has-error')
		return false;
	}else{
		$('#saveProcessName').tooltip('destroy')
		$('#processNameGroup').removeClass('form-group has-error').addClass('form-group')
	}
	if(processKey == '') {		
		options.placement = 'top';
		options.title = '流程标识不能为空'
		$('#saveProcessKey').tooltip(options)
		$('#processKeyGroup').removeClass('form-group').addClass('form-group has-error')
		return false;
	}else{
		$('#saveProcessKey').tooltip('destroy')
		$('#processKeyGroup').removeClass('form-group has-error').addClass('form-group')
	}
	$.ajax({
		url : ctx + '/process/saveFlowDefine.action',
		type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
		async : false, // 默认值: true。默认设置下，所有请求均为异步请求
		dataType : 'json', // 预期服务器返回的数据类型
		data : {processName : processName, processKey : processKey, description : description},
		success : function(json) {
			if (json && json.state) {
				alert(json.info);
				if(json.state === 'success'){
					$('#myModal').modal('hide');
					search();
					window.open(ctx+'/modeler.html?modelId='+json.modelId,'activitiDesigner');
				}
			}
		},
		error : function(json) {// 请求失败时调用此函数。
			alert("系统请求失败！");
		}
	})
}

/**
 * 该方法用于更新相关记录预览
 * @param id
 */
function preModifyProcess(id) {
	$.ajax({
		url : ctx + '/process/preUpdateFlowDefine.action',
		type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
		async : false, // 默认值: true。默认设置下，所有请求均为异步请求
		dataType : 'json', // 预期服务器返回的数据类型
		data : {defineId : id},
		success : function(json) {
			if (json && json.state) {
				if(json.state === 'success') {
					var define = json.define;
					showUpdateFlowModal(define);
				}else{
					alert('获取记录失败，请稍后重试');
				}
			}
		},
		error : function(json) {// 请求失败时调用此函数。
			alert("无相关记录，请刷新后重试");
		}
	})
}
/**
 * 更新流程方法
 * @returns {Boolean}
 */
function realUpdateProcess(){
	var processName = $('#saveProcessName').val();
	var processKey = $('#saveProcessKey').val();
	var description = $('#saveProcessDes').val();
	var oldId = $('#oldProcessId').val();
	var options = {}
	if(processName == '') {		
		options.placement = 'top';
		options.title = '流程名称不能为空'
		$('#saveProcessName').tooltip(options)
		$('#processNameGroup').removeClass('form-group').addClass('form-group has-error')
		return false;
	}else{
		$('#saveProcessName').tooltip('destroy')
		$('#processNameGroup').removeClass('form-group has-error').addClass('form-group')
	}
	if(processKey == '') {		
		options.placement = 'top';
		options.title = '流程标识不能为空'
		$('#saveProcessKey').tooltip(options)
		$('#processKeyGroup').removeClass('form-group').addClass('form-group has-error')
		return false;
	}else{
		$('#saveProcessKey').tooltip('destroy')
		$('#processKeyGroup').removeClass('form-group has-error').addClass('form-group')
	}
	$.ajax({
		url : ctx + '/process/updateFlowDefine.action',
		type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
		async : false, // 默认值: true。默认设置下，所有请求均为异步请求
		dataType : 'json', // 预期服务器返回的数据类型
		data : {defineId : oldId, processName : processName, processKey : processKey, description : description},
		success : function(json) {
			if (json && json.state) {
				alert(json.info);
				if(json.state === 'success'){
					$('#myModal').modal('hide');
					search();
				}
			}
		},
		error : function(json) {// 请求失败时调用此函数。
			alert("系统请求失败！");
		}
	})
}
/**
 * 该方法用于删除流程定义
 * @param id
 */
function deleteProcess(id) {
	if(confirm('确认删除该条流程定义吗？')) {
		var ids = new Array();
		ids.push(id);
		$.ajax({
			url : ctx + '/process/deleteFlowDefine.action',
			type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
			traditional:true,
			async : false, // 默认值: true。默认设置下，所有请求均为异步请求
			dataType : 'json', // 预期服务器返回的数据类型
			data : {defineIds : ids},
			success : function(json) {
				if (json && json.state) {
					alert(json.info);
					if(json.state === 'success'){
						search();
					}
				}
			},
			error : function(json) {// 请求失败时调用此函数。
				alert("系统请求失败！");
			}
		})
	}
}

document.onkeydown = function(event) {
	e = event ? event : (window.event ? window.event : null);
	if (e.keyCode == 13) {
		//先判断一下小层是否打开
		if(document.getElementById('myModal').style.display != 'block' && document.getElementById('versionModal').style.display != 'block') {
			search();
		} else if(document.getElementById('versionModal').style.display == 'block') {
			searchDetail();
		}
	} else
		return;
}
/**
 * 该方法用于批量删除流程定义
 */
function deleteProcessFlows(){
	var records = processGrid.getCheckedRecords();
	var size = records.length;
	if(size == 0)  {
		alert('请至少选择一条记录');
		return;
	}
	if(confirm('确认删除选中的流程定义吗？')){
		var ids = [];
		for(var i=0; i<size; i++) {
			ids.push(records[i].id);
		}
		$.ajax({
			url : ctx + '/process/deleteFlowDefine.action',
			type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
			traditional:true,
			async : false, // 默认值: true。默认设置下，所有请求均为异步请求
			dataType : 'json', // 预期服务器返回的数据类型
			data : {defineIds : ids},
			success : function(json) {
				if (json && json.state) {
					alert(json.info);
					if(json.state === 'success'){
						search();
					}
				}
			},
			error : function(json) {// 请求失败时调用此函数。
				alert("系统请求失败！");
			}
		})
	}
}

var processDetailGridColumns = [
{id:'operation', title:'操作', type:'string', columnClass:'text-center', resolution:function(value, record, column, grid, dataNo, columnNo){
        var content = '';
        content += '<button title="编辑" class="btn btn-xs btn-default" onclick="preModifyProcessVersion('+record.id+');"><i class="fa fa-edit"></i> 编辑</button>';
        content += '  ';
        content += '<button title="流程发布" class="btn btn-xs btn-success" onclick="deployProcess('+record.modelId+');"><i class="glyphicon glyphicon-ok"></i> 发布</button>';
        content += '  ';
        content += '<button title="设计流程" class="btn btn-xs btn-primary" onclick="preShowProcessDefine('+record.modelId+');"><i class="glyphicon glyphicon-pencil"></i> 设计</button>';
        content += '  ';
        content += '<button title="删除流程" class="btn btn-xs btn-danger" onclick="deleteProcessVersion('+record.id+');"><i class="fa fa-trash-o"></i> 删除</button>';
        return content;
 }},
  {id:'id', title:'记录Id', type:'number', columnClass:'text-center', hide:true},
  {id:'modelId', title:'模板Id', type:'number', columnClass:'text-center', hide:true},
  {id: 'detailName', title: '流程名称', type: 'string', columnClass: 'text-center'},
//  {id: 'key', title: '流程版本标识', type: 'string', columnClass: 'text-center'},
  {id: 'd', title: '版本描述', type: 'string', columnClass: 'text-center'},
  {id: 'version', title: '版本', type: 'number', format: '#,###.0', columnClass: 'text-center'},
  {id: 'status', title: '状态', type: 'number', columnClass: 'text-center',resolution:function(value, record, column, grid, dataNo, columnNo){
  	var content = '未发布';
  	if(value == 1)
  		content = '已发布';
      	return content;
      }}
  ];
  var processDetailGridOption = {
      lang: 'zh-cn',
  ajaxLoad: true,
  check : true,
  loadURL: ctx + '/process/processShowProcessVersion.action',
  exportFileName: '流程版本列表',
  columns: processDetailGridColumns,
  gridContainer: 'gridContainerVersion',
  toolbarContainer: 'gridToolBarContainerVersion',
  tools: '',
  pageSize: 10,
  pageSizeLimit: '10',
  onRowClick : function(value, record, column, grid, dataNo, columnNo, cell, row, extraCell, e){
  	if(column.id == 'operation' || column.title == '操作') {
  		var targetElObj = $(e.target);
		if (targetElObj.hasClass('list-edit-btn')){
			addwork.updateaddworkUI(targetElObj.data('record'));
		}
		return;
  	}
  	var $checkBox = row.find("input[type='checkbox']");
  	var status = $checkBox.prop("checked");
  	$checkBox.prop("checked", !status);
      }
  };
var processDetailGrid = $.fn.dlshouwen.grid.init(processDetailGridOption);
/**
 * 显示流程定义的版本
 * @param id
 */
function preVersionManager(id){
	processDetailGrid.parameters = new Object();
	processDetailGrid.parameters['defineId'] = id;
	processDetailGrid.load();
	$('#search_detail').click(searchDetail);
	$('#defineId').val(id);
	$('#versionProcessId').val(id);
	var options = {}
	options.backdrop = 'static';
	$('#versionModal').modal(options)
}
/**
 * 绑定查询版本方法
 */
function searchDetail() {
	var dName = $('#detailName_search').val();
	processDetailGrid.parameters = new Object();
	processDetailGrid.parameters['dName'] = dName;
	processDetailGrid.parameters['defineId'] = $('#defineId').val();
    //第二个参数用于表示是否为查询
	processDetailGrid.refresh(true, true);
}
/**
 * 新建工作流程版本
 */
function preShowDetailModal() {
	$('#saveProcessName_detail').val('');
	$('#saveProcessDes_detail').val('');
	$('#versionModal').modal('hide');
	$('#saveOrUpdateVersion_btn').off('click');
	$('#saveOrUpdateVersion_btn').on('click',realSaveProcessVersion);
	$('#newVersionModalLabel').html('新增流程版本');
	var options = {}
	options.backdrop = 'static';
	$('#newVersionModal').modal(options)
}

function closeShowDetailModal() {
	$('#newVersionModal').modal('hide');
	$('#versionModal').modal('show');
}

function realSaveProcessVersion() {
	var vName = $('#saveProcessName_detail').val();
	var vDes = $('#saveProcessDes_detail').val();
	var options = {}
	if(vName == '') {
		options.placement = 'top';
		options.title = '流程版本名称不能为空'
		$('#saveProcessName_detail').tooltip(options)
		$('#processNameGroup_detail').removeClass('form-group').addClass('form-group has-error')
		return false;
	}else{
		$('#saveProcessName_detail').tooltip('destroy')
		$('#processNameGroup_detail').removeClass('form-group has-error').addClass('form-group')
	}
	var detail = {"detail.detailName":vName,"detail.description":vDes,"detail.defineId" : $('#versionProcessId').val()};
	$.ajax({
		url : ctx + '/process/saveProcessVersion.action',
		type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
		async : false, // 默认值: true。默认设置下，所有请求均为异步请求
		dataType : 'json', // 预期服务器返回的数据类型
		data : detail,
		success : function(json) {
			if (json && json.state) {
				alert(json.info);
				if(json.state === 'success') {
					closeShowDetailModal();
					searchDetail();
					search();
				}
			}
		},
		error : function(json) {// 请求失败时调用此函数。
			alert("系统请求失败！");
		}
	})
}
/**
 * 该方法用于显示编辑版本信息界面
 * @param id
 */
function preModifyProcessVersion(id) {
	$.ajax({
		url : ctx + '/process/preUpdateFlowVersion.action',
		type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
		async : false, // 默认值: true。默认设置下，所有请求均为异步请求
		dataType : 'json', // 预期服务器返回的数据类型
		data : {detailId : id},
		success : function(json) {
			if (json && json.state) {
				if(json.state === 'success') {
					var detail = json.detail;
					showUpdateFlowVersionModal(detail);
				}else{
					alert('获取记录失败，请稍后重试');
				}
			}
		},
		error : function(json) {// 请求失败时调用此函数。
			alert("无相关记录，请刷新后重试");
		}
	})
}

function showUpdateFlowVersionModal(detail) {
	$("#newVersionModal .form-horizontal .form-control").val('');
	$('#saveProcessName_detail').tooltip('destroy')
	$('#processNameGroup_detail').removeClass('form-group has-error').addClass('form-group')
	$('#saveProcessName_detail').val(detail.detailName);
	$('#saveProcessDes_detail').val(detail.description==null ? '' : detail.description);
	$('#newVersionModalLabel').html('编辑流程版本');
	$('#versionProcessId').val(detail.id);
	$('#saveOrUpdateVersion_btn').off('click');
	$('#saveOrUpdateVersion_btn').on('click',realUpdateProcessVersion);
	var options = {}
	options.backdrop = 'static';
	$('#versionModal').modal('hide');
	$('#newVersionModal').modal(options)
}

function realUpdateProcessVersion(){
	var vName = $('#saveProcessName_detail').val();
	var vDes = $('#saveProcessDes_detail').val();
	var options = {}
	if(vName == '') {
		options.placement = 'top';
		options.title = '流程版本名称不能为空'
		$('#saveProcessName_detail').tooltip(options)
		$('#processNameGroup_detail').removeClass('form-group').addClass('form-group has-error')
		return false;
	}else{
		$('#saveProcessName_detail').tooltip('destroy')
		$('#processNameGroup_detail').removeClass('form-group has-error').addClass('form-group')
	}
	var oldId = $('#versionProcessId').val();
	var detail = {"detail.id":oldId,"detail.detailName":vName,"detail.description":vDes,"detail.defineId" : $('#versionProcessId').val()};
	$.ajax({
		url : ctx + '/process/updateProcessVersion.action',
		type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
		async : false, // 默认值: true。默认设置下，所有请求均为异步请求
		dataType : 'json', // 预期服务器返回的数据类型
		data : detail,
		success : function(json) {
			if (json && json.state) {
				alert(json.info);
				if(json.state === 'success') {
					closeShowDetailModal();
					searchDetail();
					search();
				}
			}
		},
		error : function(json) {// 请求失败时调用此函数。
			alert("系统请求失败！");
		}
	})
}

/**
 * 删除流程版本方法
 */
function deleteDetailVersion(){
	var records = processDetailGrid.getCheckedRecords();
	var size = records.length;
	if(size == 0)  {
		alert('请至少选择一条记录');
		return;
	}
	for(var i=0; i<size; i++) {
		if(records[i].status == 1){
			alert('已经发布的流程版本无法删除，请重新选择');
			return false;
		}
	}
	if(confirm('确认删除选中的流程版本吗？')){
		var ids = [];
		for(var i=0; i<size; i++) {
			ids.push(records[i].id);
		}
		$.ajax({
			url : ctx + '/process/deleteProcessVersion.action',
			type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
			traditional:true,
			async : false, // 默认值: true。默认设置下，所有请求均为异步请求
			dataType : 'json', // 预期服务器返回的数据类型
			data : {detailIds : ids},
			success : function(json) {
				if (json && json.state) {
					alert(json.info);
					if(json.state === 'success'){
						searchDetail();
						search();
					}
				}
			},
			error : function(json) {// 请求失败时调用此函数。
				alert("系统请求失败！");
			}
		})
	}
}
/**
 * 删除流程版本单条
 * @param id
 */
function deleteProcessVersion(id) {
	if(confirm('确认删除该条流程版本吗？')) {
		var ids = new Array();
		ids.push(id);
		$.ajax({
			url : ctx + '/process/deleteProcessVersion.action',
			type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
			traditional:true,
			async : false, // 默认值: true。默认设置下，所有请求均为异步请求
			dataType : 'json', // 预期服务器返回的数据类型
			data : {detailIds : ids},
			success : function(json) {
				if (json && json.state) {
					alert(json.info);
					if(json.state === 'success'){
						searchDetail();
						search();
					}
				}
			},
			error : function(json) {// 请求失败时调用此函数。
				alert("系统请求失败！");
			}
		})
	}
}

/**
 * 该方法用于展示设计流程图
 * @param id
 */
function preShowProcessDefine(modelId) {
	window.open(ctx+'/modeler.html?modelId='+modelId,'activitiDesigner');
}

/**
 * 该方法用于流程发布
 * @param modelId
 */
function deployProcess(modelId) {
	if(confirm('确认发布该条流程吗？')){
		$.ajax({
			url : ctx + '/process/deployProcess.action?modelId='+modelId,
			type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
			traditional:true,
			async : false, // 默认值: true。默认设置下，所有请求均为异步请求
			dataType : 'json', // 预期服务器返回的数据类型
			success : function(json) {
				if (json && json.state) {
					alert(json.info);
					if(json.state === 'success'){
						searchDetail();
						search();
					}
				}
			},
			error : function(json) {// 请求失败时调用此函数。
				alert("系统请求失败！");
			}
		})
	}
}