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

	private final String name;

	private MenuType(int key, String name) {
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

	public static MenuType get(int key) {
		for (MenuType e : MenuType.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return MENU;
	}
}
