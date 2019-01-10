package com.pepper.model.console.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.pepper.core.IEnum;

/**
 * 省市区级别枚举
 * 
 * @author weber
 *
 */
public enum AreaLevelEnum implements IEnum {
	PROVINCE(0, "省"), CITY(1, "市"), BUTCHER(2, "区"), STREET(3, "街道");

	private final int key;

	private final String name;

	private AreaLevelEnum(int key, String name) {
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

	public static AreaLevelEnum get(int key) {
		for (AreaLevelEnum e : AreaLevelEnum.values()) {
			if (e.getKey() == key) {
				return e;
			}
		}
		return PROVINCE;
	}

}
