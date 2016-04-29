
package com.zoomkey.usercenter.org;

import java.util.List;

import com.zoomkey.framework.core.GenericBo;

public interface OrgBo extends GenericBo<Org, Long> {

	/**
	 * 
	 * @function:该方法用于返回全部租户记录
	 * @return
	 */
	List<Org> getTenantList();
}
