package com.pepper.model.console.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 
 * @author mrliu
 *
 */
public enum UserType implements IEnum {

	EMPLOYEE(0, "员工/会员"), ADMIN(1, "管理员"), SUPER_ADMIN(2, "超级管理员");

	private final int key;

	private final String desc;

	private UserType(int key, String desc) {
		this.key = key;
		this.desc = desc;
	}

	@Override
	public Integer getKey() {
		return key;
	}

	@Override
	public String getName() {
		return this.toString();
	}
	
	@Override
	@JsonValue
	public String getDesc(){
		return desc;
	}
}
