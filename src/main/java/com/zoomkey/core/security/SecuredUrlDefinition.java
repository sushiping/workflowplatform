/*
 * Copyright: Tianjin Zoomkey Software Co,.ltd, China
 * ems
 * com.zoomkey.core.security.SecuredUrlDefinition.java
 * Created on 2015年7月31日-上午10:48:55
 */

package com.zoomkey.core.security;

import java.util.Collection;
import java.util.Map;

/**
 * 类功能描述：获取可以操作某个权限的角色集合。一般权限通常由业务系统确定，该接口
 * 的主要目的就是将业务系统与可以独立运行的权限系统分离，让权限系统仅仅依赖于接口
 * 而接口的实现由业务系统来完成。
 */
public interface SecuredUrlDefinition<T> {

	/**
	 * <p>对于web系统来说，系统中的所有资源都可以使用URL来确定，而所有访问的角色可以放在
	 * 一个集合里面。这个函数的功能就是返回所有受限资源以及可以访问该受限资源的角色。</p>
	 * <p>集合使用了泛型T代表角色的描述，由于Spring Security中使用
	 * {@link org.springframework.security.access.ConfigAttribute}
	 * 作为权限的描述，为了与Spring Security隔离，采用了泛型T表示角色的描述方式。
	 * @return 所有受限资源以及可以访问的角色Map
	 */
	Map<String, Collection<T>> getSecuredUrlDefinition();
}
