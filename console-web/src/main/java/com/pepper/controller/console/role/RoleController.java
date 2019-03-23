package com.pepper.controller.console.role;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.dubbo.config.annotation.Reference;
import com.pepper.common.emuns.Status;
import com.pepper.core.JpqlParameter;
import com.pepper.core.Pager;
import com.pepper.core.ResultData;
import com.pepper.core.TreeData;
import com.pepper.core.base.BaseController;
import com.pepper.core.base.impl.BaseControllerImpl;
import com.pepper.core.constant.SearchConstant;
import com.pepper.core.exception.BusinessException;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.menu.Menu;
import com.pepper.model.console.role.Role;
import com.pepper.service.authentication.ConsoleAuthorize;
import com.pepper.service.authentication.aop.Authorize;
import com.pepper.service.console.menu.MenuService;
import com.pepper.service.console.role.RoleMenuService;
import com.pepper.service.console.role.RoleService;
import com.pepper.service.console.role.RoleUserService;

/**
 * 
 * @author mrliu
 *
 */
@Controller
@RequestMapping(value = "/console/role")
public class RoleController extends BaseControllerImpl implements BaseController{

	@Reference
	private RoleService roleService;

	@Reference
	private RoleMenuService roleMenuService;

	@Resource
	ConsoleAuthorize consoleAuthorize;

	@Reference
	private MenuService menuService;

	@Reference
	private RoleUserService roleUserService;

	/**
	 * 
	 * @return
	 */
	@Authorize
	@RequestMapping(value = "/index")
	public String index() {
		return "role/role";
	}

	/**
	 * 
	 * @param role
	 * @return
	 */
	@Authorize
	@RequestMapping(value = "list")
	@ResponseBody
	public Pager<Role> list() {
		Pager<Role> pager = new Pager<Role>();
		pager.getJpqlParameter().setSearchParameter(SearchConstant.NOTIN+"_code", new String[]{"SUPER_ADMIN_ROLE","ADMIN_ROLE"});
		pager = roleService.findNavigator(pager);
		return pager;
	}

	/**
	 * 
	 * @param role
	 * @return
	 */
	@Authorize
	@RequestMapping(value = "toAdd")
	public String toAdd() {
		return "role/role_add";
	}

	/**
	 * 
	 * @param role
	 * @return
	 * @throws BusinessException
	 */
	@Authorize
	@RequestMapping(value = "add")
	@ResponseBody
	public ResultData add(Role role, String resourceIds) {
		if (roleService.findByName(role.getName())!=null) {
			throw new BusinessException("角色名已存在，请重新输入！");
		}
		if (roleService.findByCode(role.getCode())!=null) {
			throw new BusinessException("角色编码已存在，请重新输入！");
		}
		AdminUser user = (AdminUser) this.getCurrentUser();
		role.setCreateDate(new Date());
		role.setCreateUser(user.getId());
		roleService.saveRole(role, resourceIds);
		return new ResultData().setLoadUrl("/console/role/index");
	}

	/**
	 * 
	 * @param role
	 * @return
	 */
	@Authorize
	@RequestMapping(value = "toEdit")
	public String toEdit(String id) {
		Role role = roleService.findById(id);
		List<String> menuIds = roleMenuService.findMenuIdsByRoleId(id);
		request.setAttribute("role", role);
		StringBuffer res = new StringBuffer("");
		for (String s : menuIds) {
			res.append(s + ";");
		}
		if (StringUtils.hasText(res.toString())) {
			request.setAttribute("menuIds",res.toString());
		}
		return "role/role_update";
	}

	/**
	 * 
	 * @param role
	 * @return
	 * @throws BusinessException
	 */
	@Authorize
	@RequestMapping(value = "update")
	@ResponseBody
	public ResultData update(Role role, String resourceIds) {
		Role old = roleService.findById(role.getId());
		if (!role.getName().equals(old.getName()) && roleService.findByName(role.getName())!=null) {
			throw new BusinessException("角色名已存在，请重新输入！");
		}
		role.setUpdateDate(new Date());
		AdminUser user = (AdminUser) this.getCurrentUser();
		role.setUpdateUser(user.getId());
		roleService.updateRole(role, resourceIds);
		return new ResultData().setLoadUrl("/console/role/index");
	}

	/**
	 * 删除角色，删角色同时删除角色菜单关系
	 * 
	 * @param role
	 * @return
	 */
	@Authorize
	@RequestMapping(value = "delete")
	@ResponseBody
	public ResultData delete(String id) {
		if (roleUserService.findByRoleId(id)!=null) {
			return new ResultData().setMessage("已关联用户，不允许删除！").setStatus(500);
		}
		roleService.deleteRole(id);
		return new ResultData();
	}

	/**
	 * 查看页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toView")
	@Authorize
	public String toView(String id) {
		Role role = roleService.findById(id);
		request.setAttribute("role", role);
		return "role/role_view";
	}

	/**
	 * 获取资源树状列表
	 * 
	 * @param id
	 * @return
	 */
	@Authorize
	@RequestMapping(value = "roleMenu")
	@ResponseBody
	public Object roleMenu(String roleId) {
		JpqlParameter jpqlParameter = new JpqlParameter();
		jpqlParameter.setSearchParameter(SearchConstant.EQUAL + "_parentId", "0");
		jpqlParameter.setSearchParameter(SearchConstant.EQUAL + "_status", Status.NORMAL.getKey());
		TreeData<List<Menu>> menuTreeVo = new TreeData<List<Menu>>();
		List<Menu> returnList = menuService.getMenuTreeList(jpqlParameter.getSearchParameter());
		menuTreeVo.setData(returnList);
		menuTreeVo.setCount(returnList.size());
		if (StringUtils.hasText(roleId)) {
			List<String> menuIds = roleMenuService.findMenuIdsByRoleId(roleId);
			StringBuffer res = new StringBuffer("");
			for (String s : menuIds) {
				res.append(s + ";");
			}
			if (StringUtils.hasText(res.toString())) {
				menuTreeVo.setExtData(res.toString());
			}
			
		}
		return menuTreeVo;
	}

	/**
	 * 
	 * @param roleId
	 * @param ids
	 * @return
	 * @throws BusinessException
	 */
	@Authorize
	@RequestMapping(value = "saveRoleMenu")
	@ResponseBody
	public ResultData saveRoleMenu(String roleId, String resourceIds) {
		ResultData resultData = new ResultData();
		roleService.updateRoleMenu(roleId, resourceIds);
		return resultData;
	}

}
