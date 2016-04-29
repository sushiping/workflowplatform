/*
 * Copyright: Tianjin Zoomkey Software Co,.ltd, China
 * ems
 * com.zoomkey.core.ZoomkeyNamingStrategy.java
 * Created on 2015年7月14日-下午9:36:02
 */

package com.zoomkey.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.AssertionFailure;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.internal.util.StringHelper;

/**
 * 类功能描述：
 */
public class ZoomkeyNamingStrategy extends ImprovedNamingStrategy {

	private static final long			serialVersionUID	= -6036638671388090996L;

	protected final transient Logger	log					= LogManager.getLogger(getClass());

	private static final String		TABLE_PREFIX		= "t_";

	private static final String		PRO_SUFFIX			= "_";

	/**
	 * 将实体名称转化表名称规则
	 * 规则：将类名全部大写，并加上"T_"的前缀
	 */
	@Override
	public String classToTableName(String className) {
		this.log.info("----------------className======" + className);
		String resultTable = TABLE_PREFIX + addUnderscores(StringHelper.unqualify(className));
		resultTable = resultTable.toUpperCase();
		this.log.info("----------------tableName======" + resultTable);
		return resultTable;
	}

	/**
	 * 将实体属性名称转化数据库字段名称规则
	 * 规则：按照驼峰命名规则，在大写字母前加入"_"并且全部转换大写后在最后加入"_"
	 */
	@Override
	public String propertyToColumnName(String propertyName) {
		this.log.info("---------------propertyName=========" + propertyName);
		String columnName = addUnderscores(StringHelper.unqualify(propertyName)) + PRO_SUFFIX;
		columnName = columnName.toUpperCase();
		this.log.info("---------------columnName=========" + columnName);
		return columnName;
	}

	/**
	 * 
	 * 配置表关联关系命名规则，多对多关系
	 * 规则："T_"+主表实体名称大写+"_"+关联表实体名称大写
	 */
	@Override
	public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {
		String resultTable = TABLE_PREFIX
					+ addUnderscores(StringHelper.unqualify(ownerEntityTable))
					+ PRO_SUFFIX
					+ addUnderscores(StringHelper.unqualify(associatedEntityTable));
		resultTable = resultTable.toUpperCase();
		this.log.info("-------------------translateTable==========" + resultTable);
		return resultTable;
	}

	/**
	 * 设置关联关系列名规则
	 * 规则：主表实体名称+"id_"
	 */
	@Override
	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
		final String header = propertyTableName;
		if (header == null) {
			throw new AssertionFailure("NamingStrategy not properly filled");
		}
		String joinColumn = columnName(header) + PRO_SUFFIX + translateJoinColumn(referencedColumnName);
		joinColumn = joinColumn.toUpperCase();
		this.log.info("------------joinColumn======" + joinColumn);
		return joinColumn;
	}

	private String translateJoinColumn(String referencedColumnName) {
        if(referencedColumnName.endsWith(PRO_SUFFIX))
            return referencedColumnName.toUpperCase();
		return (referencedColumnName + PRO_SUFFIX).toUpperCase();
	}
}
