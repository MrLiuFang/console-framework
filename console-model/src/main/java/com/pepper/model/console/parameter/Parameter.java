package com.pepper.model.console.parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import com.pepper.core.base.BaseModel;
import com.pepper.core.validator.Validator.Insert;
import com.pepper.core.validator.Validator.Update;

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

	@NotBlank(message="请输入编码",groups= {Insert.class})
	@Length(min=3,max=30,message="编码请输入{min}至{max}个字符",groups= {Insert.class})
	@Column(name = "code", nullable = false,length=30,unique=true)
	private String code;

	@NotBlank(message="请输入值",groups= {Insert.class,Update.class})
	@Length(max=30,message="值请输入不超过{max}个字符",groups= {Insert.class,Update.class})
	@Column(name = "value", nullable = false,length=30)
	private String value;

	@Length(max=500,message="备注不要超过{max}个字符",groups= {Insert.class,Update.class})
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
