<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xiaox
  Date: 2019/7/8
  Time: 20:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>csv日志列表</title>
    <link rel="stylesheet" type="text/css" href="../static/css/bootstrap/bootstrap.css">
    <script type="text/javascript" src="../static/css/bootstrap/jquery-1.11.1.js" ></script>
    <script type="text/javascript" src="../static/css/bootstrap/bootstrap.min.js"></script>
    <script>
        $(function () {
            $("#stateId").val(${state});
        })
    </script>
</head>
<body>
<div style="    margin-top: 20px;height: 60px;">
    <form action="/work/toReadCSVLogsList" method="post">
        <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width: 10%;margin-left:5px;">
            <span class="input-group-addon" style="width:1%;">CSV路径：</span>
            <input type="text" style="width: 200px;height: 40px;"  class="global-text-right form-control"  id="pointName" name="filePath" value='${filePath}'/>
        </div>
        <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width: 10%;margin-left:5px;">
            <span class="input-group-addon" style="width:1%;">导入状态：</span>
            <select style="width: 200px;height: 40px;" id="stateId" name="state" class="form-control">
                <option value="">请选择</option>
                <option value="1">正常</option>
                <option value="-1">异常</option>
            </select>
        </div>
        <div style="float:left;width:5%;margin-left:5px;">
            <input type="submit" class="btn btn-primary btn-sm" style="width:100px;height:35px;margin-bottom: 10px;" value="查询" />
        </div>
    </form>
</div>
<table class="table table-bordered table-striped table-condensed"  style="width: 99%;margin-left:6px;">
    <tr>
        <th style="width:50px;text-align: center;vertical-align: middle">ID</th>
        <th style="text-align: center;vertical-align: middle">CSV路径</th>
        <th style="text-align: center;width:80px;vertical-align: middle">导入状态</th>
        <th style="width: 150px;text-align: center;vertical-align: middle">导入时间</th>
        <th style="text-align: center;vertical-align: middle">异常描述</th>
    <c:forEach items="${csvLogs}" var="res">
        <tr>
            <td style="text-align: center">${res.logId}</td>
            <td>${res.filePath}</td>
            <td style="text-align: center">
                <c:if test="${res.state eq 1}">
                    正常
                </c:if>
                <c:if test="${res.state eq -1}">
                    <span style="color:red;">异常</span>
                </c:if>
            </td>
            <td style="text-align: center">${res.initDate}</td>
            <td>${res.detail}</td>
        </tr>
    </c:forEach>
    <tr style="line-height: 10px;">
        <td colspan="9">
            <ul class="pagination" style="width: 100%;margin:0px">
                <li class=""><a href="/work/toReadCSVLogsList?pageNo=0">首页</a></li>
                <li class=""><a href="/work/toReadCSVLogsList?pageNo=${lastPage}">上一页</a></li>
                <li class=""><a href="/work/toReadCSVLogsList?pageNo=${nextPage}">下一页</a></li>
                <%--   <li class=""><a href="/work/toPointList?pageNo=${lastPage}">尾页</a></li>--%>
                <%--  <li class=""><input type="text" size="3" id="p2" value="${pageNum}" style="width:50px;height:35px;" onkeypress="javascript:return EnterPress_p2(event)" onkeydown="javascript:return EnterPress_p2()"></li>--%>
                <%--<li class=""><input type="button" value="跳转" onclick="f_skip_page_p2();" class="btn btn-primary"></li>--%>
                <li class=""><div style="margin-top: 6px">第 ${pageNum+1} 页 每页 ${pageSize} 条</div></li>
            </ul>
        </td>
    </tr>
</table>
</body>
</html>
