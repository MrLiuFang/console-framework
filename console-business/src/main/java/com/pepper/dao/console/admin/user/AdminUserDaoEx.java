package com.pepper.dao.console.admin.user;

import com.pepper.core.Pager;

/**
 * 
 * @author mrliu
 *
 */
public interface AdminUserDaoEx<T> {

	public Pager<T> list(Pager<T> pager);
}
