
package com.zoomkey.workflow.process;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.zoomkey.framework.core.BaseObject;

/**
 * 
 * 类功能描述：流程定义明细信息
 */
@Entity
public class ProcessDefineDetail extends BaseObject {

	private static final long	serialVersionUID	= 2603287658356785082L;

	/**
	 * 不同版本名称
	 */
	private String					detailName;

	/**
	 * 版本号，默认从1开始
	 */
	private String					version;

	/**
	 * 版本描述
	 */
	private String					description;

	/**
	 * 发布状态，0未发布，1已发布
	 */
	private int						status				= 0;

	/**
	 * 对应工作流中模型id
	 */
	private String					modelId;

	/**
	 * 关联流程定义id
	 */
	private Long					defineId;

	/**
	 * 流程版本标识
	 */
	private String					key;

	public String getDetailName() {
		return this.detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getModelId() {
		return this.modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getDefineId() {
		return this.defineId;
	}

	public void setDefineId(Long defineId) {
		this.defineId = defineId;
	}

	@Override
	@CreationTimestamp
	@Transient
	public Date getCreateTime() {
		return super.getCreateTime();
	}

	@Override
	@UpdateTimestamp
	@Transient
	public Date getModifyTime() {
		return super.getModifyTime();
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
