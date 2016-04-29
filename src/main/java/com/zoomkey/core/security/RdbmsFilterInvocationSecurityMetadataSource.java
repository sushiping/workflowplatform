
package com.zoomkey.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Repository;

/**
 * 类功能描述：Spring Security默认是采用在xml配置文件中定义权限和该权限可以访问的角色的，
 * 但是提供了扩展，可以让我们将权限与访问角色的定义放在其他的存储位置，我们这个类就是实现将
 * 权限和访问角色定义放置在数据库中的。
 * 
 * <p>这个类是历史遗留下来的，在迁移的时候，仅仅是将相关的类的包重新根据新版本的情况进行了调整
 * 并没有重新设计。有些代码看起来奇怪，时间比较着急。暂时这样处理。</p>
 * FIXME 该类的函数可以进行重构
 */
@Repository("rdbmsFilterInvocationDefinitionSource")
public class RdbmsFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	protected final Log											logger		= LogFactory.getLog(getClass());

	// 存储url对应的角色列表，url就是权限集合，也就是说这里定义了可以访问权限的角色集合
	private SecuredUrlDefinition<ConfigAttribute>		rdbmsInvocationDefinition;

	private Map<String, Collection<ConfigAttribute>>	requestMap	= new LinkedHashMap<String, Collection<ConfigAttribute>>();

	RdbmsFilterInvocationSecurityMetadataSource() {
	}

	// ~ Methods
	// ========================================================================================================
	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		this.requestMap = getRdbmsRequestMap();
		Collection<Collection<ConfigAttribute>> attributesCollection = this.requestMap.values();
		Collection<ConfigAttribute> allConfigAttributes = new LinkedList<ConfigAttribute>();
		for (Collection<ConfigAttribute> attributes : attributesCollection) {
			for (ConfigAttribute attribute : attributes) {
				allConfigAttributes.add(attribute);
			}
		}
		return allConfigAttributes;
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if (object == null || !supports(object.getClass())) {
			throw new IllegalArgumentException("Object must be a FilterInvocation");
		}
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		return lookupAttributes(request);
	}

	/**
	 * Performs the actual lookup of the relevant <code>ConfigAttribute</code> for the
	 * specified <code>FilterInvocation</code>. <p> By default, iterates through the stored URL map
	 * and calls the {@link UrlMatcher#pathMatchesUrl(Object path, String url)} method until a match
	 * is found. <p> Subclasses can override if required to perform any modifications to the URL.
	 * 
	 * @param url the URI to retrieve configuration attributes for
	 * @param method the HTTP method (GET, POST, DELETE...).
	 * @return the <code>ConfigAttribute</code> that applies to the specified
	 *         <code>FilterInvocation</code> or null if no match is foud
	 */
	protected Collection<ConfigAttribute> lookupAttributes(HttpServletRequest request) {
		this.requestMap = getRdbmsRequestMap();
		Collection<ConfigAttribute> attributes = lookupUrlInMap(this.requestMap, request);
		return attributes;
	}

	private Collection<ConfigAttribute> lookupUrlInMap(Map<String, Collection<ConfigAttribute>> requestMap, HttpServletRequest request) {
		Iterator<Entry<String, Collection<ConfigAttribute>>> entries = requestMap.entrySet().iterator();
		RequestMatcher requestMatcher;
		while (entries.hasNext()) {
			Entry<String, Collection<ConfigAttribute>> entry = entries.next();
			Object p = entry.getKey();
			requestMatcher = new AntPathRequestMatcher(p.toString());
			boolean matched = requestMatcher.matches(request);
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Candidate is: '"
							+ request.getRequestURI()
							+ "'; pattern is "
							+ p
							+ "; matched="
							+ matched);
			}
			if (matched) {
				return entry.getValue();
			}
		}
		// 当没有查找到任何访问该资源的角色的时候，需要给一个默认的值。
		Collection<ConfigAttribute> returnCollection = new ArrayList<ConfigAttribute>();
		returnCollection.add(new SecurityConfig("ROLE_NO_USER"));
		return returnCollection;
	}

	private Map<String, Collection<ConfigAttribute>> getRdbmsRequestMap() {
		Map<String, Collection<ConfigAttribute>> map = null;
		this.requestMap = this.rdbmsInvocationDefinition.getSecuredUrlDefinition();
		map = addWildCardToUrl(this.requestMap);
		return map;
	}

	private Map<String, Collection<ConfigAttribute>> addWildCardToUrl(Map<String, Collection<ConfigAttribute>> requestMap) {
		Map<String, Collection<ConfigAttribute>> newMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
		Iterator<Entry<String, Collection<ConfigAttribute>>> it = requestMap.entrySet().iterator();
		while (it.hasNext()) {
			String url = it.next().getKey();
			newMap.put("/**" + url + "**", requestMap.get(url));
		}
		return newMap;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	public int getMapSize() {
		return this.requestMap.size();
	}

	Map<String, Collection<ConfigAttribute>> getRequestMap() {
		return this.requestMap;
	}

	/**
	 * @return Returns the rdbmsInvocationDefinition.
	 */
	public SecuredUrlDefinition<ConfigAttribute> getRdbmsInvocationDefinition() {
		return this.rdbmsInvocationDefinition;
	}

	/**
	 * @param rdbmsInvocationDefinition The rdbmsInvocationDefinition to set.
	 */
	@Autowired
	public void setRdbmsInvocationDefinition(SecuredUrlDefinition<ConfigAttribute> rdbmsInvocationDefinition) {
		this.rdbmsInvocationDefinition = rdbmsInvocationDefinition;
	}
}
