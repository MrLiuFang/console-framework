package com.pepper.dao.console.admin.user;

import com.pepper.core.base.BaseDao;
import com.pepper.model.console.admin.user.AdminUser;
/**
 * 
 * @author mrliu
 *
 * @param <T>
 */
public interface AdminUserDao extends BaseDao<AdminUser> ,AdminUserDaoEx<AdminUser> {

	/**
	 * 根据account和password获取用户。
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	AdminUser findByAccountAndPassword(String account, String password);

	/**
	 * 根据account获取用户
	 * 
	 * @param account
	 */
	AdminUser findByAccount(String account);

}
