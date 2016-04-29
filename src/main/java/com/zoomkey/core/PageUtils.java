package com.zoomkey.core;

@SuppressWarnings("rawtypes")
public class PageUtils {

	private static final ThreadLocal<PagerForGrid> pagerForGrid = new ThreadLocal<PagerForGrid>();

	public static void setPagerForGrid(PagerForGrid pager) {
		pagerForGrid.set(pager);
	}

	public static PagerForGrid getPagerForGrid() {
		return pagerForGrid.get();
	}
}
