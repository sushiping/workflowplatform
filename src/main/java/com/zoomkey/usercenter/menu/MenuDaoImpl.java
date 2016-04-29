/*
 * Copyright: Tianjin Zoomkey Software Co,.ltd, China
 * ems
 * com.zoomkey.ems.menu.MenuDao.java
 * Created on 2015年8月5日-下午7:42:59
 */

package com.zoomkey.usercenter.menu;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zoomkey.framework.core.GenericDaoImpl;

/**
 * 类功能描述：
 */
@Repository("menuDao")
public class MenuDaoImpl extends GenericDaoImpl<Menu, Long> implements MenuDao {

	/**菜单标识*/
	private static final Integer MENU_FLAG = 0;

	public MenuDaoImpl() {
		super(Menu.class);
	}

	/**
	 * @function:获取用户权限菜单
	 * @param userId 用户id
	 * @param belongSys 所属系统
	 * @return
	 */
	@Override
	public List<Menu> getUserMenus(Long userId, Integer belongSys) {
		final String hql = "SELECT m FROM Menu m LEFT JOIN m.roles r LEFT JOIN r.users u "
					+ "WHERE m.type = ? AND u.id = ? AND m.belongSys =?"
					+ "ORDER BY m.showIndex";
		final Object args[] = {
					MENU_FLAG, userId, belongSys};
		final List<Menu> menuList = query(hql, args);
		return menuList;
	}

	/**
	 * @function:获取角色权限菜单
	 * @param roleIds 角色id串
	 * @param belongSys 所属系统
	 * @return
	 */
	@Override
	public List<Menu> getRoleMenus(String roleIds, Integer belongSys) {
		String hql = "SELECT m FROM Menu m LEFT JOIN m.roles r "
					+ "WHERE r.id IN("
					+ roleIds
					+ ") AND m.belongSys=?"
					+ "ORDER BY m.showIndex";
		Object args[] = {
					belongSys};
		return query(hql, args);
	}

	/**
	 * @function:得到所有的菜单
	 * @param belongSys 所属系统
	 * @return
	 */
	@Override
	public List<Menu> getAllMenu(Integer belongSys) {
		String hql = "FROM Menu WHERE belongSys=? ORDER BY showIndex ASC";
		Object[] params = {
					belongSys};
		List<Menu> menuList = query(hql, params);
		return menuList;
	}

	/**
	 * @function:根据id串查询菜单
	 * @param menuIds
	 * @return
	 */
	@Override
	public List<Menu> getMenus(String menuIds) {
		String hql = "FROM Menu WHERE id IN (" + menuIds + ")";
		return query(hql);
	}
}
