
package com.zoomkey.usercenter.role;

import java.util.List;

import com.zoomkey.framework.core.GenericBo;

/**
 * <p>RoleService接口，用于连接持久层（Dao）和表现层（Action）
 * 针对Role进行相关的业务逻辑都放在该类中。</p>
 * 该类继承自泛型Service（GenericManager）
 *
 */
public interface RoleBo extends GenericBo<Role, Long> {

	/**
	 * 返回全部角色集合
	 */
	List<Role> getRoles();

	/**
	 * @function:返回全部角色集合
	 * @param ids 角色id串
	 * @return
	 */
	public List<Role> getRoles(String ids);

	/**
	 * 根据角色名字返回对应角色
	 */
	Role getRole(String rolename);

	/**
	 * 保存角色方法，根据role对象保存
	 */
	Role saveRole(Role role);

	/**
	 * @function: 修改角色
	 * @param roleObj 角色对象
	 */
	void updateRole(Role roleObj);

	/**
	 * 删除角色
	 */
	void deleteRole(Long id);

	/**
	 * @function:设置角色菜单
	 * @param roleId
	 * @param menuIds
	 */
	public void saveRoleMenus(Long roleId, String menuIds);

	public List<Role> getUgRoleList(Integer belongSys);
}
