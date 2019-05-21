package com.pepper.dao.console.admin.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pepper.core.base.BaseDao;
import com.pepper.core.base.curd.DaoExImpl;
import com.pepper.dao.console.admin.user.AdminUserDaoEx;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.enums.UserType;

/**
 * 
 * @author mrliu
 *
 */
public class AdminUserDaoImpl extends DaoExImpl<AdminUser> implements AdminUserDaoEx<AdminUser>{
	

	@Override
	public List<AdminUser> findUserByDepartmentId(String departmentId) {
		BaseDao<AdminUser> baseDao = getPepperSimpleJpaRepository(this.getClass());
		Map<String,Object> searchParameter = new HashMap<String, Object>();
//		String jpql = "SELECT  au from AdminUser au left join RoleUser ru on au.id = ru.userId left join Role r on ru.roleId = r.id "
//				+ " where au.userType = :userType and au.departmentId =:departmentId and r.code = 'EMPLOYEE_ROLE' ";
		String jpql = "SELECT  au from AdminUser au left join RoleUser ru on au.id = ru.userId left join Role r on ru.roleId = r.id "
				+ " where au.userType = :userType  and r.code = 'EMPLOYEE_ROLE' ";
		searchParameter.put("userType", UserType.EMPLOYEE);
//		searchParameter.put("departmentId", departmentId);
		List<AdminUser> list = baseDao.find(jpql, searchParameter);
		return list;
	}
	
	

}
