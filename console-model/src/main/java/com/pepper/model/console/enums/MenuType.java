package com.pepper.model.console.enums;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 
 * 菜单类型
 *
 */
public enum MenuType implements IEnum {

	CATALOG(0, "目录"), MENU(1, "菜单"), RESOURCE(2, "资源");

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
	public String getDesc(){
		return desc;
	}
	
	@JsonValue
	public Map<String, Object> jsonValue() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("key", key);
		map.put("desc", desc);
		map.put("name", getName());
		return map;
	}
}
