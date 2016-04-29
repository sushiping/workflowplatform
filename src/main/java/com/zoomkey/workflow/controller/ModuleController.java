
package com.zoomkey.workflow.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Created by colortear on 2016/4/8.
 */
@Controller
@RequestMapping("/module")
public class ModuleController {

	private final Logger			logger	= LoggerFactory.getLogger(ModuleController.class);

	@Autowired
	private RepositoryService	repositoryService;

	@Autowired
	private RuntimeService		runtimeService;

	@Autowired
	private TaskService			taskService;

	@Autowired
	private ManagementService	managementService;

	@RequestMapping(value = "create")
	public void create(@RequestParam("name") String name, @RequestParam("key") String key, @RequestParam("description") String description, HttpServletRequest request, HttpServletResponse response) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = this.repositoryService.newModel();
			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			description = StringUtils.defaultString(description);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(name);
			modelData.setKey(StringUtils.defaultString(key));
			this.repositoryService.saveModel(modelData);
			this.repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
			response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
		} catch (Exception e) {
			this.logger.error("创建模型失败：", e);
		}
	}

	/**
	 * 导出model的xml文件
	* @throws IOException
	 */
	@RequestMapping("export")
	public void export(String modelId, HttpServletResponse response) throws IOException {
		try {
			Model modelData = this.repositoryService.getModel(modelId);
			BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
			JsonNode editorNode = new ObjectMapper()
				.readTree(this.repositoryService.getModelEditorSource(modelData.getId()));
			BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
			BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
			byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
			ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
			IOUtils.copy(in, response.getOutputStream());
			String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
			response.setHeader("Content-Disposition", "attachment; filename=" + filename);
			response.flushBuffer();
		} catch (Exception e) {
			this.logger.error("导出model的xml文件失败：modelId={}", modelId, e);
			response.getWriter()
				.print("<script language='javascript'>alert('该流程无任何节点，请设置后重新导出');window.close();</script>");
		}
	}

	@RequestMapping("deploy/{modelId}")
	public void deploy(@PathVariable("modelId") String modelId) {
		try {
			Model model = this.repositoryService.getModel(modelId);
			ObjectNode modelNode = (ObjectNode) new ObjectMapper()
				.readTree(this.repositoryService.getModelEditorSource(model.getId()));
			byte[] bpmnBytes = null;
			BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
			String processName = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
			Deployment deploy = this.repositoryService.createDeployment()
				.addInputStream(processName, new ByteArrayInputStream(bpmnBytes))
				.deploy();
			model.setDeploymentId(deploy.getId());
			this.repositoryService.saveModel(model);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
