
package com.zoomkey.workflow.process;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zoomkey.core.PagerForGrid;
import com.zoomkey.framework.core.GenericBoImpl;
import com.zoomkey.framework.util.LogUtil;

import net.sf.json.JSONObject;

@Service("processDefineBo")
public class ProcessDefineBoImpl extends GenericBoImpl<ProcessDefine, Long> implements ProcessDefineBo {

	private ProcessDefineDao			processDefineDao;

	private ProcessDefineDetailDao	processDefineDetailDao;

	private RepositoryService			repositoryService;

	@SuppressWarnings("rawtypes")
	@Override
	public String processShowFlowDefine() {
		PagerForGrid records = this.processDefineDao.queryFlowDefine();
		JSONObject obj = JSONObject.fromObject(records);
		return obj.toString();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String processShowFlowDetail() {
		PagerForGrid records = this.processDefineDetailDao.queryFlowDetail();
		JSONObject obj = JSONObject.fromObject(records);
		return obj.toString();
	}

	@SuppressWarnings("deprecation")
	@Override
	public String saveProcessFlow(ProcessDefine define) throws UnsupportedEncodingException {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilSetNode);
		ObjectNode properties = objectMapper.createObjectNode();
		properties.put("process_id", define.getKey() + "V1");
		editorNode.put("properties", properties);
		Model modelData = this.repositoryService.newModel();
		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, define.getProcessName());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, define.getDescription());
		modelData.setMetaInfo(modelObjectNode.toString());
		modelData.setName(define.getProcessName());
		modelData.setKey(define.getKey());
		modelData.setVersion(1);
		this.repositoryService.saveModel(modelData);
		this.repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
		// 开始保存自定义表
		this.processDefineDao.save(define);
		ProcessDefineDetail detail = new ProcessDefineDetail();
		detail.setDefineId(define.getId());
		detail.setDetailName(define.getProcessName());
		detail.setDescription(define.getDescription());
		detail.setModelId(modelData.getId());
		detail.setVersion("1");
		this.processDefineDetailDao.save(detail);
		return modelData.getId();
	}

	@Override
	public ProcessDefine getProcessDefineById(Long id) {
		return this.processDefineDao.get(id);
	}

	@Override
	public void updateProcessFlow(ProcessDefine newDefine) {
		if (newDefine.getId() == null) {
			return;
		}
		ProcessDefine oldDefine = getProcessDefineById(newDefine.getId());
		oldDefine.setDescription(newDefine.getDescription());
		oldDefine.setKey(newDefine.getKey());
		oldDefine.setProcessName(newDefine.getProcessName());
		this.processDefineDao.save(oldDefine);
	}

	@Override
	public void deleteProcessFlow(Long[] ids) {
		if (ids == null || ids.length <= 0) {
			return;
		}
		for (Long id : ids) {
			ProcessDefine define = getProcessDefineById(id);
			if (define == null) {
				continue;
			}
			List<ProcessDefineDetail> details = define.getDetails();
			for (ProcessDefineDetail detail : details) {
				String modelId = detail.getModelId();
				// 删除模型对象
				this.repositoryService.deleteModel(modelId);
				this.processDefineDetailDao.realDelete(detail);
			}
			this.processDefineDao.realDelete(define);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void saveProcessVersion(ProcessDefineDetail detail) {
		Long defineId = detail.getDefineId();
		ProcessDefine define = this.processDefineDao.get(defineId);
		if (define == null) {
			return;
		}
		List<ProcessDefineDetail> details = define.getDetails();
		int lastVersion = Integer.parseInt(details.get(details.size() - 1).getVersion());
		detail.setVersion(String.valueOf(lastVersion + 1));
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilSetNode);
		ObjectNode properties = objectMapper.createObjectNode();
		properties.put("process_id", define.getKey() + "V" + detail.getVersion());
		editorNode.put("properties", properties);
		Model modelData = this.repositoryService.newModel();
		ObjectNode modelObjectNode = objectMapper.createObjectNode();
		modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, detail.getDetailName());
		modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
		modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, detail.getDescription());
		modelData.setMetaInfo(modelObjectNode.toString());
		modelData.setName(detail.getDetailName());
		modelData.setKey(define.getKey());
		modelData.setVersion(1);
		this.repositoryService.saveModel(modelData);
		try {
			this.repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			this.log.error(LogUtil.stackTraceToString(e));
		}
		detail.setModelId(modelData.getId());
		this.processDefineDetailDao.save(detail);
	}

	@Override
	public ProcessDefineDetail getProcessDefineDetailById(Long id) {
		return this.processDefineDetailDao.get(id);
	}

	@Override
	public void updateProcessVersion(ProcessDefineDetail detail) {
		if (detail == null) {
			return;
		}
		Long oldId = detail.getId();
		ProcessDefineDetail oldDetail = this.processDefineDetailDao.get(oldId);
		oldDetail.setDetailName(detail.getDetailName());
		oldDetail.setDescription(detail.getDescription());
		this.processDefineDetailDao.save(oldDetail);
	}

	@Override
	public void deleteProcessVersion(Long[] ids) {
		if (ids == null || ids.length <= 0) {
			return;
		}
		for (Long id : ids) {
			ProcessDefineDetail detail = getProcessDefineDetailById(id);
			if (detail == null) {
				continue;
			}
			String modelId = detail.getModelId();
			// 删除模型对象
			this.repositoryService.deleteModel(modelId);
			this.processDefineDetailDao.realDelete(detail);
		}
	}

	@Override
	public void deployProcess(String modelId) {
		Model model = this.repositoryService.getModel(modelId);
		ObjectNode modelNode = null;
		try {
			modelNode = (ObjectNode) new ObjectMapper()
				.readTree(this.repositoryService.getModelEditorSource(model.getId()));
		} catch (JsonProcessingException e) {
			this.log.error(LogUtil.stackTraceToString(e));
		} catch (IOException e) {
			this.log.error(LogUtil.stackTraceToString(e));
		}
		byte[] bpmnBytes = null;
		BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
		bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
		String processName = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
		Deployment deploy = this.repositoryService.createDeployment()
			.addInputStream(processName, new ByteArrayInputStream(bpmnBytes))
			.deploy();
		model.setDeploymentId(deploy.getId());
		this.repositoryService.saveModel(model);
		// 更新相关发布流程状态
		List<ProcessDefineDetail> details = this.processDefineDetailDao.queryFlowDetailByModelId(modelId);
		for (ProcessDefineDetail detail : details) {
			detail.setStatus(1);
			this.processDefineDetailDao.save(detail);
		}
	}

	@Override
	public boolean isAllowKey(String key, Long id) {
		long result = this.processDefineDao.queryCountByKey(key, id);
		return result <= 0;
	}

	@Override
	public void startProcess(String modelId) {
		// TODO
	}

	@Autowired
	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	@Autowired
	public void setProcessDefineDao(ProcessDefineDao processDefineDao) {
		this.dao = processDefineDao;
		this.processDefineDao = processDefineDao;
	}

	@Autowired
	public void setProcessDefineDetailDao(ProcessDefineDetailDao processDefineDetailDao) {
		this.processDefineDetailDao = processDefineDetailDao;
	}
}
