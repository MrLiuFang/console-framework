package com.pepper.dao.console.admin.user;

import com.pepper.core.Pager;
import com.pepper.model.console.admin.user.AdminUser;

/**
 * 
 * @author mrliu
 *
 */
public interface AdminUserDaoEx {

	public Pager<AdminUser> list(Pager<AdminUser> pager);
}
