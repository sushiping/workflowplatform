<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<HTML>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<link rel="icon" href="${ctx}/favicon.ico" type="image/x-icon"/>
<link rel="shortcut icon" href="${ctx}/favicon.ico" type="image/x-icon"/>
<link rel="stylesheet" type="text/css" href="${ctx}/core/login/login.css">
<HEAD>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>众齐工作流演示平台</title>
</HEAD>
<BODY>
<DIV id=logo>
    <div style="color: #FFFFFF;font-size: 20px;">管理员登录</div>
</DIV>
<DIV id=login>
    <FORM action="<c:url value='/j_hcms_security_check'/>" method="POST" name="f"/>
    <P id=info>
			<span style="font-size:13px;color:red;">
		   <strong id="errorZone">
               <c:if test="${param.error == 'loginFailure'}">
                   验证失败！
               </c:if>
               <c:if test="${param.error != 'loginFailure'}">
                   &nbsp;
               </c:if>
           </strong>
			</span>
    </P>
    <DIV class=control-group><SPAN class=icon-user></SPAN><input type="text" name="username" id="user"
                                                                 style="font-family: 微软雅黑;" placeholder="Username"/>
    </DIV>
    <DIV class=control-group><SPAN class=icon-lock></SPAN><input name="password" id="password" type="password"
                                                                 style="font-family: 微软雅黑;"
                                                                 onKeyPress="javascript:if(event.keyCode==13){login();}"
                                                                 placeholder="Password"/></DIV>
    <DIV class=remember-me><INPUT id=rm value=1 type=checkbox name=remember><LABEL for=rm>记住我</LABEL> <A
            id=forget-password href="/">忘记密码?</A></DIV>
    <DIV class=login-btn><INPUT id=login-btn value="登 录" type=submit name=submit></DIV>
    <input type="hidden" value="e3d4fb20-e9bb-4aed-8d98-a2c2e726974b" name="_csrf">
    </FORM>
</DIV>
<DIV id=login-copyright>2015-2016 天津众齐软件 <A href="http://www.zoomkey.com.cn" target=_blank>www.zoomkey.com.cn</A></DIV>
</BODY>
</HTML>