package com.pepper.model.console.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 
 * 菜单类型
 *
 */
public enum MenuType implements IEnum {

	MENU(0, "菜单"), RESOURCE(1, "资源");

	private final int key;

	private final String desc;

	private MenuType(int key, String desc) {
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
