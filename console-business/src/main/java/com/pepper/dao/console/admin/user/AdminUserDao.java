package com.pepper.dao.console.admin.user;

import org.springframework.data.jpa.repository.Query;

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
	@Query(" from AdminUser where account=?1 and password=?2")
	AdminUser queryAdminUserByAccountPaasword(String account, String password);

	/**
	 * 根据account获取用户
	 * 
	 * @param account
	 */
	@Query(" from AdminUser where account=?1")
	AdminUser findByAccount(String account);

	/**
	 * 获取登录用户总数
	 * @return
	 */
	@Query( value="select  count(1) from t_admin_user where last_login_time is not null and last_login_time >= date_sub(now(),interval 30 minute )" ,nativeQuery=true )
	public Long findOnLineCount();
}
