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
    <title>点列表</title>
    <link rel="stylesheet" type="text/css" href="../static/css/bootstrap/bootstrap.css">
    <script type="text/javascript" src="../static/css/bootstrap/jquery-1.11.1.js" ></script>
    <script type="text/javascript" src="../static/css/bootstrap/bootstrap.min.js"></script>
    <script>

    </script>
</head>
<body>
<table class="table table-bordered table-striped table-condensed" >
    <tr><th style="width:50px;">点ID</th><th>点名</th><th>点别名</th><th>点单位</th><th>楼号</th><th>操作</th></tr>
    <c:forEach items="${pointList}" var="res">
        <tr>
             <td>${res.pointId}</td>
             <td>${res.pointName}</td>
             <td>${res.remarksName}</td>
             <td>${res.pointUnit}</td>
             <td>${res.blockNo}</td>
             <td><a href="/work/toEditPoint?pointId=${res.pointId}" >修改</a></td>
        </tr>
    </c:forEach>
    <tr style="line-height: 10px;">
        <td colspan="6">
            <ul class="pagination" style="width: 100%;margin:0px">
                <li class=""><a href="/work/toPointList?pageNo=0">首页</a></li>
                <li class=""><a href="/work/toPointList?pageNo=${pageNum-1}">上一页</a></li>
                <li class=""><a href="/work/toPointList?pageNo=${pageNum+1}">下一页</a></li>
                <li class=""><a href="/work/toPointList?pageNo=${lastPage}">尾页</a></li>
              <%--  <li class=""><input type="text" size="3" id="p2" value="${pageNum}" style="width:50px;height:35px;" onkeypress="javascript:return EnterPress_p2(event)" onkeydown="javascript:return EnterPress_p2()"></li>--%>
                <%--<li class=""><input type="button" value="跳转" onclick="f_skip_page_p2();" class="btn btn-primary"></li>--%>
                <li class="">第 ${pageNum} 页 每页 ${pageSize} 条 共 ${totalPage} 条</li>
            </ul>
        </td>
    </tr>
</table>
</body>
</html>
