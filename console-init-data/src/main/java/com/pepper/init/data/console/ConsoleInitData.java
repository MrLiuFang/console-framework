package com.pepper.init.data.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import org.apache.dubbo.config.annotation.Reference;
import com.pepper.core.YamlPropertySourceFactory;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.menu.Menu;
import com.pepper.model.console.menu.MenuVo;
import com.pepper.model.console.parameter.Parameter;
import com.pepper.model.console.role.Role;
import com.pepper.model.console.role.RoleMenu;
import com.pepper.model.console.role.RoleUser;
import com.pepper.service.console.admin.user.AdminUserService;
import com.pepper.service.console.menu.MenuService;
import com.pepper.service.console.parameter.ParameterService;
import com.pepper.service.console.role.RoleMenuService;
import com.pepper.service.console.role.RoleService;
import com.pepper.service.console.role.RoleUserService;

/**
 * 后台基础数据初始化
 * @author mrliu
 *
 */
@PropertySource(value = "classpath:console-init-data.yml",name="console-init-data",  ignoreResourceNotFound = true, encoding = "UTF-8",factory=YamlPropertySourceFactory.class)
@Component
@ConfigurationProperties(prefix="console")
@Lazy
public class ConsoleInitData implements ApplicationListener<ContextRefreshedEvent> {
	
	private static Logger logger = LoggerFactory.getLogger(ConsoleInitData.class);
	
	@Autowired
   private Environment environment;
	
	private List<AdminUser> adminUser;
	
	private List<Parameter> parameter;
	
	private List<Role> role;
	
	private MenuVo menu;
	
	private Map<String, String> roleUser;
	
	private Map<String, List<String>> roleMenu;
	
	@Reference
	private AdminUserService adminUserService;
	
	@Reference
	private ParameterService parameterService;
	
	@Reference
	private RoleService roleService;
	
	@Reference
	private RoleUserService roleUserService;
	
	@Reference
	private MenuService menuService;
	
	@Reference
	private RoleMenuService roleMenuService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		String initData = environment.getProperty("console-init-data", "false").trim();
		if(arg0.getApplicationContext().getParent() == null && initData.toLowerCase().equals("true")){
			logger.info("开始初始化数据！");
			saveAdminUser();
			saveParameter();
			saveRole();
			List<MenuVo> list = new ArrayList<MenuVo>();
			list.add(menu);
			saveMenu(list, null);
			saveRoleUser();
			saveRoleMenu();
			logger.info("结束初始化数据！");
		}
	}
	
	/**
	 * 创建初始化用户
	 */
	private void saveAdminUser(){
		for(AdminUser entity : adminUser){
			AdminUser user = adminUserService.findByAccount(entity.getAccount());
			if(user == null){
				adminUserService.save(entity);
			}
		}
	}
	
	/**
	 * 创建初始化参数
	 */
	private void saveParameter(){
		for(Parameter entity : parameter){
			if(parameterService.findByCode(entity.getCode()) == null ){
				parameterService.save(entity);
			}
		}
	}
	
	/**
	 * 创建初始化角色
	 */
	private void saveRole(){
		for(Role entity : role){
			if(roleService.findByCode(entity.getCode()) == null ){
				roleService.save(entity);
			}
		}
	}
	
	/**
	 * 创建初始化菜单
	 */
	private void saveMenu(List<MenuVo> list, Menu parentMenu){
		for(MenuVo bean : list){
			Menu entity = new Menu();
			BeanUtils.copyProperties(bean, entity);
			if(parentMenu!=null && StringUtils.hasText(parentMenu.getId())){
				entity.setParentId(parentMenu.getId());
			}
			Menu oldMenu = menuService.findByCode(entity.getCode());
			if(oldMenu == null ){
				entity = menuService.save(entity);
			}else{
				entity = oldMenu;
			}
			if(bean.getChild()!=null && !bean.getChild().isEmpty()){
				saveMenu(bean.getChild(), entity);
			}
		}
	}
	
	
	
	/**
	 * 保存角色用户
	 */
	private void saveRoleUser(){
		for (String roleCode : roleUser.keySet()) {
			String roleId = roleService.findByCode(roleCode).getId();
			String userId = adminUserService.findByAccount(roleUser.get(roleCode)).getId();
			if(roleUserService.findByRoleIdAndUserId(roleId, userId) == null){
				RoleUser roleUser = new RoleUser();
				roleUser.setRoleId(roleId);
				roleUser.setUserId(userId);
				roleUserService.save(roleUser);
			}
		}
	}
	
	/**
	 * 保存角色菜单资源
	 */
	private void saveRoleMenu(){
		for (String roleCode : roleMenu.keySet()) {
			String roleId = roleService.findByCode(roleCode).getId();
			List<String> list = roleMenu.get(roleCode);
			for(String menuCode : list){
				String menuId = menuService.findByCode(menuCode).getId();
				if(roleMenuService.findByRoleAndMenu(roleId, menuId) == null ){
					RoleMenu roleMenu = new RoleMenu();
					roleMenu.setMenuId(menuId);
					roleMenu.setRoleId(roleId);
					roleMenuService.save(roleMenu);
				}
			}
		}
	}
	
	public List<Parameter> getParameter() {
		return parameter;
	}

	public void setParameter(List<Parameter> parameter) {
		this.parameter = parameter;
	}
	
	public MenuVo getMenu() {
		return menu;
	}

	public void setMenu(MenuVo menu) {
		this.menu = menu;
	}

	public List<AdminUser> getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(List<AdminUser> adminUser) {
		this.adminUser = adminUser;
	}

	public List<Role> getRole() {
		return role;
	}

	public void setRole(List<Role> role) {
		this.role = role;
	}

	public Map<String, String> getRoleUser() {
		return roleUser;
	}

	public void setRoleUser(Map<String, String> roleUser) {
		this.roleUser = roleUser;
	}

	public Map<String, List<String>> getRoleMenu() {
		return roleMenu;
	}

	public void setRoleMenu(Map<String, List<String>> roleMenu) {
		this.roleMenu = roleMenu;
	}

	
}
