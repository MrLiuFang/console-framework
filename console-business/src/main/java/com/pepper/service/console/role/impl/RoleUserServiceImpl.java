package com.pepper.service.console.role.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Service;

import com.pepper.core.base.impl.BaseServiceImpl;
import com.pepper.dao.console.role.RoleUserDao;
import com.pepper.model.console.role.RoleUser;
import com.pepper.service.console.role.RoleService;
import com.pepper.service.console.role.RoleUserService;


/**
 *
 * @author Mr.Liu
 *
 */
@Service(interfaceClass = RoleUserService.class)
public class RoleUserServiceImpl extends BaseServiceImpl<RoleUser> implements RoleUserService {


	@Resource
	private RoleService roleService;

	@Resource
	private RoleUserDao roleUserDao;

	@Override
	public void deleteRoleUserByUserId(String userId) {
		RoleUser roleUser = roleUserDao.findByUserId(userId);
		if (roleUser != null) {
			roleUserDao.delete(roleUser);
		}
	}
	@Override
	public RoleUser findByUserId(String userId) {
		return roleUserDao.findByUserId(userId);
	}

	@Override
	public RoleUser findByRoleIdAndUserId(String roleId, String userId) {
		return roleUserDao.findByRoleIdAndUserId(roleId, userId);
	}

	@Override
	public List<RoleUser> findByRoleId(String id) {
		return roleUserDao.findByRoleId(id);
	}
	@Override
	public RoleUser findRoleUserByUserId(String userId) {
		return roleUserDao.findByUserId(userId);
	}

}
