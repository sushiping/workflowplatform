
package com.zoomkey.usercenter.user;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.berheley.oa.exception.BusinessException;
import com.zoomkey.core.common.EnumDefine;
import com.zoomkey.framework.core.GenericBoImpl;
import com.zoomkey.framework.util.ConverterUtil;
import com.zoomkey.framework.util.LogUtil;
import com.zoomkey.framework.util.StringUtil;
import com.zoomkey.usercenter.role.Role;
import com.zoomkey.usercenter.role.RoleBo;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 *<p>UserManager的实现类，业务相关的方法都需要在UserManager接口中定义后，在该类中实现。</p>
 * 该类实现UserManager接口并继承GenericManagerImpl。
 */
@Service("userBo")
public class UserBoImpl extends GenericBoImpl<User, Long> implements UserBo, UserDetailsService {

	private Md5PasswordEncoder	passwordEncoder;

	private SaltSource			saltSource;

	private UserDao				userDao;

	private RoleBo					roleBo;

	@Autowired
	@Qualifier("passwordEncoder")
	public void setPasswordEncoder(final Md5PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Autowired
	@Qualifier("saltSource")
	public void setSaltSource(final SaltSource saltSource) {
		this.saltSource = saltSource;
	}

	@Autowired
	public void setUserDao(final UserDao userDao) {
		this.dao = userDao;
		this.userDao = userDao;
	}

	/**
	 * 返回全部user集合，相当于获取表中全部数据
	 */
	@Override
	public List<User> getUsers() {
		return this.userDao.getAllDistinct();
	}

	/**
	 * 保存用户方法，如果用户已经存在，则抛出UserExistsException异常
	 */
	@Override
	public void saveUser(final User user) throws BusinessException {
		final String newUsername = user.getUsername().toLowerCase();
		// 先验证用户名是否已经存在，如果存在则抛出UserExistsException
		final User oldUser = getUserByUsername(newUsername);
		if (oldUser != null) {
			throw new BusinessException("用户 '" + newUsername + "' 已经存在！");
		}
		final User newUser = new User();
		newUser.setUsername(newUsername);// 设定user用户名，转化小写
		newUser.setPassword(user.getPassword());// 设定密码
		newUser.setIsValid('1');
		newUser.setMobile(user.getMobile());
		try {
			this.userDao.save(newUser);
		} catch (final Exception e) {
			this.log.error(LogUtil.stackTraceToString(e));
		}
	}

	/**
	 * @function:根据用户名返回对应user对象
	 * @param username 要查询的对应用户名
	 * @return 返回该用户名的user对象
	 */
	@Override
	public User getUserByUsername(String username) {
		return this.userDao.findUserByUserName(username);
	}

	/**
	 * @function:security登陆后进入的方法，继承UserDetails接口需要重写该方法，用于进行特殊处理
	 * @param username 登录时用户名，前端传来的记录
	 * @return 返回对应UserDetails，用于验证是否登录成功
	 * @throws UsernameNotFoundException 如果用户不存在则抛出UsernameNotFoundException异常
	 */
	@Override
	@Cacheable("userCache")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = this.userDao.findUserByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException("user '" + username + "' not found...");
		} else {
			final Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
			for (final Role role : user.getRoles()) {
				final GrantedAuthority ga = new SimpleGrantedAuthority(role.getId().toString());
				authorities.add(ga);
			}
			user.setAuthorities(authorities);
			return user;
		}
	}

	@Override
	public List<User> getAllUsersForShow() {
		final List<User> users = getUsers();
		if (users == null || users.size() <= 0) {
			return null;
		}
		return users;
	}

	@Override
	public void optAssignRole(Long[] userIds, Long[] roleIds) {
		for (final Long uId : userIds) {
			final User user = get(uId);
			// 先清空原有角色绑定关系，重新添加新角色
			user.getRoles().clear();
			for (final Long rId : roleIds) {
				final Role role = this.roleBo.get(rId);
				user.getRoles().add(role);
			}
			this.userDao.save(user);
		}
	}

	/**
	 * @function: 获取某机构下的所有用户
	 * @param orgId 机构id
	 * @return 用户list
	 */
	@Override
	public List<User> getUserList(Long orgId) {
		return this.userDao.getUsers(orgId);
	}

	@Override
	public Boolean checkPasswordRight(String pagePassword, Long userId) {
		final User userObj = get(userId);
		// 编码加密转化
		pagePassword = this.passwordEncoder.encodePassword(pagePassword, this.saltSource.getSalt(userObj));
		return pagePassword.equals(userObj.getPassword());
	}

	@Override
	public void optModifyUserPassword(User userObj) {
		final User userObjOrig = get(userObj.getId());
		if (userObjOrig == null) {
			throw new BusinessException("系统用户不存在");
		}
		if (StringUtil.isNullOrEmpty(userObj.getPassword())) {
			throw new BusinessException("系统用户密码为空");
		}
		userObjOrig
			.setPassword(this.passwordEncoder.encodePassword(userObj.getPassword(), this.saltSource.getSalt(userObjOrig)));
		userObj.setUsername(userObjOrig.getUsername());
		this.userDao.save(userObjOrig);
	}

	@Override
	public void saveUser(User userObj, String roleIds) {
		checkExistUser(userObj);
		setUserInfo(userObj, true);
		// 因为密码加密需要用到用户ID，故再此先进行数据库建立用户
		save(userObj);
		userObj.setPassword(this.passwordEncoder.encodePassword(userObj.getPassword(), this.saltSource.getSalt(userObj)));
		// 把加密后的密码重新进行保存
		final List<Role> roles = this.roleBo.getRoles(roleIds);
		// 保存权限角色
		userObj.setRoles(roles);
		save(userObj);
		// 更新user缓存
		updateUserCache(userObj.getUsername());
	}

	/**
	 * @function: 校验是否存在用户
	 * @param ugUser 新建/修改的用户对象
	 * @return 存在返回false
	 */
	private void checkExistUser(User ugUser) {
		final User userObj = this.userDao.getUgUser(ugUser);
		if (userObj != null && !userObj.getId().equals(ugUser.getId())) {
			throw new BusinessException("该用户名已经存在");
		}
		final User loginUserObj = this.userDao.getUgUser(ugUser.getUsername());
		if (loginUserObj != null && !loginUserObj.getId().equals(ugUser.getId())) {
			throw new BusinessException("该登录名已经存在");
		}
	}

	/**
	 * @function: 填充用户信息
	 * @param ugUser 用户对象
	 * @param flag true为新建,false为修改
	 * @return
	 */
	private User setUserInfo(User ugUser, boolean flag) {
		if (EnumDefine.IsOrNot.是.getValue().equals(ugUser.getIsDisabled())) {
			ugUser.setIsDisabled(1);
		} else {
			ugUser.setIsDisabled(0);
		}
		if (flag) {
			ugUser.setIsValid(1);
		}
		// 用户所属数据过滤机构(用于数据过滤)
		// ugUser.setLimitHeatingId(getLimitUgOrgId(ugUser.getUgOrgId()));
		return ugUser;
	}

	/**
	 * @function: 修改用户信息(包括权限角色)
	 * @param ugUser 用户对象
	 * @param roleIds 权限角色ID
	 */
	@Override
	public void updateUser(User ugUser, String roleIds) {
		checkExistUser(ugUser);
		final User userObj = get(ugUser.getId());
		if (userObj == null) {
			throw new BusinessException("系统用户不存在");
		}
		setUserInfo(ugUser, false);
		// change
		userObj.setOrgId(ugUser.getOrgId());
		userObj.setName(ugUser.getName());
		userObj.setUsername(ugUser.getUsername());
		userObj.setMobile(ugUser.getMobile());
		userObj.setIsDisabled(ugUser.getIsDisabled());
		userObj.getRoles().clear();
		final List<Role> roles = this.roleBo.getRoles(roleIds);
		// 保存权限角色
		userObj.setRoles(roles);
		save(userObj);
		// 更新user缓存
		updateUserCache(userObj.getUsername());
	}

	/**
	 * @function:删除用户
	 * @param userId 用户id
	 */
	@Override
	public void deleteUser(Long userId) {
		final User userObj = get(userId);
		delete(userObj);
	}

	/**
	 * @function: 重置用户密码
	 * @param userId 用户ID
	 * @param passwad 新密码
	 */
	@Override
	public void optResetPassword(Long userId, String passwad) {
		final User userObj = get(userId);
		userObj.setPassword(this.passwordEncoder.encodePassword(passwad, this.saltSource.getSalt(userObj)));
		save(userObj);
		// 更新cache缓存
		updateUserCache(userObj.getUsername());
	}

	@Override
	public List<Role> getUserRoleByUserId(Long id) {
		final User userObj = this.userDao.get(id);
		return userObj.getRoles();
	}

	/**
	 * @function:获取角色树
	 * @param userId 用户id
	 * @return
	 */
	@Override
	public String getUserRoleTree(Long userId, Integer belongSys) {
		List<Role> userRoles = new ArrayList<Role>();
		try {
			final User userObj = get(userId);
			userRoles = userObj.getRoles();
		} catch (final Exception e) {
		}
		// IMenuAssignBO man = (IMenuAssignBO) getService(ServiceDefine.SYS_MENU_ASSIGN_SERVICE);
		// List<THelpInfo> helpInfoList = this.roleDao.loadAll(THelpInfo.class);
		// if (helpInfoList.size() == 1) {// 只使用一个系统时不会显示系统名称节点
		// List<Map<String, String>> roleList = man.getSystemRole(helpInfoList.get(0).getBelongSys());
		// return man.roleMenuToJson(roleList, roles);
		// } else if (helpInfoList.size() > 1) {// 同时使用多个系统时显示系统名称节点
		// StringBuilder roleTreeStr = new StringBuilder();
		// for (THelpInfo helpInfo : helpInfoList) {
		// roleTreeStr.append(",{");
		// roleTreeStr.append("id:'bizRoot_" + helpInfo.getBelongSys() + "',");
		// // roleTreeStr.append("text:'" +
		// // ConstantEnumDefine.BELONG_SYS.getLabel(helpInfo.getBelongSys()) + "',");
		// roleTreeStr.append("children:" +
		// man.roleMenuToJson(man.getSystemRole(helpInfo.getBelongSys()), roles));
		// roleTreeStr.append("}");
		// }
		// return "[" + roleTreeStr.deleteCharAt(0) + "]";
		// } else {
		// 无系统信息时取当前系统下角色（原则上不会出现这种情况，新系统上线时必须同时添加THelpInfo表系统信息）
		final List<Role> allRoles = this.roleBo.getUgRoleList(belongSys);
		return roleToJson(allRoles, userRoles);
		// }
	}

	public String roleToJson(List<Role> allRoles, List<Role> userRoles) {
		final StringBuilder json = new StringBuilder("[");
		for (int i = 0; i < allRoles.size(); i++) {
			final Role role = allRoles.get(i);
			json.append("{'id':");
			json.append(role.getId());
			json.append(",'text':'");
			json.append(role.getName());
			json.append("','qtip':'");
			json.append(ConverterUtil.nullObjectToString(role.getRemark()));
			json.append("','leaf': true");
			for (final Role userRole : userRoles) {
				if (role.getId().equals(userRole.getId())) {
					json.append(",'checked':true");
				}
			}
			json.append("},");
		}
		if (json.charAt(json.length() - 1) == ',') {
			json.deleteCharAt(json.length() - 1);
		}
		json.append("]");
		return json.toString();
	}

	@Override
	public List<Long> getUserIdsByRole(String roleIds) {
		return this.userDao.getUserIdsByRole(roleIds);
	}

	@Autowired
	public void setRoleBo(RoleBo roleBo) {
		this.roleBo = roleBo;
	}

	@Override
	public void modifyNameAndMobile(User user) {
		Long userId = user.getId();
		User oldUser = this.userDao.get(userId);
		oldUser.setName(user.getName());
		oldUser.setMobile(user.getMobile());
		this.userDao.save(oldUser);
		// 更新cache缓存
		updateUserCache(oldUser.getUsername());
	}

	/**
	 * 
	 * @function:该方法用于更新用户缓存
	 * @param userName
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void updateUserCache(String userName) {
		// 更新cache缓存
		final CacheManager cacheManager = CacheManager.create();
		final Cache userCache = cacheManager.getCache("userCache");
		final Cache webresdbCache = cacheManager.getCache("webresdbCache");
		final List keys = userCache.getKeys();
		for (final Object key : keys) {
			if (!key.toString().equals(userName)) {
				continue;
			}
			userCache.remove(key);
		}
		final List webkeys = webresdbCache.getKeys();
		for (final Object webkey : webkeys) {
			if (!webkey.toString().equals(userName)) {
				continue;
			}
			webresdbCache.remove(webkey);
		}
	}

	@Override
	public List<User> getUserListByOrgId(Long orgId) {
		return this.userDao.getUserListByOrgId(orgId);
	}
}
