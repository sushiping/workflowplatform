<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
    // 系统上下文，用于js文件中定义url
    var ctx = "${pageContext.request.contextPath}";
</script>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="${ctx}/editor-app/libs/jquery_1.11.0/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
-->
<!-- 新 Bootstrap 核心 CSS 文件
<link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
-->
<script src="${ctx}/core/dtgrid/dependents/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="${ctx}/core/dtgrid/dependents/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="${ctx}/core/dtgrid/dependents/fontAwesome/css/font-awesome.min.css"
      media="all"/>
<!-- 引用dtgrid -->
<script type="text/javascript" src="${ctx}/core/dtgrid/dlshouwen.grid.js"></script>
<!-- 引用dt国际环境 -->
<script type="text/javascript" src="${ctx}/core/dtgrid/i18n/zh-cn.js"></script>
<!-- 引用dtgrid样式-->
<link rel="stylesheet" type="text/css" href="${ctx}/core/dtgrid/dlshouwen.grid.css"/>
<!-- datePicker -->
<script type="text/javascript" src="${ctx}/core/dtgrid/dependents/datePicker/WdatePicker.js" defer="defer"></script>
<link rel="stylesheet" type="text/css" href="${ctx}/core/dtgrid/dependents/datePicker/skin/WdatePicker.css"/>
<link rel="stylesheet" type="text/css" href="${ctx}/core/dtgrid/dependents/datePicker/skin/default/datepicker.css"/>

<link href="${ctx}/core/mainFrame/mainFrame.css" rel="stylesheet">
<link rel="icon" href="${ctx}/favicon.ico" type="image/x-icon"/>
<link rel="shortcut icon" href="${ctx}/favicon.ico" type="image/x-icon"/>
<script type="text/javascript">
    function logout() {
        if (confirm("确定退出系统？")) {
            $.ajax({
                url: "${ctx}/logout",
                type: "post",
                success: function () {
                    window.location.href = "${ctx}/sof_login.jsp";
                }
            });
        }
    }
</script>