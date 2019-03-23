package com.pepper.controller.console.login;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.Cookie;

import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pepper.common.emuns.Status;
import com.pepper.core.base.BaseController;
import com.pepper.core.base.impl.BaseControllerImpl;
import com.pepper.core.constant.GlobalConstant;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.role.Role;
import com.pepper.service.authentication.ConsoleAuthorize;
import com.pepper.service.authentication.aop.Authorize;
import com.pepper.service.console.admin.user.AdminUserService;
import com.pepper.service.console.menu.MenuService;
import com.pepper.service.console.parameter.ParameterService;
import com.pepper.service.console.role.RoleService;
import com.pepper.service.console.role.RoleUserService;
import com.pepper.service.redis.string.serializer.SetOperationsService;
import com.pepper.service.redis.string.serializer.StringRedisTemplateService;
import com.pepper.service.redis.string.serializer.ValueOperationsService;
import com.pepper.service.verification.code.VerificationCodeService;

/**
 * 后台用户登录Contorller
 * 
 * @author mrliu
 *
 */
@Controller
@RequestMapping("/console")
public class LoginContorller extends BaseControllerImpl implements BaseController {

	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(LoginContorller.class);
	
	@SuppressWarnings("unused")
	@Autowired
	private Environment environment;
	
	@Autowired
	private ConsoleAuthorize consoleAuthorize;

	@Reference
	private ParameterService parameterService;

	@Reference
	private AdminUserService adminUserService;

	@Reference
	private ValueOperationsService valueOperationsService;

	@Reference
	private StringRedisTemplateService stringRedisTemplateService;

	@Reference
	private SetOperationsService setOperationsService;

	@Reference
	private RoleUserService roleUserService;

	@Reference
	private RoleService roleService;

	@Reference
	private MenuService menuService;
	
	@Autowired
	private VerificationCodeService verificationCodeService;


	/**
	 * 后台用户登录
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	public String login(AdminUser user,String vcode)  {

		if (!StringUtils.hasText(vcode)) {
			request.setAttribute("message", "验证码不能为空!");
			return "forward:/";
		}
		if (!StringUtils.hasText(user.getAccount())) {
			request.setAttribute("message", "用户名不能为空!");
			return "forward:/";
		}
		if (!StringUtils.hasText(user.getPassword())) {
			request.setAttribute("message", "密码不能为空!");
			return "forward:/";
		}
		
		if(!verificationCodeService.validateVerificationCode(vcode)){
			request.setAttribute("message", "验证码错误!");
			return "forward:/";
		}
		
		AdminUser userReal = adminUserService.findByAccountAndPassword(user.getAccount(), user.getPassword());

		if (userReal==null) {
			request.setAttribute("message", "登录失败!用户名/密码错误!");
			return "forward:/";
		}
		if ((userReal.getStatus()!=null && Status.DISABLE.equals(userReal.getStatus()))||userReal.getStatus()==null) {
			request.setAttribute("message", "用户已被锁定，请联系管理员!");
			return "forward:/";
		}
		Role role = roleService.findByUserId(userReal.getId());
		if (role==null) {
			request.setAttribute("message", "该用户无角色！，请联系管理员!");
			return "forward:/";
		}
		if (Status.DISABLE.equals(role.getStatus())) {
			request.setAttribute("message", "用户所在角色已被禁用，请联系管理员!");
			return "forward:/";
		}


		// 更新用户的最后登录时间
		userReal.setLastLoginTime(new Date());
		
		adminUserService.updateLoginTime(userReal.getId());
		
		// 获取用户所有资源，并让其处于登录状态。
		List<String> resourceList = roleService.queryUserAllResources(userReal.getId());
		setLoginInfo(userReal, resourceList);
		
		return "redirect:/";
	}
	
	private void setLoginInfo(AdminUser user, List<String> resourceList) {
		String token = UUID.randomUUID().toString();
		
		// 记录用户登录状态
		consoleAuthorize.setAuthorizeInfo(user.getId(), token);
		// 先删除以前的权限资源
		consoleAuthorize.deleteUserResources(user.getId());
		// 设置权限资源
		consoleAuthorize.setUserResources(resourceList, user.getId());
		//将当前用户信息保存到redis
		consoleAuthorize.setCurrentUser(user.getId(), user);

		/**
		 * 写token cookie到前端
		 */
		Cookie cookieToken = new Cookie(GlobalConstant.AUTHORIZE_TOKEN, token);
		cookieToken.setMaxAge(-1);
		cookieToken.setPath("/");
		response.addCookie(cookieToken);
		
		/**
		 * 将用户资源放到redis中，用于jsp的鉴权标签
		 */
		List<String> authMenuCode = roleService.queryUserAllMenuCode(user.getId());
		if (authMenuCode!=null && authMenuCode.size()>0) {
			String[] authMenuCodeArr = new String[authMenuCode.size()];
			setOperationsService.add(GlobalConstant.USER_RESOURCE_CODE + user.getId(),
					authMenuCode.toArray(authMenuCodeArr));
		}
		
	}


	/**
	 * 退出登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/loginOut", method = { RequestMethod.GET })
	@Authorize(authorizeResources = false, authorizeLogin = false)
	public String loginOut() {
		String token = this.getCookie(GlobalConstant.AUTHORIZE_TOKEN);
		String userId = consoleAuthorize.getUserId(token);
		consoleAuthorize.deleteAuthorizeInfo(token);
		consoleAuthorize.deleteUserResources(userId);
		consoleAuthorize.deleteResourceCode(userId);
		return "redirect:/";
	}

}
