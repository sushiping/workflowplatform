
package com.zoomkey.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;

/**
 * 控制层参数接受解析
 */
@Aspect
@Component
public class RequiresGridParamsAspect {

	@Around("@annotation (com.zoomkey.core.RequiresGridParams)")
	public Object before(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request = ServletActionContext.getRequest();
		String pager = request.getParameter("gridPager");
		JSONObject pagerJson = JSONObject.fromObject(pager);
		int nowPage = pagerJson.getInt("nowPage");
		int pageSize = pagerJson.getInt("pageSize");
		int startRecord = pagerJson.getInt("startRecord");
		JSONObject paramJson = pagerJson.getJSONObject("parameters");
		PagerForGrid<?> pagerForGrid = PagerForGrid.getPagerForGrid(pageSize, startRecord, nowPage, paramJson);
		PageUtils.setPagerForGrid(pagerForGrid);
		return point.proceed();
	}
}
