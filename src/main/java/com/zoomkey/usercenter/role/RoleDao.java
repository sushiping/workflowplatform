
package com.zoomkey.usercenter.role;

import java.util.List;

import com.zoomkey.framework.core.GenericDao;

/**
 * RoleDao接口，继承泛型Dao接口，并规定Model类型<Role>和主键类型<Long>
 *
 */
public interface RoleDao extends GenericDao<Role, Long> {

	/**
	 * 根据name获取Role对象
	 * @param rolename 角色名字
	 * @return 角色对象
	 */
	Role getRoleByName(String rolename);

	public List<Role> getUgRoleList(Integer belongSys);

	/**
	 * @function:返回全部角色集合
	 * @param ids 角色id串
	 * @return
	 */
	public List<Role> getRoles(String ids);
}
