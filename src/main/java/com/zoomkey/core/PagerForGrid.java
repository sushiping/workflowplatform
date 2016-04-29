
package com.zoomkey.core;

import java.util.List;

import net.sf.json.JSONObject;

@SuppressWarnings("rawtypes")
public class PagerForGrid<T> {

	/**
	 * 每页显示条数
	 */
	private int				pageSize;

	/**
	 * 开始记录数
	 */
	private int				startRecord;

	/**
	 * 当前页数
	 */
	private int				nowPage;

	/**
	 * 记录总数
	 */
	private int				recordCount;

	/**
	 * 总页数
	 */
	private int				pageCount;

	/**
	 * 显示数据集
	 */
	private List			exhibitDatas;

	/**
	 * 是否成功
	 */
	private boolean		isSuccess;

	/**
	 * 参数列表
	 */
	private JSONObject	parameters;

	public PagerForGrid() {
	}

	public static PagerForGrid<?> getPagerForGrid(int pageSize, int startRecord, int nowPage, JSONObject parameters) {
		PagerForGrid<?> pager = new PagerForGrid<>();
		pager.setPageSize(pageSize);
		pager.setStartRecord(startRecord);
		pager.setNowPage(nowPage);
		pager.setParameters(parameters);
		return pager;
	}

	public boolean getIsSuccess() {
		return this.isSuccess;
	}

	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartRecord() {
		return this.startRecord;
	}

	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}

	public int getNowPage() {
		return this.nowPage;
	}

	public void setNowPage(int nowPage) {
		this.nowPage = nowPage;
	}

	public int getRecordCount() {
		return this.recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public int getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(int pageSize, int count) {
		this.pageCount = (count + pageSize - 1) / pageSize;
	}

	public List getExhibitDatas() {
		return this.exhibitDatas;
	}

	public void setExhibitDatas(List exhibitDatas) {
		this.exhibitDatas = exhibitDatas;
	}

	public JSONObject getParameters() {
		return this.parameters;
	}

	public void setParameters(JSONObject parameters) {
		this.parameters = parameters;
	}
}
