
package com.zoomkey.usercenter.user;

import java.io.Serializable;
import java.util.List;

import com.zoomkey.framework.core.GenericDao;

/**
 * UserDao接口，继承泛型Dao接口，并规定Model类型<User>和主键类型<Long>
 *
 */
public interface UserDao extends GenericDao<User, Long> {

	/**
	 * 返回登录用户的密码
	 * @param userId the user's id
	 * @return the password in DB, if the user is already persisted
	 */
	String getUserPassword(Long userId);

	/**
	 * 根据用户名返回用户信息
	 * @function:
	 * @param username 要返回的user的用户名
	 * @return
	 */
	User findUserByUserName(String username);

	public List<User> getUsers(Long orgId);

	/**
	 * @function: 根据新建/修改的用户，获取同一机构下的用户对象
	 * @param ugUser 新建/修改的用户对象
	 * @return 用户对象
	 */
	public User getUgUser(User userObj);

	/**
	 * @function:得到系统用户对象
	 * @param loginName 登录名
	 * @return 登录用户对象
	 */
	public User getUgUser(String loginName, Integer... currentSys);

	/**
	 * @function: 获取某机构下的所有用户
	 * @param orgId 机构id
	 * @return 用户list
	 */
	public List<User> getUserList(Integer orgId);

	/**
	 * 
	 * @function:根据roleId集合获取全部UserId
	 * @param roleIds
	 * @return
	 */
	public List<Long> getUserIdsByRole(String roleIds);

	/**
	 * 判断用户是否有某个按钮的权限。
	 * @param userId 用户id
	 * @param code 按钮代码
	 * @return 如果有按钮权限则返回true，否则返回false。
	 */
	public boolean isUserHasControlerPrivilege(Serializable userId, String code);

	List<User> getUserListByOrgId(Long orgId);
}
