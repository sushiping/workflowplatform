var processGridColumns = [
	{id:'operation', title:'操作', type:'string', columnClass:'text-center', resolution:function(value, record, column, grid, dataNo, columnNo){
	        var content = '';
	        content += '<button title="流程启动" class="btn btn-xs btn-primary" onclick="preStartWorkflow('+record.id+','+record.status+');"><i class="glyphicon glyphicon-play"></i> 启动</button>';
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
    gridContainer: 'gridContainerStart',
    toolbarContainer: 'gridToolBarContainerStart',
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
//    processGrid.showProcessBar();
    //第二个参数用于表示是否为查询
    processGrid.refresh(true, true);
}

document.onkeydown = function(event) {
	e = event ? event : (window.event ? window.event : null);
	if (e.keyCode == 13) {
		if(document.getElementById('versionModal') && document.getElementById('versionModal').style.display == 'block') {
			searchDetail();
		}else{
			search();
		}
	} else
		return;
}

var processDetailGridColumns = [
{id:'operation', title:'操作', type:'string', columnClass:'text-center', resolution:function(value, record, column, grid, dataNo, columnNo){
        var content = '';
        content += '<button title="流程启动" class="btn btn-xs btn-warning" onclick="startWorkflow('+record.modelId+','+record.status+');"><i class="glyphicon glyphicon-play"></i> 启动</button>';
        content += '  ';
        content += '<button title="流程发布" class="btn btn-xs btn-success" onclick="deployProcess('+record.modelId+');"><i class="glyphicon glyphicon-ok"></i> 发布</button>';
        content += '  ';
        content += '<button title="设计流程" class="btn btn-xs btn-primary" onclick="preShowProcessDefine('+record.modelId+');"><i class="glyphicon glyphicon-pencil"></i> 设计</button>';
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
 * 该方法用于启动工作流相关流程
 * @param id
 * @param status
 */
function preStartWorkflow(id,status){
	if(status === 0) {
		alert('该流程还没有已经发布的版本，请发布后重试');
		return false;
	}
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
/**
 * 该方法用于流程启动
 * @param id
 * @param status
 */
function startWorkflow(id,status) {
	if(status === 0) {
		alert('该条流程还未发布，请先发布后重新启动该流程');
		return false;
	}
	if(confirm('确定启动该条流程吗？')) {
		$.ajax({
			url : ctx + '/process/startProcess.action',
			type : 'POST', // 默认值: "GET"。请求方式 ("POST" 或 "GET")， 默认为 "GET"
			data:{modelId:id},
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