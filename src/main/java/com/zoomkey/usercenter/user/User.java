
package com.zoomkey.usercenter.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zoomkey.framework.core.BaseObject;
import com.zoomkey.usercenter.org.Org;
import com.zoomkey.usercenter.role.Role;

@Entity
public class User extends BaseObject implements UserDetails {

	private static final long	serialVersionUID	= 3418227281370383846L;

	/**
	 * 所属机构id
	 */
	private Long					orgId;

	/**
	 * 用户名
	 */
	private String					name;

	private String					username;

	private String					password;

	private String					mobile;

	private Integer				isDisabled;

	/**
	 * 新密码（非数据库字段）
	 */
	private String					newPassword;

	/**
	 * 关联角色集合
	 */
	private List<Role>			roles					= new ArrayList<Role>();

	/**
	 * 获取对应角色集合，名称
	 */
	private String					roleNames;

	/**
	 * 获取对应用户所在组织机构对象
	 */
	private Org						orgObj;

	public Long getOrgId() {
		return this.orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String pwd) {
		this.password = pwd;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getIsDisabled() {
		return this.isDisabled;
	}

	public void setIsDisabled(Integer isDisabled) {
		this.isDisabled = isDisabled;
	}

	@Transient
	public String getNewPassword() {
		return this.newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@Fetch(FetchMode.SELECT)
	@Where(clause = "IS_VALID_ = 1")
	public List<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * 为用户添加角色方法
	 *
	 */
	public void addRole(Role role) {
		getRoles().add(role);
	}

	@Transient
	public String getRoleNames() {
		final StringBuilder result = new StringBuilder();
		for (final Role role : this.roles) {
			result.append(",").append(role.getName());
		}
		this.roleNames = result.length() > 0 ? result.substring(1) : "";
		return this.roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public enum EnabledEnum {
		可用(1), 不可用(0);

		private Integer flag;

		private EnabledEnum(Integer flag) {
			this.flag = flag;
		}

		public static String getEnabledEnum(Integer compare) {
			String result = "";
			final EnabledEnum[] enums = EnabledEnum.values();
			for (final EnabledEnum e : enums) {
				if (e.getFlag().intValue() != compare.intValue()) {
					continue;
				}
				result = e.name();
				break;
			}
			return result;
		}

		public Integer getFlag() {
			return this.flag;
		}

		public void setFlag(Integer flag) {
			this.flag = flag;
		}
	}

	// ================================= 以下为security相关 ====================================
	/**
	 * 用户是否过期
	 */
	private boolean					accountExpired			= false;

	/**
	 * 用户是否锁定
	 */
	private boolean					accountLocked			= false;

	/**
	 * 用户凭证是否过期
	 */
	private boolean					credentialsExpired	= false;

	private Set<GrantedAuthority>	authorities				= new HashSet<GrantedAuthority>();

	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * 为该user用户返回权限方法
	 * @see UserDetails#getAuthorities()
	 */
	@Override
	@Transient
	@JsonIgnore
	public Set<GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	@Transient
	public boolean isEnabled() {
		return this.isValid == 1;
	}

	@Transient
	public boolean isAccountExpired() {
		return this.accountExpired;
	}

	@Override
	@Transient
	public boolean isAccountNonExpired() {
		return !isAccountExpired();
	}

	@Transient
	public boolean isAccountLocked() {
		return this.accountLocked;
	}

	@Override
	@Transient
	public boolean isAccountNonLocked() {
		return !isAccountLocked();
	}

	@Transient
	public boolean isCredentialsExpired() {
		return this.credentialsExpired;
	}

	@Override
	@Transient
	public boolean isCredentialsNonExpired() {
		return !this.credentialsExpired;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	/**
	 * @function:用于加密采用的key
	 * @return
	 */
	@Transient
	public Long getSalt() {
		return getId();
	}

	/**
	 * @function:用于加密采用的key
	 * @return
	 */
	public void setSalt(Long salt) {
		setId(salt);
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ORG_ID_", insertable = false, updatable = false)
	public Org getOrgObj() {
		return this.orgObj;
	}

	public void setOrgObj(Org orgObj) {
		this.orgObj = orgObj;
	}
}