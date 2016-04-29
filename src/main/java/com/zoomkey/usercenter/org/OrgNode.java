
package com.zoomkey.usercenter.org;

import com.zoomkey.usercenter.user.User;

public class OrgNode {

	private Long		id;

	private Long		pId;

	private boolean	hasChild	= false;

	private String		name;

	private boolean	nocheck	= true;

	public OrgNode() {
	}

	public OrgNode(Org org) {
		this.id = org.getId();
		this.pId = org.getParentId() == null ? -1l : org.getParentId();
		this.name = org.getName();
		this.hasChild = org.getChildren().size() <= 0 ? org.getUsers().size() <= 0 ? false : true : true;
	}

	public OrgNode(User user, Long pid) {
		this.id = user.getId();
		this.pId = pid;
		this.name = user.getName();
		this.hasChild = false;
		this.nocheck = false;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getpId() {
		return this.pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNocheck() {
		return this.nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public boolean isHasChild() {
		return this.hasChild;
	}

	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
}
