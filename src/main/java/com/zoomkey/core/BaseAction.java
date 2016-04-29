
package com.zoomkey.core;

import com.zoomkey.framework.util.LogUtil;
import com.zoomkey.usercenter.user.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 该类是Action的基类，所有Action继承这个基类
 * 方便子类实现自己功能。
 */
@Results({
        @Result(name = BaseAction.JSON, type = BaseAction.JSON, params = {
                BaseAction.ROOT, "msg"})})
@ParentPackage(value = "default")
public class BaseAction {

    /**
     * 前台拼接查询条件的map，从前台传递过来的值，自动转换为map形式
     * key值对应前台input的name，value是input的值
     */
    protected Map<String, String> conditionMap = new HashMap<String, String>();

    /**
     * 用于返回json数据map，子类自动继承该属性
     */
    protected Map<String, Object> msg = new HashMap<String, Object>();

    /**
     * 显示的错误信息，接收错误具体信息，子类自动继承该属性
     */
    protected String emsg;

    /**
     * "cancel"的字符串常量
     */
    protected static final String CANCEL = "cancel";

    /**
     * ajax 提交后的信息提示类型：success成功，error 失败
     */
    protected static final String RETURN_AJAX_STATUS = "state";

    /**
     * 返回前台提示信息内容
     */
    protected static final String RETURN_AJAX_TIPS = "info";

    /**
     * "json"的字符串常量
     */
    protected static final String JSON = "json";

    /**
     * "root"的字符串常量
     */
    protected static final String ROOT = "root";

    /**
     * "failed"的字符串常量
     */
    protected static final String FAILED = "failed";

    /**
     * "success"的字符串常量
     */
    protected static final String SUCCESS = "success";

    /**
     * "error"的字符串常量
     */
    protected static final String ERROR = "error";

    /**
     * "warn"的字符串常量
     */
    protected static final String WARN = "warn";

    /**
     * "result"的字符串常量，用于action返回结果的标识，success，failed
     */
    protected static final String RESULT = "result";

    /**
     * 拼接集合对应符号，默认英文逗号
     */
    protected static final String POINT = ",";

    /**
     * log日至输出对象，子类也可用该属性作为日志输出
     */
    protected final transient Logger log = LogManager.getLogger(getClass());

    /**
     * 简单的方法返回"cancel"字符串
     *
     * @return "cancel"
     */
    public String cancel() {
        return CANCEL;
    }

    /**
     * 简单的方法获取request对象
     *
     * @return 当前request
     */
    protected HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    /**
     * 简单方法获取response对象
     *
     * @return 当前response
     */
    protected HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    /**
     * 简单的方法获取请求的session
     *
     * @return 请求（request）的session对象
     */
    protected HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * @param value
     * @function:提示前台警告消息(橙色)
     */
    protected void addWarning(String value) {
        this.msg.put(SUCCESS, false);
        this.msg.put(RETURN_AJAX_STATUS, WARN);
        this.msg.put(RETURN_AJAX_TIPS, value);
    }

    /**
     * @param value
     * @function:提示前台错误消息(红色)
     */
    protected void addError(String value) {
        this.msg.put(SUCCESS, false);
        this.msg.put(RETURN_AJAX_STATUS, ERROR);
        this.msg.put(RETURN_AJAX_TIPS, value);
    }

    /**
     * @param value
     * @function:提示前台成功消息(绿色)
     */
    protected void addMessage(String value) {
        this.msg.put(SUCCESS, true);
        this.msg.put(RETURN_AJAX_STATUS, SUCCESS);
        this.msg.put(RETURN_AJAX_TIPS, value);
    }

    /**
     * @param key
     * @param obj
     * @function:
     */
    protected void addRecordData(String key, Object obj) {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        this.msg.put(key, obj);
    }

    /**
     * @param returnStr 需要返回给前端的
     * @function:该方法用于直接通过action返回字符串，不需要单纯再包裹一层的json
     */
    protected void returnOnlyStr(String returnStr) {
        final HttpServletResponse response = getResponse();
        response.setCharacterEncoding("utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.write(returnStr);
        } catch (final IOException e) {
            this.log.error(LogUtil.stackTraceToString(e));
        } finally {
            pw.flush();
            pw.close();
        }
    }

    /**
     * 获取当前用户对象
     * @return User对象
     */
    public User getCurrentUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            final User u = (User) auth.getPrincipal();
            return u;
        } else {
            try {
                getRequest().logout();
            } catch (final ServletException e) {
                log.error(LogUtil.stackTraceToString(e));
            }
        }
        return null;
    }

    public Map<String, String> getConditionMap() {
        return this.conditionMap;
    }

    public void setConditionMap(Map<String, String> conditionMap) {
        this.conditionMap = conditionMap;
    }

    public Map<String, Object> getMsg() {
        return this.msg;
    }

    public void setMsg(Map<String, Object> msg) {
        this.msg = msg;
    }

}
