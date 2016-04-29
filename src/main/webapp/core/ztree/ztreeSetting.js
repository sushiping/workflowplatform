var tree = function(){};
tree.translateNodesForCheck = function(rootNode,treeNodes) {
	if(rootNode){
		treeNodes.push({
			'id':rootNode.id,
			'pId':parseInt(rootNode.pId),
			'name':rootNode.name,
			open:true,
			isParent:rootNode.hasChild,
			nocheck:rootNode.nocheck
		})
	}
}

tree.initZtreeForCheck = function(treeId, treeNodes, url, asyncFlag){
	var setting = {
		async: {
			enable: asyncFlag,
			url: url,
			autoParam: ["id=pid"]
		},
		data: {
			simpleData: {
				enable: true,
				idKey: "id",
				pIdKey: "pId",
				rootPId: 0
			}
		},
		check: {
			enable: true,
			chkStyle: "checkbox",
			chkboxType : { "Y": "", "N": "" }
		},
		view: {
			fontCss:{marginLeft:'2px'},//background:"white",border:'none'},
			expandSpeed: ""
		},
		callback: {
			onCheck: treeOncheckForCheck
		}
	};
	return $.fn.zTree.init($("#"+treeId), setting, treeNodes);
}
function treeOncheckForCheck(event,treeId,treeNode,msg){
	var treeObj = $.fn.zTree.getZTreeObj(treeId);
	var noNodes = treeObj.getCheckedNodes(true);
	for(var i = 0;i<noNodes.length;i++){
		noNodes[i].checked = false;
		treeObj.updateNode(noNodes[i]);
	}
	treeNode.checked = true;
	treeObj.updateNode(treeNode);
}