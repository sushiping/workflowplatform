
package com.zoomkey.workflow;

import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public interface WorkFlowBo {

    void processDeployTask(String deployDir);

    InputStream getTaskImage();

    String startProcessTask(String userId);

    void claimTask(String userName, String executionId);

    void completeTask(String userName);

    ActivityImpl getCurrentImgFlag();

    void convertXMLToModeler(String dir) throws UnsupportedEncodingException;
}
