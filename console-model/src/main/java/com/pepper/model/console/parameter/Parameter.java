package com.pepper.model.console.parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicUpdate;

import com.pepper.core.base.BaseModel;

/**
 *
 * @author mrliu
 *
 */
@Entity()
@Table(name = "t_parameter")
@DynamicUpdate(true)
public class Parameter extends BaseModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1956990961779494025L;

	@Column(name = "code", nullable = false,length=200,unique=true)
	private String code;

	@Column(name = "value", nullable = false,length=200)
	private String value;

	@Column(name = "remarks",length=500)
	private String remarks;

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
