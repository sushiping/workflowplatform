
package com.zoomkey.usercenter.role;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zoomkey.framework.core.GenericDaoImpl;

/**
 * RoleDao的实现类，继承了GenericDaoHibernate，可以对role进行保存删除编辑等相关操作。
 *
 */
@Repository("roleDao")
public class RoleDaoImpl extends GenericDaoImpl<Role, Long> implements RoleDao {

	/**
	 * 将Role对象进行持久化构造的方法
	 */
	public RoleDaoImpl() {
		super(Role.class);
	}

	/**
	 * 根据角色的名字获取对应角色对象
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public Role getRoleByName(String rolename) {
		final List roles = getSession().createCriteria(Role.class).add(Restrictions.eq("name", rolename)).list();
		if (roles.isEmpty()) {
			return null;
		} else {
			return (Role) roles.get(0);
		}
	}

	@Override
	public List<Role> getUgRoleList(Integer belongSys) {
		final String hql = "SELECT r FROM Role r WHERE r.isValid = 1 AND r.belongSys=? ORDER BY FN_ORDER_BY(r.name)";
		final Object[] params = {
					belongSys};
		return query(hql, params);
	}

	/**
	 * @function:返回全部角色集合
	 * @param ids 角色id串
	 * @return
	 */
	@Override
	public List<Role> getRoles(String ids) {
		final String hql = "FROM Role WHERE id IN(" + ids + ")";
		return query(hql);
	}
}
