<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: xiaox
  Date: 2019/7/9
  Time: 22:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="../static/js/bootstrap/css/bootstrap.css">
    <script type="text/javascript" src="../static/js/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="../static/js/jquery/jquery-1.11.1.js" ></script>
    <script type="text/javascript" src="../static/js/datepicker/WdatePicker.js" ></script>
</head>
<body>
<div style="width: 100%">
    <div style="width:1100px;margin:auto;margin-top:20px;">
        <table>
            <tr>
            <td style="width: 200px">请选择要导出数据的点:</td>
            <td>
                <ul class="pagination" style="width: 100%;margin:0px">
                    <c:forEach items="${pointList}" var="res" varStatus="index">
                        <li class=""><input type="checkbox" id="pointIndex${index.index}"  value="${res.pointId}"><label for="pointIndex${index.index}">${res.pointName}</label></li>
                    </c:forEach>
                </ul>
            </td>
        </tr>
        <tr>
            <td style="width: 200px">请选择要导出的时间间隔:</td>
            <td>
                <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%;margin-left:5px;">
                    <input class="input-warning form-control" style="width:180px;height: 41px;" id="startDate" name="startModDate" value="${startModDate}" size="20"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',startDate:'', minDate:'',maxDate:''})" type="text">
                </div>
                <div style="float:left;line-height: 35px;width:1%;text-align:center;">~</div>
                <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%">
                    <input class="input-warning form-control" style="width:180px;height: 41px;" id="endDate" name="endModDate" value="${endModDate}" size="20"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',startDate:'', minDate:'',maxDate:''})" type="text">
                </div>
            </td>
        </tr>
        </table>
        <div><input type="button" class="btn btn-default btn-success" value="生成报告"></div>
    </div>
</div>
</body>
</html>
