/*
 * Copyright: Tianjin Zoomkey Software Co,.ltd, China
 * ems
 * com.zoomkey.ems.menu.MenuBo.java
 * Created on 2015年8月5日-下午7:31:10
 */

package com.zoomkey.usercenter.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;

import com.zoomkey.core.security.SecuredUrlDefinition;
import com.zoomkey.framework.core.GenericBoImpl;
import com.zoomkey.usercenter.role.Role;

/**
 * 类功能描述：
 */
@Service("menuBo")
public class MenuBoImpl extends GenericBoImpl<Menu, Long> implements MenuBo, SecuredUrlDefinition<ConfigAttribute> {

	@Autowired
	private MenuDao menuDao;

	@Override
	@Cacheable("urls")
	public Map<String, Collection<ConfigAttribute>> getSecuredUrlDefinition() {
		final String hql = "FROM Menu WHERE url IS NOT NULL AND url <> ''";
		final List<Menu> menus = this.menuDao.query(hql);
		final Map<String, Collection<ConfigAttribute>> definition = new HashMap<String, Collection<ConfigAttribute>>();
		for (final Menu menu : menus) {
			final Collection<ConfigAttribute> attributes = new LinkedList<ConfigAttribute>();
			for (final Role role : menu.getRoles()) {
				final ConfigAttribute attribute = new SecurityConfig(role.getId().toString());
				attributes.add(attribute);
			}
			definition.put(menu.getUrl(), attributes);
		}
		return definition;
	}

	/**
	 * @function:组装菜单权限树
	 * @param sysTypes 系统类型（串）
	 * @param userId 用户id 注：若要全部菜单，传0即可
	 * @return
	 */
	@Override
	public String buildMenuTree(String sysTypes, Long userId) {
		String[] sysTypeArr = sysTypes.split(",");
		if (sysTypeArr.length == 1) {// 只使用一个系统时不会显示系统名称节点
			Integer belongSys = Integer.parseInt(sysTypeArr[0]);
			List<Menu> allMenuList = getAllMenu(belongSys);
			List<Menu> userMenuList = this.menuDao.getUserMenus(userId, belongSys);
			return getTreeJsonByMenuList(allMenuList, userMenuList);
		} else if (sysTypeArr.length > 1) {// 同时使用多个系统时显示系统名称节点
			StringBuilder menuTreeStr = new StringBuilder();
			for (String sysType : sysTypeArr) {
				menuTreeStr.append(",{");
				menuTreeStr.append("id:'bizRoot_" + sysType + "',");
				// TODO:
				// menuTreeStr.append("text:'" + ConstantEnumDefine.BELONG_SYS.getLabel(sysType) +
				// "',");
				List<Menu> allMenuList = getAllMenu(Integer.parseInt(sysType));
				List<Menu> userMenuList = this.menuDao.getUserMenus(userId, Integer.parseInt(sysType));
				menuTreeStr.append("children:" + getTreeJsonByMenuList(allMenuList, userMenuList));
				menuTreeStr.append("}");
			}
			return "[" + menuTreeStr.deleteCharAt(0) + "]";
		}
		return "";
	}

	/**
	 * @function:获取用户权限菜单
	 * @param userId 用户id
	 * @param belongSys 所属系统
	 * @return
	 */
	@Override
	public List<Menu> getUserMenus(Long userId, Integer belongSys) {
		return this.menuDao.getUserMenus(userId, belongSys);
	}

	/**
	 * 查询系统全部的菜单
	 * @param belongSys 所属系统
	 * @return	List 系统全部的菜单对象list数组
	 */
	@Override
	public List<Menu> getAllMenu(Integer belongSys) {
		List<Menu> menuList = this.menuDao.getAllMenu(belongSys);
		return menuList;
	}

	/**
	 * @function:根据id串查询菜单
	 * @param menuIds
	 * @return
	 */
	@Override
	public List<Menu> getMenus(String menuIds) {
		return this.menuDao.getMenus(menuIds);
	}

	/**
	 * @function:根据系统权限list组装json,用于前台ext tree显示
	 * @param menuList 系统权限list
	 * @return String 系统权限list组装的json
	 */
	private String getTreeJsonByMenuList(List<Menu> menuList, List<Menu> userMenuList) {
		List<Map<String, Object>> parentMenuList = new ArrayList<Map<String, Object>>();
		// 将一级权限装到parentMenuList中
		for (Menu menu : menuList) {
			if (menu.getParentId() == null) {
				Map<String, Object> parentMap = new HashMap<String, Object>();
				parentMap.put("id", menu.getId());
				parentMap.put("name", menu.getName());
				parentMenuList.add(parentMap);
			}
		}
		StringBuilder json = new StringBuilder("[");
		// 按照父子节点，组转json
		for (int i = 0; i < parentMenuList.size(); i++) {
			Map<String, Object> parentMap = parentMenuList.get(i);
			json.append("{text:'");
			json.append(parentMap.get("name"));
			json.append("',id:");
			json.append(parentMap.get("id"));
			// 对权限树进行展开禁用加工
			appendTreeJson(json, userMenuList, (Long) parentMap.get("id"), true);
			json.append(",'children':[");
			StringBuilder json1 = new StringBuilder("");
			for (Menu menu : menuList) {
				if (menu.getParentId() != null && menu.getParentId().toString().equals(parentMap.get("id").toString())) {
					json1.append("{text:'");
					json1.append(menu.getName());
					json1.append("',id:'");
					json1.append(menu.getId());
					json1.append("','leaf': true");
					// 对权限树进行展开禁用加工
					appendTreeJson(json1, userMenuList, menu.getId(), false);
					json1.append("},");
				}
			}
			if (json1.length() >= 1) {
				json.append(json1.substring(0, json1.length() - 1));
			}
			json.append("]},");
		}
		// 删除最后一个逗号
		if (json.charAt(json.length() - 1) == ',') {
			json.deleteCharAt(json.length() - 1);
		}
		json.append("]");
		return json.toString();
	}

	/**
	 * @function: 对权限树进行展开禁用加工
	 * @param json 树json
	 * @param userMenuList 用户所拥有的权限菜单
	 * @param menuId 权限菜单ID
	 * @param flag true为父节点,false为子节点
	 * @return
	 */
	private StringBuilder appendTreeJson(StringBuilder json, List<Menu> userMenuList, Long menuId, boolean flag) {
		for (Menu menu : userMenuList) {
			if (menu.getId().equals(menuId)) {
				if (flag) {
					json.append(",checked:true,expanded:true");
				} else {
					json.append(",checked:true");
				}
			}
		}
		return json;
	}
}
