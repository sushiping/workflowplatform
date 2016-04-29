
package com.zoomkey.workflow.process;

import java.util.List;

import com.zoomkey.core.PagerForGrid;
import com.zoomkey.framework.core.GenericDao;

public interface ProcessDefineDetailDao extends GenericDao<ProcessDefineDetail, Long> {

	PagerForGrid<ProcessDefineDetail> queryFlowDetail();

	List<ProcessDefineDetail> queryFlowDetailByModelId(String modelId);
}
