package com.pepper.controller.console.admin.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.pepper.common.emuns.Scope;
import com.pepper.common.emuns.Status;
import com.pepper.core.ObjectMapperCustomer;
import com.pepper.core.Pager;
import com.pepper.core.ResultData;
import com.pepper.core.base.BaseController;
import com.pepper.core.base.impl.BaseControllerImpl;
import com.pepper.core.constant.GlobalConstant;
import com.pepper.core.constant.SearchConstant;
import com.pepper.core.exception.BusinessException;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.enums.UserType;
import com.pepper.model.console.role.Role;
import com.pepper.model.console.role.RoleUser;
import com.pepper.service.authentication.ConsoleAuthorize;
import com.pepper.service.authentication.aop.Authorize;
import com.pepper.service.console.admin.user.AdminUserService;
import com.pepper.service.console.parameter.ParameterService;
import com.pepper.service.console.role.RoleService;
import com.pepper.service.console.role.RoleUserService;
import com.pepper.service.file.FileService;
import com.pepper.util.Md5Util;


/**
 * 后台管理用户Controller
 * 
 * @author mrliu
 *
 */
@Controller
@RequestMapping(value = "/console/user", method = { RequestMethod.POST })
public class AdminUserController extends BaseControllerImpl implements BaseController {

	@Reference
	private AdminUserService adminUserService;

	@Reference
	private RoleService roleService;

	@Reference
	private RoleUserService roleUserService;

	@Reference
	private ParameterService parameterService;

	@Reference
	private FileService fileService;
	
	@Reference
	private ConsoleAuthorize consoleAuthorize;

	/**
	 * 用户管理页面
	 * 
	 * @return
	 */
	@Authorize
	@RequestMapping(value = "/index")
	public String index() {
		List<Map<String, Object>> roleSelectItems = getRoleSelectItems(null);
		request.setAttribute("roles", roleSelectItems);
		return "admin/user/admin_user";
	}

	/**
	 * 获取用户列表
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@Authorize
	@RequestMapping(value = "/list")
	@ResponseBody
	public Object list() throws BusinessException {
		Pager<AdminUser> pager = new Pager<AdminUser>();
		pager.getJpqlParameter().setSearchParameter(SearchConstant.EQUAL+"_type", UserType.EMPLOYEE);
		pager = adminUserService.list(pager);
		Role role = null;
		for (AdminUser u : pager.getResults()) {
			role = roleService.findByUserId(u.getId());
			if (role!=null) {
				u.setCreateUser(role.getName());
			}
		}
		return pager;
	}

	/**
	 * 新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toAdd")
	@Authorize
	public String toAdd() {
		List<Map<String, Object>> roleSelectItems = getRoleSelectItems(null);
		request.setAttribute("roles", roleSelectItems);
		return "/admin/user/admin_user_add";
	}

	/**
	 * 添加后台用户
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@ResponseBody
	@RequestMapping(value = "/add")
	@Authorize
	public ResultData add(AdminUser adminUser, String roleId) throws BusinessException {
		adminUser.setUserType(UserType.EMPLOYEE);
		adminUser.setCreateDate(new Date());
		AdminUser user = (AdminUser) consoleAuthorize.getCurrentUser();
		adminUser.setCreateUser(user.getId());
		adminUser.setPassword(Md5Util.encryptPassword(parameterService.findByCode(GlobalConstant.ADMIN_USER_INIT_PWD).getValue()));
		adminUserService.saveUser(adminUser, roleId);
		return new ResultData().setLoadUrl("/admin/user/index");
	}

	/**
	 * 修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	@Authorize
	public String toEdit(String id) {
		RoleUser roleUser = roleUserService.findByUserId(id);
		List<Map<String, Object>> roleSelectItems = getRoleSelectItems(roleUser);
		request.setAttribute("roles", roleSelectItems);
		request.setAttribute("adminUser", adminUserService.findById(id));
		return "/admin/user/admin_user_update";
	}

	/**
	 * 
	 * @param adminUser
	 * @return
	 * @throws BusinessException
	 */
	@ResponseBody
	@RequestMapping(value = "/update")
	@Authorize
	public ResultData update(AdminUser adminUser, String roleId) throws BusinessException {
		adminUser.setUpdateDate(new Date());
		AdminUser user = (AdminUser) consoleAuthorize.getCurrentUser();
		adminUser.setUpdateUser(user.getId());
		// 账号不允许修改
		AdminUser old = adminUserService.findById(adminUser.getId()).get();
		adminUser.setAccount(old.getAccount());
		adminUserService.updateUser(adminUser, roleId);
		return new ResultData().setLoadUrl("/admin/user/index");
	}

	/**
	 * 获取可选的role，并且根据当前用户所在的role选中role节点。用于修改查看页面的角色下拉。
	 * 
	 * @param roleUser
	 * @return
	 */
	private List<Map<String, Object>> getRoleSelectItems(RoleUser roleUser) {
		Map<String, Object> searchParameter = new HashMap<String, Object>();
		searchParameter.put(SearchConstant.EQUAL+"_scope", Scope.CONSOLE);
		searchParameter.put(SearchConstant.EQUAL+"_status", Status.NORMAL);
		if (roleUser!=null) {
			searchParameter.put(SearchConstant.NOTEQUAL+"_id", roleUser.getRoleId());
		}

		List<Role> roles =  roleService.findAll(searchParameter);
		if (roleUser!=null) {
			Role role = roleService.findById(roleUser.getRoleId()).get();
			roles.add(role);
		}
		List<Map<String, Object>> roleSelectItems = new ArrayList<Map<String, Object>>();
		Map<String, Object> roleItems = null;
		for (Role r : roles) {
			roleItems = new HashMap<String, Object>();
			if (roleUser!=null && r.getId().equals(roleUser.getRoleId())) {
				roleItems.put("selected", "selected");
			}
			roleItems.put("id", r.getId());
			roleItems.put("name", r.getName());
			roleSelectItems.add(roleItems);
		}
		return roleSelectItems;
	}

