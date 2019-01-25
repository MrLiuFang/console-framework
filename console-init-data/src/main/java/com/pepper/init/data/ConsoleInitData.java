package com.pepper.init.data;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pepper.core.YamlPropertySourceFactory;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.menu.Menu;
import com.pepper.model.console.parameter.Parameter;
import com.pepper.model.console.role.Role;
import com.pepper.service.console.admin.user.AdminUserService;
import com.pepper.service.console.menu.MenuService;
import com.pepper.service.console.parameter.ParameterService;
import com.pepper.service.console.role.RoleService;

/**
 * 后台基础数据初始化
 * @author mrliu
 *
 */
@PropertySource(value = "console-init-data.yml", ignoreResourceNotFound = true, encoding = "UTF-8",factory=YamlPropertySourceFactory.class)
@Component
@Order(value=Ordered.LOWEST_PRECEDENCE)
@ConfigurationProperties(prefix="console")
@Lazy
public class ConsoleInitData implements ApplicationListener<ContextRefreshedEvent> {
	
	private static Logger logger = LoggerFactory.getLogger(ConsoleInitData.class);
	
	@Autowired
   private Environment environment;
	
	private AdminUser adminUser;
	
	private List<Parameter> parameter;
	
	private Role role;
	
	private Menu menu;
	
	@Reference
	private AdminUserService adminUserService;
	
	@Reference
	private ParameterService parameterService;
	
	@Reference
	private RoleService roleService;
	
	@Reference
	private MenuService menuService;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		logger.info("开始初始化数据！");
		String initData = environment.getProperty("console-init-data", "false").trim();
		if(arg0.getApplicationContext().getParent() == null && initData.toLowerCase().equals("true")){
			saveAdminUser();
			saveParameter();
			saveRole();
			saveMenu();
		}
		logger.info("结束初始化数据！");
	}
	
	/**
	 * 创建初始化用户
	 */
	private void saveAdminUser(){
		AdminUser user = adminUserService.findByAccount(adminUser.getAccount());
		if(user == null){
			adminUserService.save(adminUser);
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
		if(roleService.findByCode(role.getCode()) == null ){
			roleService.save(role);
		}
	}
	
	/**
	 * 创建初始化菜单
	 */
	private void saveMenu(){
		if(menuService.findByCode(menu.getCode()) == null){
			menuService.save(menu);
		}
	}
	
	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public List<Parameter> getParameter() {
		return parameter;
	}

	public void setParameter(List<Parameter> parameter) {
		this.parameter = parameter;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

}
