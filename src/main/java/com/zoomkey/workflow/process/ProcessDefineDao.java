
package com.zoomkey.workflow.process;

import com.zoomkey.core.PagerForGrid;
import com.zoomkey.framework.core.GenericDao;

public interface ProcessDefineDao extends GenericDao<ProcessDefine, Long> {

	PagerForGrid<ProcessDefine> queryFlowDefine();

	long queryCountByKey(String key, Long id);
}