	/**
	 * 
	 * @param adminUser
	 * @return
	 * @throws BusinessException
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUserInfo")
	@Authorize(authorizeResources = false)
	public ResultData updateUserInfo(AdminUser adminUser, String roleId) throws BusinessException {
		adminUser.setUpdateDate(new Date());
		AdminUser user = (AdminUser) consoleAuthorize.getCurrentUser();
		adminUser.setUpdateUser(user.getId());
		adminUserService.updateUser(adminUser, roleId);
		return new ResultData().setLoadUrl("/");
	}

	/**
	 * 查看页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toView")
	@Authorize
	public String toView(String id) {
		RoleUser roleUser = roleUserService.findByUserId(id);
		List<Map<String, Object>> roleSelectItems = getRoleSelectItems(roleUser);
		request.setAttribute("roles", roleSelectItems);
		request.setAttribute("adminUser", adminUserService.findById(id));
		request.setAttribute("action", "view");
		return "/admin/user/admin_user_update";
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(value = "/delete")
	@Authorize
	@ResponseBody
	public ResultData delete(String id) {
		adminUserService.deleteUser(id);
		return new ResultData();
	}

	@Authorize
	@RequestMapping(value = "/roleList")
	@ResponseBody
	public ResultData roleList(String userId) {
		RoleUser roleUser = roleUserService.findByUserId(userId);
		List<Map<String, Object>> roleList = getRoleSelectItems(roleUser);
		ResultData rd = new ResultData().setData("list", roleList);
		return rd;
	}

	/**
	 * 保存用户角色
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "/saveUserRole")
	@Authorize
	@ResponseBody
	public ResultData saveUserRole(RoleUser roleUser) {
		adminUserService.saveUserRole(roleUser);
		return new ResultData();
	}

	/**
	 * 重置密码
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/rePwd")
	@Authorize
	@ResponseBody
	public ResultData rePwd(String userId) {
		AdminUser adminUser = adminUserService.findById(userId).get();
		adminUser.setPassword(Md5Util.encryptPassword(parameterService.findByCode(GlobalConstant.ADMIN_USER_INIT_PWD).getValue()));
		adminUserService.save(adminUser);
		return new ResultData();
	}

	/**
	 * 去修改密码页面
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/toRePwd")
	@Authorize(authorizeResources = false)
	public String toRePwd(String id) {
		AdminUser user = adminUserService.findById(id).get();
		request.setAttribute("adminUser", user);
		return "/admin/user/admin_user_repwd";
	}

	/**
	 * 修改密码
	 * 
	 * @param userId
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value = "/changePwd")
	@Authorize(authorizeResources = false)
	@ResponseBody
	public ResultData changePwd(String userId, String oldPwd, String newPwd) throws BusinessException {
		if (!StringUtils.hasText(oldPwd) || !StringUtils.hasText(newPwd)) {
			throw new BusinessException("请输入原密码以及新密码！");
		}
		AdminUser user = adminUserService.findById(userId).get();
		if (user.getPassword().equals(Md5Util.encryptPassword(oldPwd))) {
			user.setPassword(Md5Util.encryptPassword(newPwd));
			adminUserService.save(user);
		} else {
			throw new BusinessException("原密码错误！");
		}
		return new ResultData().setLoadUrl("/");
	}

	/**
	 * 跳转到基本资料页面
	 * 
	 * @return
	 * @throws BusinessException
	 * @throws JsonProcessingException 
	 */
	@RequestMapping(value = "/userInfo")
	@Authorize(authorizeResources = false)
	public String userInfo(String id) throws BusinessException, JsonProcessingException {
		RoleUser roleUser = roleUserService.findByUserId(id);
		List<Map<String, Object>> roleSelectItems = getRoleSelectItems(roleUser);
		AdminUser user = adminUserService.findById(id).get();
		if (StringUtils.hasText(user.getHeadPortrait())) {
			Map<String, Object> photo = new HashMap<String, Object>();
			String url = fileService.getUrl(user.getHeadPortrait());
			photo.put("id", user.getHeadPortrait());
			photo.put("url", url);
			List<Map<String, Object>> photos = new ArrayList<Map<String, Object>>();
			photos.add(photo);
			request.setAttribute("photos", new ObjectMapperCustomer().writeValueAsString(photos));
		}
		request.setAttribute("roles", roleSelectItems);
		request.setAttribute("adminUser", user);
		return "/admin/user/admin_user_info";
	}

	/**
	 * 检测商品是否已经存在
	 * 
	 * @param categoryId
	 * @return
	 */
	@RequestMapping(value = "/checkExist")
	@ResponseBody
	public Object checkExist(String account) {
		AdminUser user = null;
		if (StringUtils.hasText(account)) {
			user = adminUserService.findByAccount(account);
		}
		if (user != null) {
			return new ResultData().setData("exist", true);
		}
		return new ResultData().setData("exist", false);
	}

	/**
	 * 修改记录状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "/statusOnOff")
	@ResponseBody
	public Object statusOnOff(String id, Status status) {
		AdminUser user = adminUserService.findById(id).get();
		user.setStatus(status);
		adminUserService.save(user);
		return new ResultData();
	}
}
