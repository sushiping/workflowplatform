
package com.zoomkey.core;

import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;

/**
 * 类功能描述：为在HQL中使用中文排序，注册添加函数类
 *
 */
public class MySQL5LocalDialect extends MySQL5InnoDBDialect {

	public MySQL5LocalDialect() {
		super();
		registerFunction("convert", new SQLFunctionTemplate(StandardBasicTypes.STRING, "convert(?1 using ?2)"));
		registerFunction("group_concat", new StandardSQLFunction("group_concat", new StringType()));
	}
}
