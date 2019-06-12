package com.pepper.dao.console.admin.user;

import java.util.List;

import com.pepper.core.Pager;
import com.pepper.model.console.admin.user.AdminUser;

/**
 * 
 * @author mrliu
 *
 */
public interface AdminUserDaoEx<T> {

	public List<T> findUserByDepartmentId(String departmentId);
	
	List<AdminUser> findByDepartmentId(String departmentId,Boolean isManager);
	
	public Pager<AdminUser> findAdminUser(Pager<AdminUser> pager,String account,String mobile,String email,String name,String departmentId,String departmentGroupId,String roleId);
}
