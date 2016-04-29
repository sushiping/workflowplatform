/*
 * Copyright: Tianjin Zoomkey Software Co,.ltd, China
 * ems
 * com.zoomkey.ems.menu.IMenuBo.java
 * Created on 2015年8月5日-下午7:27:26
 */

package com.zoomkey.usercenter.menu;

import java.util.List;

import com.zoomkey.framework.core.GenericBo;

/**
 * 类功能描述：
 */
public interface MenuBo extends GenericBo<Menu, Long> {

	/**
	 * @function:获取用户权限菜单
	 * @param userId 用户id
	 * @param belongSys 所属系统
	 * @return
	 */
	public List<Menu> getUserMenus(Long userId, Integer belongSys);

	/**
	 * 查询系统全部的菜单
	 * @param belongSys 所属系统
	 * @return	List 系统全部的菜单对象list数组
	 */
	public List<Menu> getAllMenu(Integer belongSys);

	/**
	 * @function:根据id串查询菜单
	 * @param menuIds
	 * @return
	 */
	public List<Menu> getMenus(String menuIds);

	/**
	 * @function:组装菜单权限树
	 * @param sysTypes 系统类型（串）
	 * @param userId 用户id 注：若要全部菜单，传0即可
	 * @return
	 */
	public String buildMenuTree(String sysTypes, Long userId);
}
