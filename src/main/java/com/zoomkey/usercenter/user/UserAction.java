
package com.zoomkey.usercenter.user;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.zoomkey.core.BaseAction;
import com.zoomkey.usercenter.org.Org;
import com.zoomkey.usercenter.org.OrgBo;
import com.zoomkey.usercenter.org.OrgNode;

import net.sf.json.JSONArray;

public class UserAction extends BaseAction {

	private OrgBo	orgBo;

	private UserBo	userBo;

	private Long	pid;

	@Action(value = "findRootOrg")
	public String findRootOrg() {
		List<OrgNode> nodes = new ArrayList<>();
		List<Org> orgs = this.orgBo.getTenantList();
		for (Org org : orgs) {
			org.setUsers(this.userBo.getUserListByOrgId(org.getId()));
			OrgNode node = new OrgNode(org);
			nodes.add(node);
		}
		addRecordData("orgs", nodes);
		return JSON;
	}

	@Action(value = "findCandidateUser")
	public void findCandidateUser() {
		this.log.info("-------get childern-------pid==" + this.pid);
		List<User> users = this.userBo.getUserListByOrgId(this.pid);
		List<OrgNode> nodes = new ArrayList<>();
		for (User user : users) {
			OrgNode node = new OrgNode(user, this.pid);
			nodes.add(node);
		}
		JSONArray array = JSONArray.fromObject(nodes);
		returnOnlyStr(array.toString());
	}

	@Autowired
	public void setOrgBo(OrgBo orgBo) {
		this.orgBo = orgBo;
	}

	@Autowired
	public void setUserBo(UserBo userBo) {
		this.userBo = userBo;
	}

	public Long getPid() {
		return this.pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}
}
