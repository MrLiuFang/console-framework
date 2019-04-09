package com.pepper.dao.console.admin.user;

import java.util.List;

/**
 * 
 * @author mrliu
 *
 */
public interface AdminUserDaoEx<T> {

	public List<T> findUserByBepartmentId(String departmentId);
}
