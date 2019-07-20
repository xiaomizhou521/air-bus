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
<div style="    margin-top: 20px;height: 60px;">
    <form action="/work/toPointList" method="post">
        <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width: 10%;margin-left:5px;">
            <span class="input-group-addon" style="width:1%;">点名称：</span>
            <input type="text" style="width: 200px;height: 40px;"  class="global-text-right form-control"  id="pointName" name="pointName" value='${searchPointName}'/>
        </div>
        <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width: 10%;margin-left:5px;">
            <span class="input-group-addon" style="width:1%;">点别名：</span>
            <input type="text" style="width: 200px;height: 40px;"  class="global-text-right form-control"  id="remarkName" name="remarkName" value='${searchRemarkName}'/>
        </div>
        <div style="float:left;width:5%;margin-left:5px;">
            <input type="submit" class="btn btn-primary btn-sm" style="width:100px;height:35px;margin-bottom: 10px;" value="查询" />
        </div>
        <div style="float:left;width:5%;margin-left: 50px;">
            <input type="button" class="btn btn-primary btn-sm" style="width:100px;height:35px;margin-bottom: 10px;" value="增加" onclick="toSearch()" />
        </div>
    </form>
</div>
<table class="table table-bordered table-striped table-condensed"  style="width: 99%;margin-left:6px;">
    <tr>
        <th style="width:50px;text-align: center">点ID</th>
        <th style="text-align: center">点名称</th>
        <th style="text-align: center">点别名</th>
        <th style="width: 70px;text-align: center">点类型</th>
        <th style="width: 70px;text-align: center">点单位</th>
        <th style="width: 50px;text-align: center">楼号</th>
        <th style="width: 300px;text-align: center">文件相对路径</th>
        <th style="width: 120px;text-align: center">文件名前缀</th>
        <th style="width: 100px;text-align: center">操作</th></tr>
    <c:forEach items="${pointList}" var="res">
        <tr>
             <td>${res.pointId}</td>
             <td>${res.pointName}</td>
             <td>${res.remarksName}</td>
            <c:if test="${res.pointType eq 'instant'}">
                <td>瞬时点</td>
            </c:if>
            <c:if test="${res.pointType eq 'usage'}">
                <td>用量点</td>
            </c:if>
             <td>${res.pointUnit}</td>
             <td>${res.blockNo}</td>
             <td>${res.fileRelativePath}</td>
             <td>${res.filePrefixName}</td>
             <td style="text-align: center">
                 <a href="/work/toEditPoint?pointId=${res.pointId}" >修改</a>&nbsp;&nbsp;&nbsp;&nbsp;
                 <a href="" >删除</a>
             </td>
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
