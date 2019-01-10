//package com.qicloud.injection.init;
//
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.ApplicationListener;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.alibaba.dubbo.config.annotation.Reference;
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.qicloud.api.console.admin.user.AdminUserService;
//import com.qicloud.api.console.menu.MenuService;
//import com.qicloud.api.console.parameter.ParameterService;
//import com.qicloud.api.console.role.RoleMenuService;
//import com.qicloud.api.console.role.RoleService;
//import com.qicloud.api.console.role.RoleUserService;
//import com.qicloud.model.console.admin.user.AdminUser;
//import com.qicloud.model.console.menu.Menu;
//import com.qicloud.model.console.parameter.Parameter;
//import com.qicloud.model.console.role.Role;
//import com.qicloud.model.console.role.RoleMenu;
//import com.qicloud.model.console.role.RoleUser;
//import com.qicloud.utils.Util;
//
///**
// * saas 多租户会存在致命bug，因此弃用，不在系统启动的时候读取数据库，并设置值
// * 初始化数据处理类
// * 
// * @author Mr.Liu
// *
// */
//
////@Component
////@ConfigurationProperties(prefix = "console-web")
//// 必须继承ApplicationListener<ApplicationReadyEvent>后才能注入EntityManager
//@Deprecated
//public class ConsoleInitData implements ApplicationListener<ApplicationReadyEvent>, Ordered {
//
//	private static Logger logger = LogManager.getLogger(ConsoleInitData.class);
//
//	@PersistenceContext
//	private EntityManager entityManager;
//
//	@Autowired
//	private ConsoleInitData initData;
//
//	@Reference
//	private RoleUserService<RoleUser> roleUserService;
//	@Reference
//	private AdminUserService<AdminUser> adminUserService;
//	@Reference
//	private ParameterService<Parameter> parameterService;
//	@Reference
//	private MenuService<Menu> menuService;
//	@Reference
//	private RoleService<Role> roleService;
//	@Reference
//	private RoleMenuService<RoleMenu> roleMenuService;
//
//	private Menu menu;
//
//	private AdminUser adminUser;
//
//	private List<Parameter> parameter;
//
//	private Role role;
//
//	private RoleUser roleUser;
//
//	public Menu getMenu() {
//		return menu;
//	}
//
//	public void setMenu(Menu menu) {
//		this.menu = menu;
//	}
//
//	public AdminUser getAdminUser() {
//		return adminUser;
//	}
//
//	public void setAdminUser(AdminUser adminUser) {
//		this.adminUser = adminUser;
//	}
//
//	public List<Parameter> getParameter() {
//		return parameter;
//	}
//
//	public void setParameter(List<Parameter> parameter) {
//		this.parameter = parameter;
//	}
//
//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}
//
//	public RoleUser getRoleUser() {
//		return roleUser;
//	}
//
//	public void setRoleUser(RoleUser roleUser) {
//		this.roleUser = roleUser;
//	}
//
//	@Override
//	public void onApplicationEvent(ApplicationReadyEvent event) {
//		try {
//			if (Util.isNotEmpty(initData)) {
//				logger.info("初始化console-web表结构及数据");
//				initData.init();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 数据初始化
//	 * 
//	 * @throws JsonProcessingException
//	 * @throws IOException
//	 */
//	@Transactional
//	private void init() throws JsonProcessingException, IOException {
//		try {
//			initMenu();
//			initAdminUser();
//			initParameter();
//			initRole();
//			initRoleUser();
//		} catch (Exception e) {
//			logger.error("初始化失败！", e);
//		}
//	}
//
//	/**
//	 * 初始化菜单及资源
//	 * 
//	 * @throws JsonParseException
//	 * @throws JsonMappingException
//	 * @throws IOException
//	 */
//	public void initMenu() throws JsonParseException, JsonMappingException, IOException {
//		saveMenu(initData.getMenu(), "0");
//	}
//
//	/**
//	 * 递归插入菜单（包括资源）
//	 * 
//	 * @param menu
//	 * @param parentId
//	 * @return
//	 */
//	private void saveMenu(Menu menu, String parentId) {
//		if (Util.isEmpty(menu)) {
//			return;
//		}
//		Menu menuExistDb = menuService.findByCode(menu.getCode());
//		String id = null;
//		if (Util.isNotEmpty(menuExistDb)) {
//			id = menuExistDb.getId();
//		} else {
//			menu.setParentId(parentId);
//			menu.setCreateDate(new Date());
//			id = menuService.save(menu).getId();
//		}
//		List<Menu> childs = menu.getChild();
//		if (Util.isNotEmpty(childs)) {
//			for (Menu child : childs) {
//				saveMenu(child, id);
//			}
//		}
//	}
//
//	/**
//	 * 初始化系统核心用户admin
//	 */
//	public void initAdminUser() {
//		AdminUser user = initData.getAdminUser();
//		if (Util.isNotEmpty(user)) {
//			AdminUser adminUserExistDb = adminUserService.findByAccount(user.getAccount());
//			if (Util.isEmpty(adminUserExistDb)) {
//				user.setCreateDate(new Date());
//				adminUserService.save(user);
//			}
//		}
//	}
//
//	/**
//	 * 初始化参数
//	 */
//	public void initParameter() {
//		List<Parameter> params = initData.getParameter();
//		if (Util.isNotEmpty(params)) {
//			for (Parameter param : params) {
//				Parameter paramterExistDb = parameterService.findByCode(param.getCode());
//				if (Util.isEmpty(paramterExistDb)) {
//					param.setCreateDate(new Date());
//					parameterService.save(param);
//				}
//			}
//		}
//	}
//
//	/**
//	 * 初始化核心用户（admin）与角色的关联
//	 */
//	public void initRoleUser() {
//		RoleUser roleUser = initData.getRoleUser();
//		if (Util.isNotEmpty(roleUser)) {
//			Role role = roleService.findByCode(roleUser.getRoleCode());
//			AdminUser adminUser = adminUserService.findByAccount(roleUser.getAdminUserAccount());
//			roleUser = roleUserService.findByRoleIdAndUserId(role.getId(), adminUser.getId());
//			if (Util.isNotEmpty(role) && Util.isNotEmpty(adminUser) && Util.isEmpty(roleUser)) {
//				roleUser = new RoleUser();
//				roleUser.setCreateDate(new Date());
//				roleUser.setRoleId(role.getId());
//				roleUser.setUserId(adminUser.getId());
//				roleUserService.save(roleUser);
//			}
//		}
//	}
//
//	/**
//	 * 初始化角色
//	 */
//	public void initRole() {
//		/**
//		 * 持久化role对象
//		 */
//		Role role = initData.getRole();
//		if (Util.isNotEmpty(role)) {
//			String roleId = null;
//			Role roleExistDb = roleService.findByCode(role.getCode());
//			if (Util.isNotEmpty(roleExistDb)) {
//				roleId = roleExistDb.getId();
//			} else {
//				roleId = roleService.save(role).getId();
//			}
//			/**
//			 * 持久化角色资源关联关系
//			 */
//			List<String> menuCodes = role.getMenuCode();
//			if (Util.isNotEmpty(menuCodes)) {
//				for (String menuCode : menuCodes) {
//					Menu menu = menuService.findByCode(menuCode);
//					if (Util.isNotEmpty(menu)) {
//						RoleMenu roleMenu = roleMenuService.findByRoleAndMenu(roleId, menu.getId());
//						if (Util.isEmpty(roleMenu)) {
//							roleMenu = new RoleMenu();
//							roleMenu.setCreateDate(new Date());
//							roleMenu.setMenuId(menu.getId());
//							roleMenu.setRoleId(roleId);
//							roleMenuService.save(roleMenu);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	@Override
//	public int getOrder() {
//		return Ordered.LOWEST_PRECEDENCE;
//	}
//
//}
