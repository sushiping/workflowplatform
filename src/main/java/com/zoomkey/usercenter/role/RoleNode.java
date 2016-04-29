
package com.zoomkey.usercenter.role;

/**
 * 
 * 类功能描述：用于返回角色节点
 */
public class RoleNode {

	private Long	id;

	private Long	pId;

	private String	name;

	public RoleNode() {
	}

	public RoleNode(Role role) {
		this.id = role.getId();
		this.pId = -1l;
		this.name = role.getName();
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
}
