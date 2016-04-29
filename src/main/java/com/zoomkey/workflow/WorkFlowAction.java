package com.zoomkey.workflow;

import com.zoomkey.core.BaseAction;
import com.zoomkey.framework.util.LogUtil;
import com.zoomkey.framework.util.StringUtil;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

/**
 * Created by colortear on 2016/4/14.
 */
public class WorkFlowAction extends BaseAction {

    /**
     * 流程文件发布路径，由前台传入，可以直接写文件名称
     */
    private String deployDir;

    private WorkFlowBo workFlowBo;

    /**
     * @return
     * @function:发布流程定义
     */
    @Action(value = "deployTask")
    public String deployTask() {
        if (StringUtil.isNullOrEmpty(this.deployDir)) {
            addError("请填写流程发布路径");
            return JSON;
        }
        this.workFlowBo.processDeployTask(this.deployDir);
        addMessage("流程发布成功");
        return JSON;
    }


    @Action(value = "convertXMLToModeler")
    public String convertXMLToModeler() {
        if(StringUtil.isNullOrEmpty(this.deployDir)){
            addError("请填写流程转换路径");
            return JSON;
        }
        try {
            this.workFlowBo.convertXMLToModeler(this.deployDir);
        } catch (UnsupportedEncodingException e) {
            log.error(LogUtil.stackTraceToString(e));
            addError("流程转化失败");
        }
        addMessage("流程转化成功");
        return JSON;
    }


    public String getDeployDir() {
        return this.deployDir;
    }

    public void setDeployDir(String deployDir) {
        this.deployDir = deployDir;
    }

    @Autowired
    public void setWorkFlowBo(WorkFlowBo workFlowBo) {
        this.workFlowBo = workFlowBo;
    }

}
