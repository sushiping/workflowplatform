
package com.zoomkey.usercenter.user;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Table;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.zoomkey.framework.core.GenericDaoImpl;

/**
 * UserDao的实现类，继承了GenericDaoHibernate，可以对user进行保存删除编辑等相关操作。
 */
@Repository("userDao")
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {

	/**
	 * 构造方法设置Dao的Model对象
	 */
	public UserDaoImpl() {
		super(User.class);
	}

	/**
	 * 重写save方法，用于保存user记录，不刷新缓存
	 *
	 * @param user 要保存的user对象
	 * @return 返回保存的user对象，如果更新则返回更新后对象
	 */
	@Override
	public User save(User user) {
		getSession().saveOrUpdate(user);
		return user;
	}

	/**
	 * 根据user的id返回用户密码
	 */
	@Override
	public String getUserPassword(Long userId) {
		final JdbcTemplate jdbcTemplate = new JdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
		final Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
		return jdbcTemplate.queryForObject("select password from " + table.name() + " where id=?", String.class, userId);
	}

	/**
	 * 根据用户名，返回对应user对象
	 * @function:
	 * @param username 用户名
	 * @return 该用户名对应的user对象
	 */
	@SuppressWarnings("unchecked")
	@Override
	public User findUserByUserName(String username) {
		final List<User> list = getSession().createCriteria(User.class)
			.add(Restrictions.eq("username", username))
			.add(Restrictions.eq("isValid", 1))
			.list();
		return CollectionUtils.isEmpty(list) ? null : list.get(0);
	}

	@Override
	public List<User> getUsers(Long orgId) {
		final String hql = "FROM User WHERE isValid = 1 AND orgId=?";
		final Object[] args = {
					orgId};
		return query(hql, args);
	}

	/**
	 * @function: 根据新建/修改的用户，获取同一机构下的用户对象
	 * @param ugUser 新建/修改的用户对象
	 * @return 用户对象
	 */
	@Override
	public User getUgUser(User userObj) {
		final StringBuilder hql = new StringBuilder();
		hql.append("FROM User WHERE name=? AND orgId=? AND isValid = 1");
		final Object[] args = {
					userObj.getName(), userObj.getOrgId()};
		return queryUniqueObject(hql.toString(), args);
	}

	/**
	 * @function:得到系统用户对象
	 * @param loginName 登录名
	 * @return 登录用户对象
	 */
	@Override
	public User getUgUser(String loginName, Integer... currentSys) {
		String hql = "FROM User WHERE username=? AND isValid = 1";
		if (currentSys.length > 0) {
			hql += " AND " + currentSys[0] + " IN (accessableSys)";
		}
		final Object[] args = {
					loginName};
		return queryUniqueObject(hql, args);
	}

	/**
	 * @function: 获取某机构下的所有用户
	 * @param orgId 机构id
	 * @return 用户list
	 */
	@Override
	public List<User> getUserList(Integer orgId) {
		final String hql = "FROM User uu WHERE uu.isValid = 1 AND uu.orgId = ?";
		// 参数值
		final Object[] args = {
					orgId};
		return query(hql, args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getUserIdsByRole(String roleIds) {
		final StringBuilder hql = new StringBuilder("SELECT u.id FROM User u INNER JOIN u.roles r WHERE r.id in (")
			.append(roleIds).append(") AND u.isValid = 1 ");
		final List<Long> list = (List<Long>) queryByHqlForObj(hql.toString());
		return list;
	}

	@Override
	public boolean isUserHasControlerPrivilege(Serializable userId, String code) {
		String hql = "from User u left join u.roles r left join r.menus m where u.id = ? and m.code = ?";
		List<User> l = query(hql, userId, code);
		if (l.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<User> getUserListByOrgId(Long orgId) {
		StringBuilder hql = new StringBuilder("SELECT u FROM User u WHERE u.orgId = ? ");
		List<User> u = query(hql.toString(), orgId);
		return u;
	}
}
