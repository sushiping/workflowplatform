
package com.zoomkey.usercenter.org;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zoomkey.framework.core.BaseObject;
import com.zoomkey.usercenter.user.User;

/**
 * 类功能描述：机构：主机构、子机构、部门
 */
@Entity
@Where(clause = "IS_VALID_ = 1")
public class Org extends BaseObject {

	private static final long	serialVersionUID	= 969807132105505483L;

	private Integer				type;													// 机构类型

	private String					name;													// 名称

	private Long					parentId;

	private String					address;												// 地址

	private String					tel;													// 电话

	private Long					principalId;										// 负责人ID

	private String					remark;												// 备注

	private Integer				showIndex;											// 显示排序顺序

	private Long					creatorId;											// 创建人id

	/**
	 * 获取根节点下对应下级
	 */
	private Set<Org>				children				= new HashSet<Org>();

	/**
	 * 表明该企业唯一身份标识，采用特殊加密算法生成
	 */
	private String					token;

	/**
	 * 部门下级用户节点
	 */
	private List<User>			users					= new ArrayList<User>();

	@JsonIgnore
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@JsonIgnore
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@JsonIgnore
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@JsonIgnore
	public Long getPrincipalId() {
		return this.principalId;
	}

	public void setPrincipalId(Long principalId) {
		this.principalId = principalId;
	}

	@JsonIgnore
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@JsonIgnore
	public Integer getShowIndex() {
		return this.showIndex;
	}

	public void setShowIndex(Integer showIndex) {
		this.showIndex = showIndex;
	}

	@JsonIgnore
	public Long getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public static enum Type {
		总公司("总公司", 0, "icon_distribute"), 分公司("分公司", 1, "icon_branch_com"), 供热站("供热站", 2, "icon_heat_station"), 部门("部门",
					3, "icon_department"), 收费站("收费站", 4, "icon_toll_station"), 收费组("收费组", 5,
								"icon_fees_sub"), 维修站("维修站", 6, "icon_toll_station"), 维修队("维修队", 7, "icon_fees_sub");

		private final String		label;

		private final Integer	value;

		private final String		iconType;

		private Type(String label, Integer value, String iconType) {
			this.label = label;
			this.value = value;
			this.iconType = iconType;
		}

		public String getLabel() {
			return this.label;
		}

		public Integer getValue() {
			return this.value;
		}

		public String getStringValue() {
			return this.value.toString();
		}

		public String getIconType() {
			return this.iconType;
		}

		public static String getLabel(Integer value) {
			for (final Type type : Type.values()) {
				if (type.getValue().equals(value)) {
					return type.getLabel();
				}
			}
			return null;
		}

		public static Integer getValue(String Label) {
			for (final Type type : Type.values()) {
				if (type.getLabel().equals(Label)) {
					return type.getValue();
				}
			}
			return null;
		}

		public static String getIconType(Integer value) {
			for (final Type type : Type.values()) {
				if (type.getValue().equals(value)) {
					return type.getIconType();
				}
			}
			return null;
		}
	}

	/**
	 * 子部门
	 */
	@OneToMany(mappedBy = "parentId", fetch = FetchType.LAZY)
	@JsonIgnore
	@Where(clause = "IS_VALID_ = 1")
	public Set<Org> getChildren() {
		return this.children;
	}

	public void setChildren(Set<Org> children) {
		this.children = children;
	}

	// 忽略一些基础信息，创建时间，更新时间等
	@Override
	@JsonIgnore
	@Transient
	public Date getCreateTime() {
		return super.getCreateTime();
	}

	@Override
	@JsonIgnore
	@Transient
	public Date getModifyTime() {
		return super.getModifyTime();
	}

	@Override
	@JsonIgnore
	@Transient
	public int getIsValid() {
		return super.getIsValid();
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Transient
	@JsonIgnore
	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}