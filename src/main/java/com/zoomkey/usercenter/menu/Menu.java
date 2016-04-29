/*
 * Copyright: Tianjin Zoomkey Software Co,.ltd, China
 * ems
 * com.zoomkey.ems.menu.Menu.java
 * Created on 2015年8月5日-下午7:01:46
 */

package com.zoomkey.usercenter.menu;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;

import com.zoomkey.framework.core.BaseObject;
import com.zoomkey.usercenter.role.Role;

@Entity
public class Menu extends BaseObject {

	private static final long	serialVersionUID	= 8526304959871496274L;

	private Long					parentId;

	private String					name;

	private Menu					parentObj;

	private String					code;

	private String					url;

	private int						showIndex;

	private int						type;

	private int						belongSys;

	private Set<Role>				roles;

	/**
	 * @return Returns the parentId.
	 */
	public Long getParentId() {
		return this.parentId;
	}

	/**
	 * @return Returns the parent.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID_", insertable = false, updatable = false)
	public Menu getParentObj() {
		return this.parentObj;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return this.code;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @return Returns the showIndex.
	 */
	public int getShowIndex() {
		return this.showIndex;
	}

	/**
	 * @return Returns the menuType.
	 */
	public int getType() {
		return this.type;
	}

	/**
	 * @return Returns the roles.
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "t_role_menu")
	@Where(clause = "IS_VALID_ = 1")
	public Set<Role> getRoles() {
		return this.roles;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param parentId The parentId to set.
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParentObj(Menu parentObj) {
		this.parentObj = parentObj;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param showIndex The showIndex to set.
	 */
	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}

	/**
	 * @param menuType The menuType to set.
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param roles The roles to set.
	 */
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public int getBelongSys() {
		return this.belongSys;
	}

	public void setBelongSys(int belongSys) {
		this.belongSys = belongSys;
	}
}
