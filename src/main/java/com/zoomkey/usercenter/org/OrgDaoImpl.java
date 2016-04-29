
package com.zoomkey.usercenter.org;

import org.springframework.stereotype.Repository;

import com.zoomkey.framework.core.GenericDaoImpl;

@Repository("orgDao")
public class OrgDaoImpl extends GenericDaoImpl<Org, Long> implements OrgDao {

	public OrgDaoImpl() {
		super(Org.class);
	}
}
