package com.pepper.service.console.role.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.util.StringUtils;

import com.pepper.core.Pager;
import com.pepper.core.base.impl.BaseServiceImpl;
import com.pepper.core.constant.SearchConstant;
import com.pepper.dao.console.role.RoleDao;
import com.pepper.model.console.menu.Menu;
import com.pepper.model.console.role.Role;
import com.pepper.model.console.role.RoleMenu;
import com.pepper.service.console.role.RoleMenuService;
import com.pepper.service.console.role.RoleService;

/**
 *
 * @author Mr.Liu
 *
 */
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseServiceImpl<Role> implements RoleService {

	@Reference
	private RoleMenuService roleMenuService;

	@Resource
	private RoleDao roleDao;



	public void delete(Role role) {
		if (role==null) {
			return;
		}
		roleMenuService.deleteByRoleId(role.getId());
		roleDao.delete(role);
	}

	@Override
	public Pager<Role> list(Pager<Role> pager) {
		pager.getJpqlParameter().setSearchParameter(SearchConstant.NOT_EQUAL+"_code", "ADMIN_ROLE");
		return roleDao.findNavigator(pager);
	}

	@Override
	public Role findByCode(String code) {
		return roleDao.findByCode(code);
	}

	@Override
	public void deleteRole(String id) {
		List<RoleMenu> roleMenus = roleMenuService.findByRoleId(id);
		roleMenuService.deleteAll(roleMenus);
		roleDao.deleteById(id);
	}

	@Override
	public void updateRoleMenu(String roleId, String resourceIds) {
		// 删除角色的菜单关联关系
		roleMenuService.deleteByRoleId(roleId);
		// 保存角色菜单
		if (StringUtils.hasText(resourceIds)) {
			List<RoleMenu> roleMenus = new ArrayList<RoleMenu>();
			RoleMenu roleMenu = null;
			for (String id : resourceIds.split(";")) {
				roleMenu = new RoleMenu();
				roleMenu.setCreateDate(new Date());
				roleMenu.setMenuId(id);
				roleMenu.setRoleId(roleId);
				roleMenus.add(roleMenu);
			}
			roleMenuService.saveAll(roleMenus);
		}
	}

	@Override
	public Role saveRole(Role role, String resourceIds) {
		role = roleDao.save(role);
		if (StringUtils.hasText(resourceIds)) {
			List<RoleMenu> roleMenus = new ArrayList<RoleMenu>();
			RoleMenu roleMenu = null;
			for (String id : resourceIds.split(";")) {
				roleMenu = new RoleMenu();
				roleMenu.setCreateDate(new Date());
				roleMenu.setCreateUser(role.getCreateUser());
				roleMenu.setMenuId(id);
				roleMenu.setRoleId(role.getId());
				roleMenus.add(roleMenu);
			}
			roleMenuService.saveAll(roleMenus);
		}
		return role;
	}

	@Override
	public void updateRole(Role role, String resourceIds) {
		roleDao.update(role);
		roleMenuService.deleteByRoleId(role.getId());
		if (StringUtils.hasText(resourceIds)) {
			List<RoleMenu> roleMenus = new ArrayList<RoleMenu>();
			RoleMenu roleMenu = null;
			for (String id : resourceIds.split(";")) {
				roleMenu = new RoleMenu();
				roleMenu.setCreateDate(new Date());
				roleMenu.setCreateUser(role.getUpdateUser());
				roleMenu.setMenuId(id);
				roleMenu.setRoleId(role.getId());
				roleMenus.add(roleMenu);
			}
			roleMenuService.saveAll(roleMenus);
		}

	}

	@Override
	public List<String> queryUserAllResources(String userId) {
		return roleDao.queryUserAllResources(userId);
	}

	@Override
	public List<Menu> queryUserAllMenu(String userId) {
		return roleDao.queryUserAllMenu(userId);
	}

	@Override
	public List<String> queryUserAllMenuCode(String userId) {
		return roleDao.queryUserAllMenuCode(userId);
	}

	@Override
	public Role findByUserId(String userId) {
		return roleDao.findByUserId(userId);
	}

	@Override
	public Role findByName(String name) {
		return roleDao.findByName(name);
	}

	@Override
	public List<Role> findByUserId1(String userId) {
		return roleDao.findByUserId1(userId);
	}


}
