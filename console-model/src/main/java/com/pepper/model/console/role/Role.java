package com.pepper.model.console.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.pepper.common.emuns.Scope;
import com.pepper.common.emuns.Status;
import com.pepper.core.base.BaseModel;


@Entity()
@Table(name = "t_role")
@DynamicUpdate(true)
public class Role extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = -2812945114040067689L;

	/**
	 * 角色名称
	 */
	@Column(name = "name",nullable=false,length=200)
	private String name;

	/**
	 * 角色编码
	 */
	@Column(name = "code",nullable=false,length=200,unique=true)
	private String code;

	/**
	 * 角色作用域
	 */
	@Column(name = "scope",nullable=false)
	private Scope scope;

	/**
	 * 状态
	 */
	@Column(name = "status",nullable=false)
	private Status status;

	@Column(name = "remarks",length=500)
	private String remarks;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	
}
