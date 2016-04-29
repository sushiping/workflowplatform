/*
 * Copyright: Tianjin Zoomkey Software Co,.ltd, China
 * ems
 * com.zoomkey.ems.menu.IMenuDao.java
 * Created on 2015年8月5日-下午7:28:43
 */

package com.zoomkey.usercenter.menu;

import java.util.List;

import com.zoomkey.framework.core.GenericDao;

/**
 * 类功能描述：
 */
public interface MenuDao extends GenericDao<Menu, Long> {

	/**
	 * @function:获取用户权限菜单
	 * @param userId 用户id
	 * @param belongSys 所属系统
	 * @return
	 */
	public List<Menu> getUserMenus(Long userId, Integer belongSys);

	/**
	 * @function:获取角色权限菜单
	 * @param roleIds 角色id串
	 * @param belongSys 所属系统
	 * @return
	 */
	public List<Menu> getRoleMenus(String roleIds, Integer belongSys);

	/**
	 * @function:得到所有的菜单
	 * @param belongSys 所属系统
	 * @return
	 */
	public List<Menu> getAllMenu(Integer belongSys);

	/**
	 * @function:根据id串查询菜单
	 * @param menuIds
	 * @return
	 */
	public List<Menu> getMenus(String menuIds);
}
