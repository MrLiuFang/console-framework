package com.pepper.service.console.role.impl;

import java.util.List;
import javax.annotation.Resource;
import com.alibaba.dubbo.config.annotation.Service;
import com.pepper.core.base.impl.BaseServiceImpl;
import com.pepper.dao.console.role.RoleMenuDao;
import com.pepper.model.console.role.RoleMenu;
import com.pepper.service.console.role.RoleMenuService;

/**
 *
 * @author Mr.Liu
 *
 * @param <T>
 */
@Service(interfaceClass = RoleMenuService.class)
public class RoleMenuServiceImpl extends BaseServiceImpl<RoleMenu> implements RoleMenuService {

	@Resource
	private RoleMenuDao roleMenuDao;

	@Override
	public void deleteByMenuId(String menuId) {
		List<RoleMenu> roleMenus = roleMenuDao.findByMenuId(menuId);
		roleMenuDao.deleteAll(roleMenus);
	}

	@Override
	public void deleteByRoleId(String roleId) {
		List<RoleMenu> roleMenus = roleMenuDao.findByRoleId(roleId);
		roleMenuDao.deleteAll(roleMenus);
	}

	@Override
	public RoleMenu findByRoleAndMenu(String roleId, String menuId) {
		return roleMenuDao.findByRoleIdAndMenuId(roleId, menuId);
	}

	@Override
	public List<String> findMenuIdsByRoleId(String roleId) {
		return roleMenuDao.findMenuIdsByRoleId(roleId);
	}

	@Override
	public List<RoleMenu> findByMenuId(String menuId) {
		return roleMenuDao.findByMenuId(menuId);
	}

	@Override
	public List<RoleMenu> findByRoleId(String roleId) {
		return roleMenuDao.findByRoleId(roleId);
	}

}
