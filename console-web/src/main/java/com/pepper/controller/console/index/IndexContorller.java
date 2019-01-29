package com.pepper.controller.console.index;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pepper.common.emuns.Status;
import com.pepper.core.base.BaseController;
import com.pepper.core.base.impl.BaseControllerImpl;
import com.pepper.core.constant.GlobalConstant;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.menu.Menu;
import com.pepper.model.console.parameter.Parameter;
import com.pepper.model.console.role.RoleUser;
import com.pepper.service.authentication.ConsoleAuthorize;
import com.pepper.service.console.admin.user.AdminUserService;
import com.pepper.service.console.menu.MenuService;
import com.pepper.service.console.parameter.ParameterService;
import com.pepper.service.console.role.RoleUserService;
import com.pepper.service.file.FileService;
import com.pepper.service.redis.string.serializer.ValueOperationsService;

/**
 * 
 * @author mrliu
 *
 */
@Controller
@RequestMapping("")
public class IndexContorller extends BaseControllerImpl implements BaseController {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(IndexContorller.class);

	@Autowired
	private Environment environment;

	@Reference
	protected ValueOperationsService valueOperationsService;
	
	@Reference
	private AdminUserService adminUserService;

	@Reference
	ParameterService parameterService;

	@Reference
	private ConsoleAuthorize consoleAuthorize;

	@Reference
	private RoleUserService roleUserService;

	@Reference
	private MenuService menuService;

	@Reference
	private FileService fileService;

//	@Resource
//	private CodeHelper codeHelper;

	@RequestMapping("/")
	public String index() {
		AdminUser adminUser = (AdminUser) consoleAuthorize.getCurrentUser();
		if (adminUser !=null ) {
			if (StringUtils.hasText(adminUser.getHeadPortrait())) {
				adminUser.setHeadPortrait(fileService.getUrl(adminUser.getHeadPortrait()));
			}
			request.setAttribute("adminUser", adminUser);
			setUserMenu(adminUser);
			if (environment.getProperty("jsLoad") != null) {
				String[] js = environment.getProperty("jsLoad").split("~");
				List<String> list = new ArrayList<String>();
				for (String str : js) {
					list.add(str);
				}
				request.setAttribute("jsLoad", list);
			}
			if (environment.getProperty("cssLoad") != null) {
				String[] css = environment.getProperty("cssLoad").split("~");
				List<String> list = new ArrayList<String>();
				for (String str : css) {
					list.add(str);
				}
				request.setAttribute("cssLoad", list);
			}
			setTitle("main_page_title");
			return "index";
		} else {
//			ImageVCodeHelper vCode;
//			try {
//				vCode = new ImageVCodeHelper(120, 40, 4, 0);
//				String token = UUID.randomUUID().toString();
//				codeHelper.redis(ValifyConstant.login_v_code_redis, token, 1, vCode.getvCode());
//				Util.setCookie(response, ValifyConstant.login_v_code_token, token, null, null);
//				String vcodeImage = EncodeUtil.encodeBase64(vCode.getOut().toByteArray());
//				request.setAttribute("vcodeImage", vcodeImage);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			setTitle("login_page_title");
			/**
			 * 客户定制登录页
			 */
			String customLoginPage = parameterService.findValueByCode(GlobalConstant.CUSTOM_LOGIN_PAGE);
			if (StringUtils.hasText(customLoginPage)) {
				return customLoginPage;
			}
			return "login";
		}
	}

	/**
	 * 设置页面TITLE
	 * 
	 * @param code
	 */
	private void setTitle(String code) {
		Parameter parameter = parameterService.findByCode(code);
		if (parameter!=null) {
			request.setAttribute("title", parameter.getValue());
		}
	}

	private void setUserMenu(AdminUser adminUer) {
		RoleUser roleUser = roleUserService.findByUserId(adminUer.getId());
		// 用户菜单组装
		List<Menu> listMenu = new ArrayList<Menu>();
		// 用户权限菜单根节点
		List<Menu> listRootMenu = menuService.queryRootMenuByRoleId(roleUser.getRoleId(), Status.NORMAL.getKey());
		for (Menu rootMenu : listRootMenu) {
			listMenu.add(rootMenu);
//			// 用户权限菜单子节点
//			List<Menu> listChildMenu = menuService.queryRoleChildMenu(rootMenu.getId(), roleUser.getRoleId(),
//					Status.NORMAL.getKey());
//			List<Menu> listChileMenu = new ArrayList<Menu>();
//			for (Menu childMennu : listChildMenu) {
//				listChileMenu.add(childMennu);
//			}
//			rootMenu.getChild();

		}
		request.setAttribute("listMenu", listMenu);
	}
	
	@RequestMapping("/404")
	public String error404() {
		return "404";
	}

	@RequestMapping("/401")
	public String error401() {
		return "401";
	}

	@RequestMapping("/403")
	public String error403() {
		return "403";
	}

	@RequestMapping("400")
	public String error400() {
		return "400";
	}

	@RequestMapping("/500")
	public String error500() {
		return "500";
	}

	@RequestMapping("/notLogin")
	public String notLogin() {
		return "notLogin";
	}

	@RequestMapping("/noPermission")
	public String noPermission() {
		return "noPermission";
	}

	@RequestMapping("/businessException")
	public String businessException(String msg, HttpServletRequest request) {
		request.setAttribute("msg", msg);
		return "businessException";
	}

}
