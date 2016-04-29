
package com.zoomkey.usercenter.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.berheley.oa.exception.BusinessException;
import com.zoomkey.framework.core.GenericBoImpl;
import com.zoomkey.usercenter.menu.Menu;
import com.zoomkey.usercenter.menu.MenuBo;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * <p>RoleManager的实现类，业务相关的方法都需要在RoleManager接口中定义后，在该类中实现。</p>
 * 该类实现RoleManager接口并继承GenericManagerImpl。
 *
 */
@Service("roleBo")
public class RoleBoImpl extends GenericBoImpl<Role, Long> implements RoleBo {

	private final RoleDao	roleDao;

	@Autowired
	private MenuBo				menuBo;

	/**
	 * 利用构造方法注入RoleDao
	 * @param roleDao
	 */
	@Autowired
	public RoleBoImpl(RoleDao roleDao) {
		super(roleDao);
		this.roleDao = roleDao;
	}

	/**
	 * 返回全部角色集合
	 */
	@Override
	public List<Role> getRoles() {
		return this.dao.getAll();
	}

	/**
	 * @function:返回全部角色集合
	 * @param ids 角色id串
	 * @return
	 */
	@Override
	public List<Role> getRoles(String ids) {
		return this.roleDao.getRoles(ids);
	}

	/**
	 * 根据角色名字返回对应角色
	 */
	@Override
	public Role getRole(String rolename) {
		return this.roleDao.getRoleByName(rolename);
	}

	/**
	 * @function:新建角色
	 * @param roleObj
	 */
	@Override
	public Role saveRole(Role roleObj) {
		checkIsExistRole(roleObj);
		this.roleDao.save(roleObj);
		return roleObj;
	}

	/**
	 * @function:修改角色
	 * @param roleObj 角色对象
	 */
	@Override
	public void updateRole(Role roleObj) {
		checkIsExistRole(roleObj);
		Role roleObjInDb = get(roleObj.getId());
		roleObjInDb.setName(roleObj.getName());
		roleObjInDb.setRemark(roleObj.getRemark());
		this.roleDao.save(roleObjInDb);
	}

	/**
	 * @function:删除角色
	 * @param id
	 */
	@Override
	public void deleteRole(Long id) {
		Role roleObj = get(id);
		if (roleObj.getUsers().size() > 0) {
			throw new BusinessException("角色下存在用户，不能删除");
		}
		delete(id);
	}

	/**
	 * @function:设置角色菜单
	 * @param roleId
	 * @param menuIds
	 */
	@Override
	public void saveRoleMenus(Long roleId, String menuIds) {
		Role roleObj = get(roleId);
		List<Menu> menus = this.menuBo.getMenus(menuIds);
		roleObj.setMenus(menus);
		save(roleObj);
		final CacheManager cacheManager = CacheManager.create();
		final Cache urlCache = cacheManager.getCache("urls");
		urlCache.removeAll();
	}

	@Override
	public List<Role> getUgRoleList(Integer belongSys) {
		return this.roleDao.getUgRoleList(belongSys);
	}

	/**
	 * @function:校验该角色是否已存在
	 * @param roleObj
	 */
	private void checkIsExistRole(Role roleObj) {
		Role roleObjInDb = this.roleDao.getRoleByName(roleObj.getName());
		if (roleObjInDb != null) {
			if (roleObj.getId() == null || !roleObj.getId().equals(roleObjInDb.getId())) {
				throw new BusinessException("该角色已存在");
			}
		}
	}
}