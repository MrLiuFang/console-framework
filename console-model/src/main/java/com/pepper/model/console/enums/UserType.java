package com.pepper.model.console.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 
 * @author mrliu
 *
 */
public enum UserType implements IEnum {

	EMPLOYEE(0, "职员（后台管理员用户）"), ADMIN(1, "管理员");

	private final int key;

	private final String name;

	private UserType(int key, String name) {
		this.key = key;
		this.name = name;
	}

	@Override
	@JsonValue
	public Integer getKey() {
		return key;
	}

	@Override
	public String getName() {
		return name;
	}

	public static UserType get(int key) {
		for (UserType e : UserType.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return EMPLOYEE;
	}
}
