
package com.zoomkey.usercenter.role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.struts2.json.annotations.JSON;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

import com.zoomkey.framework.core.BaseObject;
import com.zoomkey.usercenter.menu.Menu;
import com.zoomkey.usercenter.user.User;

/**
 * 用户角色Model，用于匹配数据库中表实体
 *
 */
@Entity
@Where(clause = "IS_VALID_ = 1")
public class Role extends BaseObject implements GrantedAuthority {

	private static final long	serialVersionUID	= 3690197650654049848L;

	/**
	 * 角色名称
	 */
	private String					name;

	/**
	 * 角色描述
	 */
	private String					remark;

	/**
	 * 是否内置
	 */
	private Integer				isIn;

	/**
	 * 所属系统
	 */
	private Integer				belongSys;

	/**
	 * 关联用户集合
	 */
	private Set<User>				users					= new HashSet<User>();

	/**
	 * 关联角色集合
	 */
	private List<Menu>			menus					= new ArrayList<Menu>();

	/**
	 * 默认构造方法，未赋值
	 */
	public Role() {
	}

	/**
	 * 为name赋值的构造方法
	 *
	 * @param name name of the role.
	 */
	public Role(final String name) {
		this.name = name;
	}

	/**
	 * 返回当前角色所拥有的权限
	 * 需要实现GrantedAuthority接口
	 * 当前返回角色名称
	 * @see GrantedAuthority#getAuthority()
	 */
	@Override
	@Transient
	public String getAuthority() {
		return getName();
	}

	@Column(length = 20)
	public String getName() {
		return this.name;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsIn() {
		return this.isIn;
	}

	public void setIsIn(Integer isIn) {
		this.isIn = isIn;
	}

	public Integer getBelongSys() {
		return this.belongSys;
	}

	public void setBelongSys(Integer belongSys) {
		this.belongSys = belongSys;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "t_role_menu")
	public List<Menu> getMenus() {
		return this.menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	/**
	 * 重写的角色equals方法
	 * 按角色名字区别
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Role)) {
			return false;
		}
		final Role role = (Role) o;
		return !(this.name != null ? !this.name.equals(role.name) : role.name != null);
	}

	/**
	 * 重写角色的hashcode，根据name返回hash值
	 */
	@Override
	public int hashCode() {
		return this.name != null ? this.name.hashCode() : 0;
	}

	/**
	 * 重写toString方法
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(this.name).toString();
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
	@JSON(serialize = false)
	@Where(clause = "IS_VALID_ = 1")
	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}
}
