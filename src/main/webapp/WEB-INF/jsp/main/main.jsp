<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:shiro="http://www.pollix.at/thymeleaf/shiro">
<!--head部分-->
<head th:include="layout :: htmlhead" th:with="title='海韵后台管理'">
    <jsp:include page="layout.jsp"/>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <!--头-->
    <%--<div th:replace="fragments/head :: header">111</div>--%>
    <jsp:include page="head.jsp"/>
    <!--主体区域-->
    <div class="layui-body">
        <div class="layui-container">
            <div class="layui-row">
                <div class="layui-col-xs9">
                   <jsp:include page="../work/excelExport.jsp" />

                </div>
            </div>
        </div>
    </div>
    <!--底部-->
   <%-- <div th:replace="footer :: footer"></div>--%>
</div>
</body>
</html>