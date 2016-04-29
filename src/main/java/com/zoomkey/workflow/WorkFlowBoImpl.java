
package com.zoomkey.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.explorer.util.XmlUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("workFlowBo")
public class WorkFlowBoImpl implements WorkFlowBo {

    /**
     * log日至输出对象，子类也可用该属性作为日志输出
     */
    protected final transient Logger log = LogManager.getLogger(WorkFlowBoImpl.class);

    private static final String PREFIX = "activiti/";

    private RuntimeService runtimeService;

    private TaskService taskService;

    private RepositoryService repositoryService;

    private IdentityService identityService;

    @Override
    public void processDeployTask(String deployDir) {
        // 发布一个流程
        this.repositoryService.createDeployment().addClasspathResource(PREFIX + deployDir).deploy();
//		this.repositoryService.createDeployment().addClasspathResource(PREFIX + "EquipDefectProcess.png").deploy();
        this.log.info("发布流程记录数：" + this.repositoryService.createProcessDefinitionQuery().count());
        // // 根据群组名称获取对应任务
        // List<Task> tasks =
        // this.taskService.createTaskQuery().taskCandidateGroup("accountancy").list();
        // for (final Task task : tasks) {
        // this.log.info("Following task is available for accountancy group: " + task.getName());
        // // 由fozzie认领该任务
        // this.taskService.claim(task.getId(), "fozzie");
        // }
        // // 只有fozzie现在可以看到该任务流程
        // tasks = this.taskService.createTaskQuery().taskAssignee("fozzie").list();
        // for (final Task task : tasks) {
        // this.log.info("Task for fozzie: " + task.getName());
        // // 完成该流程
        // this.taskService.complete(task.getId());
        // }
        // this.log.info("Number of tasks for fozzie: " +
        // this.taskService.createTaskQuery().taskAssignee("fozzie").count());
        // // 流转到第二个任务节点，并由kermit认领该任务
        // tasks = this.taskService.createTaskQuery().taskCandidateGroup("management").list();
        // for (final Task task : tasks) {
        // this.log.info("Following task is available for accountancy group: " + task.getName());
        // this.taskService.claim(task.getId(), "kermit");
        // }
        // // 完成整个流程任务
        // for (final Task task : tasks) {
        // this.taskService.complete(task.getId());
        // }
        // // 验证流程已经结束
        // final HistoricProcessInstance historicProcessInstance =
        // this.historyService.createHistoricProcessInstanceQuery()
        // .processInstanceId(procId)
        // .singleResult();
        // this.log.info("Process instance end time: " + historicProcessInstance.getEndTime());
    }

    @Autowired
    public void setRuntimeService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @Autowired
    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }


    @Autowired
    public void setRepositoryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public InputStream getTaskImage() {
        ProcessDefinition procDef = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionId("equipDefectProcess:27:107513")
                .singleResult();
        String diagramResourceName = procDef.getDiagramResourceName();
        InputStream imageStream = this.repositoryService.getResourceAsStream(procDef.getDeploymentId(),
                diagramResourceName);
        return imageStream;
    }

    @Override
    public String startProcessTask(String userId) {
        // 设置启动前对应的申请人
        this.identityService.setAuthenticatedUserId(userId);
        // 启动一个流程
        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("defectType", 1);
        final String procId = this.runtimeService.startProcessInstanceByKey("equipDefectProcess", variables).getId();
        return procId;
    }


    @Autowired
    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public void claimTask(String userName, String executionId) {
        final Task task = this.taskService.createTaskQuery().executionId(executionId).singleResult();
        this.taskService.claim(task.getId(), userName);
    }

    @Override
    public void completeTask(String userName) {
        final List<Task> tasks = this.taskService.createTaskQuery().taskAssignee(userName).list();
        for (final Task task : tasks) {
            final Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("agreeFlag", 1);
            this.taskService.complete(task.getId(), variables);// 完成当前流程
        }
    }

    @Override
    public ActivityImpl getCurrentImgFlag() {
        ProcessDefinition processDefinition = this.repositoryService.createProcessDefinitionQuery()
                .processDefinitionId("equipDefectProcess:27:107513")
                .singleResult();
        ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
        String processDefinitionId = pdImpl.getId();// 流程标识
        ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) this.repositoryService)
                .getDeployedProcessDefinition(processDefinitionId);
        ActivityImpl actImpl = null;
        ExecutionEntity execution = (ExecutionEntity) this.runtimeService.createExecutionQuery()
                .executionId("107514")
                .singleResult();// 执行实例
        String activitiId = execution.getActivityId();// 当前实例的执行到哪个节点
        // List<String>activitiIds = runtimeService.getActiveActivityIds(executionId);
        List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
        // for(String activitiId : activitiIds){
        for (ActivityImpl activityImpl : activitiList) {
            String id = activityImpl.getId();
            if (id.equals(activitiId)) {// 获得执行到那个节点
                actImpl = activityImpl;
                break;
            }
        }
        // }
        return actImpl;
    }

    @Override
    public void convertXMLToModeler(String dir) throws UnsupportedEncodingException {
        String msg = "";
        XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(PREFIX + dir);
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(is, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        XMLStreamReader xtr = null;
        try {
            xtr = xif.createXMLStreamReader(in);
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
        if (bpmnModel.getMainProcess() == null
                || bpmnModel.getMainProcess().getId() == null) {
            msg = "上传流程文件解析有问题！";
            log.error(msg);
            return;
        } else {
            BpmnJsonConverter converter = new BpmnJsonConverter();
            ObjectNode modelNode = converter.convertToJson(bpmnModel);
            Model modelData = repositoryService.newModel();
            ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
            modelObjectNode.put("name", "计划审核");
            modelObjectNode.put("revision", 1);
            modelObjectNode.put("documentation", "xml上传计划审核");
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName("计划审核");
            repositoryService.saveModel(modelData);
            //modelNode.put("resourceId", modelData.getId());
            log.debug("json:" + modelNode.toString());
            repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
        }
    }
}
