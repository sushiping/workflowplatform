
package com.zoomkey.usercenter.org;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zoomkey.framework.core.GenericBoImpl;

@Service("orgBo")
public class OrgBoImpl extends GenericBoImpl<Org, Long> implements OrgBo {

	private OrgDao orgDao;

	@Override
	public List<Org> getTenantList() {
		return this.orgDao.getAll();
	}

	@Autowired
	public void setOrgDao(OrgDao orgDao) {
		this.dao = orgDao;
		this.orgDao = orgDao;
	}
}
