package com.pepper.dao.console.admin.user.impl;

import com.pepper.core.Pager;
import com.pepper.core.base.curd.DaoExImpl;
import com.pepper.dao.console.admin.user.AdminUserDaoEx;
import com.pepper.model.console.admin.user.AdminUser;

/**
 * 
 * @author mrliu
 *
 */
public class AdminUserDaoImpl extends DaoExImpl<AdminUser> implements AdminUserDaoEx<AdminUser>{
	

	@Override
	public Pager<AdminUser> list(Pager<AdminUser> pager) {
		return getPepperSimpleJpaRepository().findNavigator(pager);
	}

}
