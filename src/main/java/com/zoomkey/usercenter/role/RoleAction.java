
package com.zoomkey.usercenter.role;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.zoomkey.core.BaseAction;

public class RoleAction extends BaseAction {

	private RoleBo roleBo;

	@Action(value = "findRoleGroup")
	public String findRoleGroup() {
		List<Role> roles = this.roleBo.getAll();
		List<RoleNode> nodes = new ArrayList<RoleNode>();
		for (Role role : roles) {
			RoleNode node = new RoleNode(role);
			nodes.add(node);
		}
		addRecordData("roles", nodes);
		return JSON;
	}

	@Autowired
	public void setRoleBo(RoleBo roleBo) {
		this.roleBo = roleBo;
	}
}
