package com.pepper.model.console.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 树层级
 * 
 * @author drake
 */
public enum Level implements IEnum {
	ZERO(0, "一级菜单"), ONE(1, "二级菜单"), TWO(2, "三级资源");

	private final int key;

	private final String desc;

	private Level(int key, String desc) {
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
