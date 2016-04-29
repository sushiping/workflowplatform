
package com.zoomkey.workflow.process;

import java.io.UnsupportedEncodingException;

import com.zoomkey.framework.core.GenericBo;

public interface ProcessDefineBo extends GenericBo<ProcessDefine, Long> {

	String processShowFlowDefine();

	String saveProcessFlow(ProcessDefine define) throws UnsupportedEncodingException;

	ProcessDefine getProcessDefineById(Long id);

	void updateProcessFlow(ProcessDefine newDefine);

	void deleteProcessFlow(Long[] ids);

	String processShowFlowDetail();

	void saveProcessVersion(ProcessDefineDetail detail);

	ProcessDefineDetail getProcessDefineDetailById(Long id);

	void updateProcessVersion(ProcessDefineDetail detail);

	void deleteProcessVersion(Long[] ids);

	void deployProcess(String modelId);

	boolean isAllowKey(String key, Long id);

	void startProcess(String modelId);
}
