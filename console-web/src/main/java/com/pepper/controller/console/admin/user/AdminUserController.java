package com.pepper.controller.console.admin.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//import com.alibaba.fescar.spring.annotation.GlobalTransactional;
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
import com.pepper.core.validator.Validator.Insert;
import com.pepper.core.validator.Validator.Update;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.enums.UserType;
import com.pepper.model.console.role.Role;
import com.pepper.model.console.role.RoleUser;
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
@Validated
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
	public Object list() {
		Pager<AdminUser> pager = new Pager<AdminUser>();
		pager.getJpqlParameter().setSearchParameter(SearchConstant.EQUAL+"_userType", UserType.EMPLOYEE);
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
	public ResultData add(@Validated({Insert.class})AdminUser adminUser,BindingResult bindingResult,@NotBlank(message="请选择角色") String roleId) {
		adminUser.setUserType(UserType.EMPLOYEE);
		adminUser.setCreateDate(new Date());
		AdminUser user = (AdminUser) this.getCurrentUser();
		adminUser.setCreateUser(user.getId());
		adminUser.setPassword(Md5Util.encryptPassword(Md5Util.encodeByMD5(parameterService.findByCode(GlobalConstant.ADMIN_USER_INIT_PWD).getValue()),adminUser.getAccount()));
		adminUserService.saveUser(adminUser, roleId);
		return new ResultData().setLoadUrl("/console/user/index");
	}

	/**
	 * 修改页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/toEdit")
	@Authorize
	public String toEdit(@NotBlank(message="请选择要修改的角色")String id) {
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
	public ResultData update(@Validated({Update.class})AdminUser adminUser, BindingResult bindingResult, @NotBlank(message="请选择角色") String roleId) {
		adminUser.setUpdateDate(new Date());
		AdminUser user = (AdminUser) this.getCurrentUser();
		adminUser.setUpdateUser(user.getId());
		// 账号不允许修改
		AdminUser old = adminUserService.findById(adminUser.getId());
		adminUser.setAccount(old.getAccount());
		
		adminUserService.updateUser(adminUser, roleId);
		return new ResultData().setLoadUrl("/console/user/index");
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
		searchParameter.put(SearchConstant.NOTIN+"_code", new String[]{"SUPER_ADMIN_ROLE","ADMIN_ROLE"});
		List<Role> roles =  roleService.findAll(searchParameter);
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
	public ResultData updateUserInfo(AdminUser adminUser,@NotBlank(message="请选择角色")  String roleId) {
		adminUser.setUpdateDate(new Date());
		AdminUser user = (AdminUser) this.getCurrentUser();
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
	public String toView(@NotBlank(message="请选择要查看的角色") String id) {
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
	public ResultData delete(@NotBlank(message="请选择要删除的角色") String id) {
		adminUserService.deleteUser(id);
		return new ResultData();
	}

	@Authorize
	@RequestMapping(value = "/roleList")
	@ResponseBody
	public ResultData roleList(@NotBlank(message="请选择用户") String userId) {
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
	@Transactional
	public ResultData saveUserRole(@Validated(value= {Insert.class}) RoleUser roleUser,BindingResult bindingResult) {
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
	public ResultData rePwd(@NotBlank(message="请选择用户") String userId) {
		AdminUser adminUser = adminUserService.findById(userId);
		if(adminUser == null) {
			throw new BusinessException("该用户不存在");
		}
		adminUser.setPassword(Md5Util.encryptPassword(Md5Util.encodeByMD5(parameterService.findByCode(GlobalConstant.ADMIN_USER_INIT_PWD).getValue()),adminUser.getAccount()));
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
	public String toRePwd(@NotBlank(message="请选择用户") String id) {
		AdminUser adminUser = adminUserService.findById(id);
		if(adminUser == null) {
			throw new BusinessException("该用户不存在");
		}
		request.setAttribute("adminUser", adminUser);
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
	public ResultData changePwd(@NotBlank(message="请选择用户") String userId, @NotBlank(message="请输入旧密码") String oldPwd,@NotBlank(message="请输入新密码") String newPwd) {
		
		AdminUser user = adminUserService.findById(userId);
		if (user.getPassword().equals(Md5Util.encryptPassword(oldPwd,user.getAccount()))) {
			user.setPassword(Md5Util.encryptPassword(newPwd,user.getAccount()));
			adminUserService.update(user);
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
	public String userInfo(@NotBlank(message="数据错误！")String id) throws JsonProcessingException{
		RoleUser roleUser = roleUserService.findByUserId(id);
		List<Map<String, Object>> roleSelectItems = getRoleSelectItems(roleUser);
		AdminUser user = adminUserService.findById(id);
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
	public Object checkExist(@NotBlank(message="请输入用户名")String account) {
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
	public Object statusOnOff(@NotBlank(message="请选择用户")String id, @NotNull(message="请选择状态")Status status) {
		AdminUser user = adminUserService.findById(id);
		if(user == null) {
			new BusinessException("该用户不存在！");
		}
		user.setStatus(status);
		adminUserService.save(user);
		return new ResultData();
	}
}
