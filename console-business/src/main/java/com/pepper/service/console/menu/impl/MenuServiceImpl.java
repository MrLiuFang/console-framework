package com.pepper.service.console.menu.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

import com.pepper.common.emuns.Scope;
import com.pepper.common.emuns.Status;
import com.pepper.core.base.impl.BaseServiceImpl;
import com.pepper.core.constant.SearchConstant;
import com.pepper.dao.console.menu.MenuDao;
import com.pepper.model.console.enums.MunuLevel;
import com.pepper.model.console.enums.MenuType;
import com.pepper.model.console.menu.Menu;
import com.pepper.model.console.menu.MenuVo;
import com.pepper.service.console.menu.MenuService;
import com.pepper.service.console.role.RoleMenuService;

import javassist.expr.NewArray;

/**
 *
 * @author Mr.Liu
 *
 * @param <T>
 */
@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl extends BaseServiceImpl<Menu> implements MenuService {

	@Resource
	private MenuDao menuDao;
	
	@Reference
	private RoleMenuService roleMenuService;

	@Override
	public List<Menu> queryRoleChildMenu(String parentMenuId, String roleId, Status status) {
		return menuDao.queryRoleChildMenu(parentMenuId, roleId, status);
	}

	@Override
	public List<Menu> queryRootMenuByRoleId(String roleId, Status status) {
		return menuDao.queryRoleRootMenuByRoleId(roleId, status);
	}

	@Override
	public Menu findByCode(String code) {
		return menuDao.findOneByCode(code);
	}

	@Override
	public List<Menu> getMenuTreeList(Map<String, Object> searchParameter) {
		List<Menu> returnList = new ArrayList<Menu>();
		setChildMenu(searchParameter, returnList);
		return returnList;

	}

	/**
	 * 查询子菜单
	 *
	 * @param list
	 * @param id
	 * @param level
	 */
	public void setChildMenu(Map<String, Object> searchParameter, List<Menu> list) {
		
		Map<String, Object> sortParameter = new HashMap<String, Object>();
		sortParameter.put("sort", Direction.ASC.name());
		List<Menu> listChildMenu = menuDao.findAll(searchParameter, sortParameter);

		if (listChildMenu==null || listChildMenu.size()<=0) {
			return;
		}
		for (Menu childMenu : listChildMenu) {
			list.add(childMenu);
			searchParameter.put(SearchConstant.EQUAL + "_parentId", childMenu.getId());
			if (MenuType.MENU == childMenu.getMenuType()) {
				setChildMenu(searchParameter, list);
			}
		}
	}
	
	@Override
	public List<Menu> queryMenu(Status status, Scope scope, String parentId) {
		Map<String, Object> searchParameter = new HashMap<String, Object>();
		searchParameter.put(SearchConstant.EQUAL + "_status", status.getKey());
		searchParameter.put(SearchConstant.EQUAL + "_scope", scope.getKey());
		searchParameter.put(SearchConstant.EQUAL + "_parentId", parentId);
		return menuDao.findAll(searchParameter);
	}

	@Override
	public List<Menu> findByParentId(String parentId) {
		return menuDao.findByParentId(parentId);
	}

	/**
	 * 新增菜单
	 */
	@Override
	public void addMenu(Menu menu) {
		// 新增菜单
		if (menu.getMenuType() == MenuType.MENU) {
			// 新增一级菜单
			if (StringUtils.hasText(menu.getParentId())) {
				menu.setUrl(null);
				menu.setParentId("0");
				menu.setLevel(MunuLevel.ZERO);
				menu.setIsLeaf(true);
			} else {
				// 新增二级菜单
				Menu parentMenu = findById(menu.getParentId());
				if (parentMenu!=null && parentMenu.getIsLeaf()) {
					parentMenu.setIsLeaf(false);
					menuDao.update(parentMenu);
				}
				menu.setLevel(MunuLevel.ONE);
				menu.setIsLeaf(true);
			}
			// 新增资源
		} else if (menu.getMenuType() == MenuType.RESOURCE) {
			Menu parentMenu = findById(menu.getParentId());
			if (parentMenu!=null && parentMenu.getIsLeaf()) {
				parentMenu.setIsLeaf(false);
				menuDao.update(parentMenu);
			}
			menu.setLevel(MunuLevel.TWO);
			menu.setIsLeaf(true);
		}
		save(menu);
	}

	/**
	 * 修改菜单
	 */
	@Override
	public void updateMenu(Menu menu) {
		// 修改前的menu
		Menu oldMenu = findById(menu.getId());
		if (menu.getParentId().equals(oldMenu.getParentId())) {
			menuDao.update(menu);
			return;
		}
		if (!oldMenu.getParentId().equals("0")) {
			// 修改前的parentMenu，判断当前菜单从原来的父菜单移走后，原来的父菜单还有没有孩子
			Menu parentMenu = findById(oldMenu.getParentId());
			List<Menu> list = menuDao.findByParentIdAndIdNot(parentMenu.getId(), oldMenu.getId());
			if (list.size() == 0) {
				parentMenu.setIsLeaf(true);
			}
			menuDao.update(parentMenu);
		}
		if (!menu.getParentId().equals("0")) {
			// 修改后的parentMenu，判断当前菜单移到现在的父菜单后，将当前父菜单变成isLeaf=NO
			Menu updateParentMenu = findById(menu.getParentId());
			updateParentMenu.setIsLeaf(false);
			menuDao.update(updateParentMenu);
		}
		menuDao.update(menu);
	}

	/**
	 * 删除menu
	 */
	@Override
	public void deleteMenu(Menu menu) {
		if (!"0".equals(menu.getParentId())) {
			Menu parentMenu = findById(menu.getParentId());
			List<Menu> list = menuDao.findByParentIdAndIdNot(menu.getParentId(), menu.getId());
			if (list.size() == 0) {
				parentMenu.setIsLeaf(true);
				menuDao.update(parentMenu);
			}
		}
		deleteById(menu.getId());
	}

	@Override
	public List<Map<String, Object>> findAllToMap() {
		List<Map<String, Object>> menuList = menuDao.findAllToMap();
		return menuList;
	}

	@Override
	public List<String> findAllResourceUrl() {
		return menuDao.findAllResourceUrl();
	}

	@Override
	public List<MenuVo> queryMenu(String parentId, Boolean isIsms) {
		Map<String, Object> searchParameter = new HashMap<String, Object>();
		searchParameter.put(SearchConstant.IS_TRUE + "_isIsms", true);
		searchParameter.put(SearchConstant.EQUAL + "_parentId", parentId);
		
		Map<String, Object> sortParameter = new HashMap<String, Object>();
		sortParameter.put("sort", Direction.ASC.name());
		List<Menu> listRootMenu = menuDao.findAll(searchParameter,sortParameter);
		return setChildMenu(listRootMenu,new ArrayList<String>());
	}
	
	

	@Override
	public Menu findByUrl(String url) {
		return menuDao.findOneByUrl(url);
	}

	@Override
	public List<MenuVo> queryAllMenuByRoleId(String roleId) {
		List<Menu> list = this.queryRootMenuByRoleId(roleId, Status.NORMAL);
		return setChildMenu(list,roleMenuService.findMenuIdsByRoleId(roleId));
	}

	private List<MenuVo> setChildMenu(List<Menu> listRootMenu,List<String> id){
		List<MenuVo> listMenu = new ArrayList<MenuVo>();
		for (Menu rootMenu : listRootMenu) {
			MenuVo menuVo = new MenuVo();
			BeanUtils.copyProperties(rootMenu, menuVo);
			listMenu.add(menuVo);
			
			List<Menu> listChildMenu = id.size()>0? menuDao.findByParentIdAndIdIn(rootMenu.getId(),id) :findByParentId(rootMenu.getId());
			List<MenuVo> listChileMenu = new ArrayList<MenuVo>();
			for (Menu childMennu : listChildMenu) {
				MenuVo childMenuVo = new MenuVo();
				BeanUtils.copyProperties(childMennu, childMenuVo);
				List<Menu> listChildMenu1 = id.size()>0? menuDao.findByParentIdAndIdIn(childMennu.getId(),id) :findByParentId(childMennu.getId());
				List<MenuVo> listChildMenuVo = new ArrayList<MenuVo>();
				for(Menu menu : listChildMenu1) {
					MenuVo childMenuVo1 = new MenuVo();
					BeanUtils.copyProperties(menu, childMenuVo1);
					listChildMenuVo.add(childMenuVo1);
				}
				childMenuVo.setChild(listChildMenuVo);
				listChileMenu.add(childMenuVo);
			}
			menuVo.setChild(listChileMenu);
		}
		return listMenu;
	}
}
