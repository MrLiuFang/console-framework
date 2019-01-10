package com.pepper.dao.console.admin.user.impl;

import com.pepper.core.Pager;
import com.pepper.core.base.curd.SelectRepository;
import com.pepper.core.base.curd.impl.SelectRepositoryImpl;
import com.pepper.dao.console.admin.user.AdminUserDaoEx;
import com.pepper.model.console.admin.user.AdminUser;

/**
 * 
 * @author mrliu
 *
 */
public class AdminUserDaoImpl extends SelectRepositoryImpl<AdminUser>  implements AdminUserDaoEx ,SelectRepository<AdminUser> {
	
	//可采用继承或者注入的方式调用封装的Repository
//	@Resource
//	private SelectRepository<AdminUser> selectRepository;

	@Override
	public Pager<AdminUser> list(Pager<AdminUser> pager) {
		
		return findNavigator(pager);
		
//		return selectRepository.findNavigator(pager, searchParameter, sortParameter);
	}

}
