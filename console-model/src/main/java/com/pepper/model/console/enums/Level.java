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

	private final String name;

	private Level(int key, String name) {
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

	public static Level get(int key) {
		for (Level e : Level.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return ZERO;
	}

}
