
package com.zoomkey.workflow.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.zoomkey.core.PageUtils;
import com.zoomkey.core.PagerForGrid;
import com.zoomkey.framework.core.GenericDaoImpl;
import com.zoomkey.framework.util.StringUtil;

import net.sf.json.JSONObject;

@Repository("processDefineDetailDao")
public class ProcessDefineDetailDaoImpl extends GenericDaoImpl<ProcessDefineDetail, Long>
			implements ProcessDefineDetailDao {

	public ProcessDefineDetailDaoImpl() {
		super(ProcessDefineDetail.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagerForGrid<ProcessDefineDetail> queryFlowDetail() {
		StringBuilder countHql = new StringBuilder("SELECT COUNT(pd.id) as num FROM ProcessDefineDetail pd ");
		StringBuilder hql = new StringBuilder("SELECT pd.id AS id,pd.detailName AS detailName, pd.description AS d,pd.key AS key, pd.modelId AS modelId,(CASE WHEN pd.version IS NULL THEN 1 ELSE pd.version END) AS version,(CASE WHEN pd.status IS NULL THEN 0 ELSE pd.status END) AS status FROM ProcessDefineDetail pd ");
		PagerForGrid<ProcessDefineDetail> pagerForGrid = PageUtils.getPagerForGrid();
		JSONObject parameters = pagerForGrid.getParameters();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		String whereSql = buildWhereSql(hql, parameters, mapParams);
		countHql.append(whereSql);
		Query countQuery = getSession().createQuery(countHql.toString());
		setParameter(mapParams, countQuery);
		List<Long> num = countQuery.list();
		int count = num == null ? 0 : num.get(0).intValue();
		Query query = getSession().createQuery(hql.toString());
		query.setFirstResult((pagerForGrid.getNowPage() - 1) * pagerForGrid.getPageSize());
		query.setMaxResults(pagerForGrid.getPageSize());
		setParameter(mapParams, query);
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		List<Map<String, Object>> records = query.list();
		pagerForGrid.setExhibitDatas(records);
		pagerForGrid.setIsSuccess(true);
		pagerForGrid.setPageCount(pagerForGrid.getPageSize(), count);
		pagerForGrid.setRecordCount(count);
		return pagerForGrid;
	}

	private void setParameter(Map<String, Object> mapParams, Query query) {
		if (mapParams != null && mapParams.size() > 0) {
			for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
				String key = entry.getKey();
				if ("name".equals(key)) {
					query.setParameter(key, "%" + entry.getValue() + "%");
				} else {
					query.setParameter(key, entry.getValue());
				}
			}
		}
	}

	private String buildWhereSql(StringBuilder sql, JSONObject parameters, Map<String, Object> mapParams) {
		sql.append(" WHERE 1=1 ");
		if (parameters == null || parameters.size() == 0) {
			return sql.substring(sql.indexOf("WHERE"));
		}
		Object pName = parameters.get("dName");
		if (pName != null && !StringUtil.isNullOrEmpty(pName.toString())) {
			sql.append(" AND pd.detailName LIKE :name");
			mapParams.put("name", pName);
		}
		Long defineId = parameters.getLong("defineId");
		if (defineId != null) {
			sql.append(" AND pd.defineId = :defineId");
			mapParams.put("defineId", defineId);
		}
		return sql.substring(sql.indexOf("WHERE"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProcessDefineDetail> queryFlowDetailByModelId(String modelId) {
		StringBuilder hql = new StringBuilder("SELECT pd FROM ProcessDefineDetail pd WHERE pd.modelId = :modelId");
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("modelId", modelId);
		return query.list();
	}
}
