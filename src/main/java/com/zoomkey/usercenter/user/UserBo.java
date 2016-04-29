
package com.zoomkey.usercenter.user;

import java.util.List;

import com.berheley.oa.exception.BusinessException;
import com.zoomkey.framework.core.GenericBo;
import com.zoomkey.usercenter.role.Role;

/**
 *<p>UserService接口，用于连接持久层（Dao）和表现层（Action）
 * 针对User进行相关的业务逻辑都放在该类中。</p>
 * 该类继承自泛型Service（GenericManager）
 *
 */
public interface UserBo extends GenericBo<User, Long> {

	/**
	 * 根据用户名返回对应的user对象
	 * @param username 用户登陆的用户名
	 * @return User 用户名对应的user对象
	 */
	User getUserByUsername(String username);

	/**
	 * 返回全部user集合，相当于获取user表中全部数据
	 * @return List user集合
	 */
	List<User> getUsers();

	/**
	 * 保存用户信息方法，如果用户已经存在，则抛出UserExistsException异常
	 *
	 * @param user 要保存的用户信息
	 * @return user 返回新增或更新后的user对象
	 */
	void saveUser(User user) throws BusinessException;

	/**
	 * 
	 * @function:该方法用于返回user列表，用于前端显示
	 * @return
	 */
	List<User> getAllUsersForShow();

	/**
	 * 
	 * @function:该方法用于进行角色分配处理，根据用户id和角色id集合进行分配处理
	 * @param userIds 要分配的用户id集合
	 * @param roleIds 分配的角色id集合
	 * @throws Exception
	 */
	void optAssignRole(Long[] userIds, Long[] roleIds) throws Exception;

	/**
	 * @function: 获取某机构下的所有用户
	 * @param orgId 机构id
	 * @return 用户list
	 */
	public List<User> getUserList(Long orgId);

	public Boolean checkPasswordRight(String pagePassword, Long userId);

	public void optModifyUserPassword(User userObj);

	public void saveUser(User ugUser, String roleIds);

	/**
	 * @function: 修改用户信息(包括权限角色)
	 * @param ugUser 用户对象
	 * @param roleIds 权限角色ID
	 */
	public void updateUser(User ugUser, String roleIds);

	/**
	 * @function:删除用户
	 * @param userId 用户id
	 */
	public void deleteUser(Long userId);

	/**
	 * @function: 重置用户密码
	 * @param userId 用户ID
	 * @param passwad 新密码
	 */
	public void optResetPassword(Long userId, String passwad);

	public List<Role> getUserRoleByUserId(Long id);

	/**
	 * @function:获取角色树
	 * @param userId 用户id
	 * @return
	 */
	public String getUserRoleTree(Long userId, Integer belongSys);

	/**
	 * 
	 * @function:根据roleId集合获取全部UserId
	 * @param roleIds
	 * @return
	 */
	public List<Long> getUserIdsByRole(String roleIds);

	/**
	 * 
	 * @function:移动端传来修改用户名和手机号方法
	 * @param user
	 */
	void modifyNameAndMobile(User user);

	/**
	 * 
	 * @function:该方法用于刷新用户缓存信息
	 * @param userName
	 */
	void updateUserCache(String userName);

	/**
	 * 
	 * @function:该方法用于根据组织机构id获取对应user信息
	 * @param orgId
	 * @return
	 */
	List<User> getUserListByOrgId(Long orgId);
}
