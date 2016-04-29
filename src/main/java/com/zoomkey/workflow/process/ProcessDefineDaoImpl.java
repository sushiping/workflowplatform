
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

@Repository("processDefineDao")
public class ProcessDefineDaoImpl extends GenericDaoImpl<ProcessDefine, Long> implements ProcessDefineDao {

	public ProcessDefineDaoImpl() {
		super(ProcessDefine.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagerForGrid<ProcessDefine> queryFlowDefine() {
		StringBuilder countHql = new StringBuilder("SELECT COUNT(p.id) as num FROM ProcessDefine p ");
		StringBuilder hql = new StringBuilder("SELECT p.id AS id,p.processName AS processName,p.key AS key,p.description AS d,(CASE WHEN MAX(pd.version) IS NULL THEN 1 ELSE MAX(pd.version) END) AS version,(CASE WHEN pd.status IS NULL THEN 0 ELSE MAX(pd.status) END) AS status FROM ProcessDefine p LEFT JOIN p.details pd ");
		PagerForGrid<ProcessDefine> pagerForGrid = PageUtils.getPagerForGrid();
		JSONObject parameters = pagerForGrid.getParameters();
		Map<String, Object> mapParams = new HashMap<String, Object>();
		String whereSql = buildWhereSql(hql, parameters, mapParams);
		countHql.append(whereSql);
		hql.append(" GROUP BY p.id ");
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
		String pName = parameters.getString("pName");
		if (!StringUtil.isNullOrEmpty(pName)) {
			sql.append(" AND p.processName LIKE :name");
			mapParams.put("name", pName);
		}
		return sql.substring(sql.indexOf("WHERE"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public long queryCountByKey(String key, Long id) {
		Map<String, Object> mapParams = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder("SELECT COUNT(p.id) FROM ProcessDefine p WHERE p.key = :key ");
		mapParams.put("key", key);
		if (id != null && id.longValue() > 0) {
			hql.append(" AND p.id != :id ");
			mapParams.put("id", id);
		}
		Query query = getSession().createQuery(hql.toString());
		setParameter(mapParams, query);
		List<Long> list = query.list();
		return list.isEmpty() ? 0 : list.get(0).longValue();
	}
}
