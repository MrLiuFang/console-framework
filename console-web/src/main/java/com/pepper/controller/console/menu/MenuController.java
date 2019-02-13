package com.pepper.controller.console.menu;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pepper.common.emuns.Scope;
import com.pepper.common.emuns.Status;
import com.pepper.core.JpqlParameter;
import com.pepper.core.ResultData;
import com.pepper.core.TreeData;
import com.pepper.core.base.BaseController;
import com.pepper.core.base.impl.BaseControllerImpl;
import com.pepper.core.constant.SearchConstant;
import com.pepper.core.exception.BusinessException;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.enums.MenuType;
import com.pepper.model.console.menu.Menu;
import com.pepper.service.authentication.ConsoleAuthorize;
import com.pepper.service.authentication.aop.Authorize;
import com.pepper.service.console.menu.MenuService;
import com.pepper.service.console.role.RoleMenuService;

/**
 * 
 * @author mrliu
 *
 */
@Controller
@RequestMapping(value = "/console/menu", method = RequestMethod.POST)
public class MenuController extends BaseControllerImpl implements BaseController {

	@Reference
	private MenuService menuService;

	@Reference
	private RoleMenuService roleMenuService;

	@Resource
	ConsoleAuthorize consoleAuthorize;

	/**
	 * @return
	 */
	@RequestMapping(value = "/index")
	@Authorize
	public String index() {
		return "/menu/menu";
	}

	/**
	 * @param menu
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/list")
	@Authorize()
	@ResponseBody
	public Object list() throws BusinessException {
		JpqlParameter jpqlParameter = new JpqlParameter();
		jpqlParameter.setSearchParameter(SearchConstant.EQUAL + "_parentId", "0");
		jpqlParameter.setSearchParameter(SearchConstant.EQUAL + "_status", Status.NORMAL);
		TreeData<List<Menu>> menuTree = new TreeData<List<Menu>>();
		List<Menu> returnList = menuService.getMenuTreeList(jpqlParameter.getSearchParameter());
		menuTree.setData(returnList);
		menuTree.setCount(returnList.size());
		return menuTree;
	}

	/**
	 * 
	 * @param scope
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value = "/getMenuList")
	@Authorize(authorizeResources = false)
	@ResponseBody
	public ResultData getMenuList(Scope scope, String parentId) {
		ResultData resultData = new ResultData();
		if (scope == null) {
			return resultData;
		}
		List<Menu> listRootMenu = menuService.queryMenu(Status.NORMAL, scope,!StringUtils.hasLength(parentId) ? "0" : parentId);
		resultData.setData("list", listRootMenu);
		return resultData;
	}

	/**
	 * 去添加菜单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	@Authorize
	public String toAdd(Integer type) {
		request.setAttribute("menuType", type);
		return "menu/menu_add";
	}

	/**
	 * 去编辑菜单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	@Authorize
	public String toEdit(String id) {
		Menu menu = menuService.findById(id);
		if (menu!=null) {
			if (menu.getMenuType() == MenuType.RESOURCE) {
				request.setAttribute("menuFirstId", menuService.findById(menu.getParentId()).getParentId());
			}
			request.setAttribute("menu", menu);
		}
		return "menu/menu_update";
	}

	/**
	 * 
	 * @param menu
	 *            添加菜单
	 * @return
	 * @throws BusinessException
	 */
	@Authorize
	@RequestMapping(value = "/add")
	@ResponseBody
	public ResultData add(Menu menu) throws BusinessException {
		ResultData resultData = new ResultData();
		if (menuService.findByCode(menu.getCode())!=null) {
			throw new BusinessException("该编码已存在！");
		}
		AdminUser adminUser = (AdminUser) this.consoleAuthorize.getCurrentUser();
		menu.setCreateDate(new Date());
		menu.setCreateUser(adminUser.getId());
		menuService.addMenu(menu);
		resultData.setLoadUrl("/admin/menu/index");
		return resultData;
	}

	/**
	 * 修改菜单（菜单可以修改父节点，但是只能同级平移，也就是二级菜单不能修改成一级菜单，也不能修改成三级菜单。同理，一级菜单也不能修改成二、三级菜单，因为我们的左侧导航栏只能是两级）
	 * 
	 * @param menu
	 * @return
	 * @throws BusinessException
	 */
	@Authorize
	@RequestMapping(value = "/update")
	@ResponseBody
	public ResultData update(Menu menu) throws BusinessException {
		ResultData resultData = new ResultData();
		Menu queryMenu = menuService.findByCode(menu.getCode());
		Menu old = menuService.findById(menu.getId());
		if (queryMenu != null && !menu.getId().equals(queryMenu.getId())) {
			throw new BusinessException("该编码已存在！");
		}
		if (!StringUtils.hasText(menu.getParentId())) {
			menu.setParentId("0");
		}
		if ("0".equals(old.getParentId())) {
			if (!"0".equals(menu.getParentId())) {
				// 如果修改前的父节点id是0，修改后不是0，也就是改变了层级
				throw new BusinessException("菜单修改父节点时，只能同级平移！");
			}
		} else {
			if (!StringUtils.hasText(menu.getParentId())) {
				throw new BusinessException("菜单修改父节点时，只能同级平移！");
			} else {
				Menu newParent = menuService.findById(menu.getParentId());
				Menu oldParent = menuService.findById(old.getParentId());
				if (!newParent.getLevel().equals(oldParent.getLevel())) {
					throw new BusinessException("菜单修改父节点时，只能同级平移！");
				}
			}
		}
		AdminUser adminUser = (AdminUser) this.consoleAuthorize.getCurrentUser();
		menu.setUpdateDate(new Date());
		menu.setUpdateUser(adminUser.getId());
		menuService.updateMenu(menu);
		resultData.setLoadUrl("/admin/menu/index");
		return resultData;
	}

	/**
	 * 
	 * @param menu
	 * @return
	 * @throws BusinessException
	 */
	@Authorize
	@RequestMapping(value = "/delete")
	@ResponseBody
	public ResultData delete(String id) throws BusinessException {
		ResultData resultData = new ResultData();
		Menu menu = menuService.findById(id);
		if (menu != null && !menu.getIsLeaf()) {
			throw new BusinessException("该菜单不是叶子节点不能删除！");
		} else {
			if (roleMenuService.findByMenuId(menu.getId()).size() > 0) {
				throw new BusinessException("该菜单已绑定角色不能删除！");
			} else {
				menuService.deleteMenu(menu);
			}
		}
		return resultData;
	}

}
