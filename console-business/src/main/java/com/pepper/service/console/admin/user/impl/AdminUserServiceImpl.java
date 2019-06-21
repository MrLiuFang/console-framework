package com.pepper.service.console.admin.user.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.pepper.core.Pager;
import com.pepper.core.base.impl.BaseServiceImpl;
import com.pepper.dao.console.admin.user.AdminUserDao;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.role.RoleUser;
import com.pepper.service.console.admin.user.AdminUserService;
import com.pepper.service.console.role.RoleUserService;
import com.pepper.util.Md5Util;


/**
 *
 * @author mrliu
 *
 * @param <T>
 */
@Service(interfaceClass = AdminUserService.class)
public class AdminUserServiceImpl extends BaseServiceImpl<AdminUser> implements AdminUserService{

	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(AdminUserServiceImpl.class);

	@Resource
	private AdminUserDao adminUserDao;

	@Reference
	private RoleUserService roleUserService;

	@Override
	public AdminUser findByAccountAndPassword(String account, String password) {
		return adminUserDao.findByAccountAndPassword(account,Md5Util.encryptPassword(password.toUpperCase(), account));
	}

	@Override
	public Pager<AdminUser> list(Pager<AdminUser> pager) {
		pager = adminUserDao.findNavigator(pager);
		
//		pager = adminUserDao.list(pager, pager.getJpqlParameter().getSearchParameter(), pager.getJpqlParameter().getSortParameter());
		return pager;
	}

	@Override
	public AdminUser findByAccount(String account) {
		return adminUserDao.findByAccount(account);
	}

	@Override
	public void deleteUser(String id) {
		RoleUser roleUsers = roleUserService.findRoleUserByUserId(id);
		roleUserService.delete(roleUsers);
		adminUserDao.deleteById(id);
	}

	@Override
	public AdminUser saveUser(AdminUser adminUser, String roleId) {
		adminUser = adminUserDao.save(adminUser);
		RoleUser roleUser = new RoleUser();
		roleUser.setCreateDate(new Date());
		roleUser.setCreateUser(adminUser.getCreateUser());
		roleUser.setRoleId(roleId);
		roleUser.setUserId(adminUser.getId());
		roleUserService.save(roleUser);
		return adminUser;
	}

	@Override
	public void saveUserRole(RoleUser roleUser) {
		roleUserService.deleteRoleUserByUserId(roleUser.getUserId());
		roleUserService.save(roleUser);
	}

	@Override
	public void updateUser(AdminUser adminUser, String roleId) {
		update(adminUser);
		if (StringUtils.hasText(roleId)) {
			roleUserService.deleteRoleUserByUserId(adminUser.getId());
			RoleUser roleUser = new RoleUser();
			roleUser.setCreateDate(new Date());
			roleUser.setRoleId(roleId);
			roleUser.setUserId(adminUser.getId());
			roleUser.setCreateUser(adminUser.getUpdateUser());
			roleUserService.save(roleUser);
		}
	}

	@Override
	public void updateLoginTime(String userId) {
		List<String> args = new ArrayList<String>();
		args.add(userId);
		
	}

	@Override
	public List<AdminUser> findUserByDepartmentId(String departmentId) {
		return adminUserDao.findUserByDepartmentId(departmentId);
	}

	@Override
	public List<AdminUser> findByDepartmentId(String departmentId,String id) {
		
		return adminUserDao.findByDepartmentIdAndIdNot(departmentId, id);
		
	}

	@Override
	public List<AdminUser> findDepartmentManager(String departmentId) {
		return adminUserDao.findByDepartmentIdAndIsManager(departmentId, true);
	}

	@Override
	public List<AdminUser> findByDepartmentGroupId(String departmentGroupId) {
		return adminUserDao.findByDepartmentGroupId(departmentGroupId);
	}

	@Override
	public List<AdminUser> findByDepartmentGroupId(String departmentGroupId, Boolean isManager) {
		return adminUserDao.findByDepartmentGroupIdAndIsManager(departmentGroupId, isManager);
	}

	@Override
	public Pager<AdminUser> findAdminUser(Pager<AdminUser> pager,String account,String mobile,String email,String name,String departmentId,String departmentGroupId,String roleId,Boolean isWork){
		return adminUserDao.findAdminUser(pager,account, mobile, email, name, departmentId, departmentGroupId, roleId,isWork);
	}

	@Override
	public List<AdminUser> findByDepartmentGroupIdAndIdNot(String departmentGroupId, String id) {
		return adminUserDao.findByDepartmentGroupIdAndIdNot(departmentGroupId, id);
	}

	@Override
	public List<AdminUser> findByDepartmentId(String departmentId, Boolean isManager) {
		
		return adminUserDao.findByDepartmentId(departmentId, isManager);
	}


}
