
package com.zoomkey.workflow.process;

import java.io.UnsupportedEncodingException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.zoomkey.core.BaseAction;
import com.zoomkey.core.RequiresGridParams;
import com.zoomkey.framework.util.LogUtil;

/**
 * Created by colortear on 2016/4/19.
 */
public class ProcessAction extends BaseAction {

	private String						processName;

	private String						processKey;

	private String						description;

	private Long						defineId;

	private Long[]						defineIds;

	private Long[]						detailIds;

	private ProcessDefineDetail	detail;

	private Long						detailId;

	private ProcessDefineBo			processDefineBo;

	private String						modelId;

	/**
	 * @function:预加载流程定义界面
	 */
	@Action(value = "preShowFlowDefine", results = {
				@Result(name = SUCCESS, location = "flowDefine.jsp")})
	public String preDeployProcess() {
		return SUCCESS;
	}

	/**
	 * @function:预加载流程发布界面
	 */
	@Action(value = "preDeployFlow", results = {
				@Result(name = SUCCESS, location = "deployFlow.jsp")})
	public String preDeployFlow() {
		return SUCCESS;
	}

	@Action(value = "preStartFlow", results = {
				@Result(name = SUCCESS, location = "startFlow.jsp")})
	public String preStartFlow() {
		return SUCCESS;
	}

	@Action(value = "processShowFlowDefine")
	@RequiresGridParams
	public void processShowFlowDefine() {
		String result = this.processDefineBo.processShowFlowDefine();
		returnOnlyStr(result);
	}

	@Action(value = "preUpdateFlowDefine")
	public String preUpdateFlowDefine() {
		ProcessDefine define = this.processDefineBo.getProcessDefineById(this.defineId);
		addMessage("获取记录成功");
		addRecordData("define", define);
		return JSON;
	}

	/**
	 * 
	 * @function:该方法用于更新流程定义
	 * @return
	 */
	@Action(value = "updateFlowDefine")
	public String updateFlowDefine() {
		// 首先判定是否已经存在key
		boolean allowKey = this.processDefineBo.isAllowKey(this.processKey, this.defineId);
		if (!allowKey) {
			addError("该流程标识key已经存在，请重新填写");
			return JSON;
		}
		ProcessDefine newDefine = new ProcessDefine();
		newDefine.setId(this.defineId);
		newDefine.setDescription(this.description);
		newDefine.setKey(this.processKey);
		newDefine.setProcessName(this.processName);
		this.processDefineBo.updateProcessFlow(newDefine);
		addMessage("编辑流程成功");
		return JSON;
	}

	@Action(value = "saveFlowDefine")
	public String saveFlowDefine() {
		// 首先判定是否已经存在key
		boolean allowKey = this.processDefineBo.isAllowKey(this.processKey, null);
		if (!allowKey) {
			addError("该流程标识key已经存在，请重新填写");
			return JSON;
		}
		ProcessDefine define = new ProcessDefine();
		define.setDescription(this.description);
		define.setKey(this.processKey);
		define.setProcessName(this.processName);
		String modelId = "";
		try {
			modelId = this.processDefineBo.saveProcessFlow(define);
			addMessage("新建流程成功");
			addRecordData("modelId", modelId);
		} catch (UnsupportedEncodingException e) {
			this.log.error(LogUtil.stackTraceToString(e));
			addError("新建流程失败");
		}
		return JSON;
	}

	/**
	 * 
	 * @function:该方法用于删除流程定义
	 * @return
	 */
	@Action(value = "deleteFlowDefine")
	public String deleteFlowDefine() {
		this.processDefineBo.deleteProcessFlow(this.defineIds);
		addMessage("删除流程成功");
		return JSON;
	}

	@Action(value = "processShowProcessVersion")
	@RequiresGridParams
	public void processShowProcessVersion() {
		String result = this.processDefineBo.processShowFlowDetail();
		returnOnlyStr(result);
	}

	@Action(value = "saveProcessVersion")
	public String saveProcessVersion() {
		this.processDefineBo.saveProcessVersion(this.detail);
		addMessage("新建流程版本成功");
		return JSON;
	}

	@Action(value = "preUpdateFlowVersion")
	public String preUpdateFlowVersion() {
		ProcessDefineDetail detail = this.processDefineBo.getProcessDefineDetailById(this.detailId);
		addMessage("获取记录成功");
		addRecordData("detail", detail);
		return JSON;
	}

	@Action(value = "updateProcessVersion")
	public String updateProcessVersion() {
		this.processDefineBo.updateProcessVersion(this.detail);
		addMessage("编辑流程版本成功");
		return JSON;
	}

	@Action(value = "deleteProcessVersion")
	public String deleteProcessVersion() {
		this.processDefineBo.deleteProcessVersion(this.detailIds);
		addMessage("删除流程版本成功");
		return JSON;
	}

	@Action(value = "deployProcess")
	public String deployProcess() {
		String modelId = getRequest().getParameter("modelId");
		try {
			this.processDefineBo.deployProcess(modelId);
			addMessage("流程发布成功");
		} catch (NullPointerException e) {
			this.log.error(LogUtil.stackTraceToString(e));
			addError("流程节点为空或只存在开始、结束节点无法进行发布，请修改后重试");
		}
		return JSON;
	}

	@Action(value = "startProcess")
	public String startProcess() {
		try {
			this.processDefineBo.startProcess(this.modelId);
		} catch (Exception e) {
			this.log.error(LogUtil.stackTraceToString(e));
			addError("流程启动失败");
		}
		return JSON;
	}

	@Autowired
	public void setProcessDefineBo(ProcessDefineBo processDefineBo) {
		this.processDefineBo = processDefineBo;
	}

	public String getProcessName() {
		return this.processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getProcessKey() {
		return this.processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getDefineId() {
		return this.defineId;
	}

	public void setDefineId(Long defineId) {
		this.defineId = defineId;
	}

	public Long[] getDefineIds() {
		return this.defineIds;
	}

	public void setDefineIds(Long[] defineIds) {
		this.defineIds = defineIds;
	}

	public ProcessDefineDetail getDetail() {
		return this.detail;
	}

	public void setDetail(ProcessDefineDetail detail) {
		this.detail = detail;
	}

	public Long getDetailId() {
		return this.detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public Long[] getDetailIds() {
		return this.detailIds;
	}

	public void setDetailIds(Long[] detailIds) {
		this.detailIds = detailIds;
	}

	public String getModelId() {
		return this.modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
}
