
package com.zoomkey.workflow.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.zoomkey.framework.core.BaseObject;

/**
 * Created by colortear on 2016/4/19.
 * 用于保存流程定义相关内容
 */
@Entity
@Where(clause = "IS_VALID_ = 1 ")
public class ProcessDefine extends BaseObject {

	private static final long				serialVersionUID	= -3797103236667605427L;

	/**
	 * 流程名称
	 */
	private String								processName;

	/**
	 * 流程标识key
	 */
	private String								key;

	/**
	 * 流程描述
	 */
	private String								description;

	/**
	 * 流程关联版本明细
	 */
	private List<ProcessDefineDetail>	details				= new ArrayList<ProcessDefineDetail>();

	public String getProcessName() {
		return this.processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEFINE_ID_")
	@Fetch(FetchMode.SELECT)
	@Where(clause = "IS_VALID_ = 1")
	@OrderBy(value = "version ASC")
	public List<ProcessDefineDetail> getDetails() {
		return this.details;
	}

	public void setDetails(List<ProcessDefineDetail> details) {
		this.details = details;
	}

	@Override
	@Transient
	@CreationTimestamp
	public Date getCreateTime() {
		return super.getCreateTime();
	}

	@Override
	@Transient
	@UpdateTimestamp
	public Date getModifyTime() {
		return super.getModifyTime();
	}
}
