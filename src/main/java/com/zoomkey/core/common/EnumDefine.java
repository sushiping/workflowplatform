/*
 * Copyright: Tianjin Zoomkey Software Co,.ltd, China
 * ems
 * com.zoomkey.core.common.ConstantEnumDefine.java
 * Created on 2015年8月2日-上午11:42:53
 */

package com.zoomkey.core.common;

import com.zoomkey.framework.util.common.Enum;

/**
 * 类功能描述：
 */
public class EnumDefine {

	public static enum IsOrNot implements Enum {
		是("是", 1), 否("否", 0);

		private final String		label;

		private final Integer	value;

		private IsOrNot(String label, Integer value) {
			this.label = label;
			this.value = value;
		}

		@Override
		public String getLabel() {
			return this.label;
		}

		@Override
		public Integer getValue() {
			return this.value;
		}

		public static String getLabel(Integer value) {
			for (IsOrNot type : IsOrNot.values()) {
				if (type.getValue().equals(value)) {
					return type.getLabel();
				}
			}
			return null;
		}

		public static Integer getValue(String Label) {
			for (IsOrNot type : IsOrNot.values()) {
				if (type.getLabel().equals(Label)) {
					return type.getValue();
				}
			}
			return null;
		}
	}

	/**
	 * 类功能描述：数据过滤方式
	 */
	public static enum DataFilterType {
		不过滤("不过滤", "0"), 按分公司过滤("按分公司过滤", "1"), 按供热站过滤("按供热站过滤", "2");

		private final String	label;

		private final String	value;

		private DataFilterType(String label, String value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return this.label;
		}

		public String getValue() {
			return this.value;
		}

		public static String getLabel(String value) {
			for (DataFilterType type : DataFilterType.values()) {
				if (type.getValue().equals(value)) {
					return type.getLabel();
				}
			}
			return null;
		}

		public static String getValue(String Label) {
			for (DataFilterType type : DataFilterType.values()) {
				if (type.getLabel().equals(Label)) {
					return type.getValue();
				}
			}
			return null;
		}
	}

	/**
	 * 类功能描述：移动方向
	 */
	public static enum MoveDirection {
		上移("上移", "0"), 下移("下移", "1");

		private final String	label;

		private final String	value;

		private MoveDirection(String label, String value) {
			this.label = label;
			this.value = value;
		}

		public String getLabel() {
			return this.label;
		}

		public String getValue() {
			return this.value;
		}

		public static String getLabel(String value) {
			for (MoveDirection type : MoveDirection.values()) {
				if (type.getValue().equals(value)) {
					return type.getLabel();
				}
			}
			return null;
		}

		public static String getValue(String Label) {
			for (MoveDirection type : MoveDirection.values()) {
				if (type.getLabel().equals(Label)) {
					return type.getValue();
				}
			}
			return null;
		}
	}
}
