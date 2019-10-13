package com.pepper.dao.console.admin.user.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.pepper.common.emuns.Status;
import com.pepper.core.Pager;
import com.pepper.core.base.BaseDao;
import com.pepper.dao.console.admin.user.AdminUserDaoEx;
import com.pepper.model.console.admin.user.AdminUser;
import com.pepper.model.console.enums.UserType;

/**
 * 
 * @author mrliu
 *
 */
public class AdminUserDaoImpl  implements AdminUserDaoEx<AdminUser>{
	
	@Autowired
	private BaseDao<AdminUser> baseDao;

	@Override
	public List<AdminUser> findUserByDepartmentId(String departmentId) {
		Map<String,Object> searchParameter = new HashMap<String, Object>();
//		String jpql = "SELECT  au from AdminUser au left join RoleUser ru on au.id = ru.userId left join Role r on ru.roleId = r.id "
//				+ " where au.userType = :userType and au.departmentId =:departmentId and r.code = 'EMPLOYEE_ROLE' ";
		String jpql = "SELECT  au from AdminUser au  join RoleUser ru on au.id = ru.userId  join Role r on ru.roleId = r.id "
				+ " where au.userType = :userType  and r.code = 'EMPLOYEE_ROLE' ";
		searchParameter.put("userType", UserType.EMPLOYEE);
//		searchParameter.put("departmentId", departmentId);
		List<AdminUser> list = baseDao.find(jpql, searchParameter);
		return list;
	}
	
	public Pager<AdminUser> findAdminUser(Pager<AdminUser> pager,String account,String mobile,String email,String name,String departmentId,String departmentGroupId,String roleId,Boolean isWork,Status status,String keyWord){
		Map<String,Object> searchParameter = new HashMap<String, Object>();
		StringBuffer jpql = new StringBuffer();
		jpql.append("SELECT distinct au from AdminUser au  join RoleUser ru on au.id = ru.userId  join Role r on ru.roleId = r.id "
				+ " where au.userType = :userType   ");
		searchParameter.put("userType", UserType.EMPLOYEE);
		
		if(StringUtils.hasText(roleId)) {
			jpql.append( " and r.id = :roleId " );
			searchParameter.put("roleId",roleId);
//			if(roleId.equals("OPERATOR_ROLE")) {
//				jpql.append( " and au.isManager = :isManager " );
//				searchParameter.put("isManager",true);
//			}
		}
		
		if(Objects.nonNull(status)) {
			jpql.append( " and au.status = :status " );
			searchParameter.put("status",status);
		}
		
		if(StringUtils.hasText(account)) {
			jpql.append( " and au.account like :account " );
			searchParameter.put("account","%"+account+"%");
		}
		
		if(StringUtils.hasText(mobile)) {
			jpql.append( " and au.mobile like :mobile " );
			searchParameter.put("mobile","%"+mobile+"%");
		}
		
		if(StringUtils.hasText(email)) {
			jpql.append( " and au.email like :email " );
			searchParameter.put("email","%"+email+"%");
		}
		
		if(StringUtils.hasText(name)) {
			jpql.append( "and au.name like :name " );
			searchParameter.put("name","%"+name+"%");
		}
		
		if(StringUtils.hasText(departmentId)) {
			jpql.append( " and au.departmentId = :departmentId " );
			searchParameter.put("departmentId",departmentId);
		}
		
		if(StringUtils.hasText(departmentGroupId)) {
			jpql.append( " and au.departmentGroupId = :departmentGroupId " );
			searchParameter.put("departmentGroupId",departmentGroupId);
		}
		if(isWork!=null) {
			jpql.append( " and au.isWork = :isWork " );
			searchParameter.put("isWork",isWork);
		}
		if(StringUtils.hasText(keyWord)) {
			jpql.append( " and ( au.account like :keyWord or au.mobile like :keyWord or au.email like :keyWord or au.name like :keyWord  )" );
			searchParameter.put("keyWord","%"+keyWord+"%");
		}
		jpql.append( " order by  au.departmentId , au.departmentGroupId ,au.account" );
		return baseDao.findNavigator(pager, jpql.toString(), searchParameter);
	}

	@Override
	public List<AdminUser> findByDepartmentId(String departmentId, Boolean isManager) {
		Map<String,Object> searchParameter = new HashMap<String, Object>();
		StringBuffer jpql = new StringBuffer();
		jpql.append(" from AdminUser t1 where t1.departmentId =:departmentId and t1.isManager =:isManager and ( t1.departmentGroupId is null or t1.departmentGroupId = '' )");
		searchParameter.put("departmentId",departmentId);
		searchParameter.put("isManager",isManager);
		return baseDao.find(jpql.toString(), searchParameter);
	}
	
	

}
