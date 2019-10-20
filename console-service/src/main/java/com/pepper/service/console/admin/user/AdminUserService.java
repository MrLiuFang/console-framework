package com.pepper.service.console.admin.user;

import java.util.List;

import com.pepper.common.emuns.Status;
import com.pepper.core.Pager;
import com.pepper.core.base.BaseService;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.role.RoleUser;

/**
 * pc后台管理用户DUBBO服务
 * 
 * @author mrliu
 *
 */
public interface AdminUserService extends BaseService<AdminUser> {

	/**
	 * 根据用户帐号和密码查找用户
	 * 
	 * @param account
	 * @param password
	 * @return
	 */
	public AdminUser findByAccountAndPassword(String account, String password);

	/**
	 * 根据account获取用户
	 * 
	 * @param account
	 */
	public AdminUser findByAccount(String account);

	/**
	 * 删除用户，同时删除角色关联表记录
	 * 
	 * @param id
	 */
	public void deleteUser(String id);

	/**
	 * 保存用户以及它的用户权限
	 * 
	 * @param adminUser
	 * @param roleId
	 */
	public AdminUser saveUser(AdminUser adminUser, String roleId);

	/**
	 * 修改用户角色，先删除原来的角色关系，然后插入新的角色关系
	 * 
	 * @param roleUser
	 */
	public void saveUserRole(RoleUser roleUser);

	/**
	 * 更新用户及其角色
	 * 
	 * @param adminUser
	 */
	public void updateUser(AdminUser adminUser, String roleId);

	/**
	 * 列表
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param searchParameter
	 * @param sortParameter
	 * @param class1
	 * @param user
	 * @return
	 */
	public Pager<AdminUser> list(Pager<AdminUser> pager);
	
	/**
	 * 更新用户登录时间
	 * @param userId
	 */
	public void updateLoginTime(String userId);
	
	List<AdminUser> findByDepartmentGroupId(String departmentGroupId);
	
	List<AdminUser> findByDepartmentGroupId(String departmentGroupId,Boolean  isManager);
	
	List<AdminUser> findByDepartmentGroupIdAndIdNot(String departmentGroupId,String id);
	
	public List<AdminUser> findUserByDepartmentId(String departmentId);
	
	public List<AdminUser> findByDepartmentId(String departmentId,String id);
	
	public List<AdminUser> findDepartmentManager(String departmentId);
	
	List<AdminUser> findByDepartmentId(String departmentId,Boolean isManager);
	
	List<AdminUser> findByDepartmentIdAndIsManagerAndDepartmentGroupIdIsNullOrDepartmentGroupId(String departmentId,Boolean isManager );
	
	public Pager<AdminUser> findAdminUser(Pager<AdminUser> pager,String account,String mobile,String email,String name,String departmentId,String departmentGroupId,String roleId,Boolean isWork,Status status,String roleCode,String keyWord);
}
