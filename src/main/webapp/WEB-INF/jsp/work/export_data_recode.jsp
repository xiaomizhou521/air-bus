<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="../static/css/bootstrap/bootstrap.css">
    <script type="text/javascript" src="../static/css/bootstrap/jquery-1.11.1.js" ></script>
    <script type="text/javascript" src="../static/css/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="../static/js/datepicker/WdatePicker.js" ></script>
    <script type="text/javascript" src="../static/css/dist/js/bootstrap-select.js"></script>
    <link rel="stylesheet" type="text/css" href="../static/css/dist/css/bootstrap-select.css">
    <script>
        $(function () {
            $("#pointId_Select").load("/work/toLoadPointSelect",{
                selectId:'data-recode'
            },function(){

            })
        })

        function makeReport(){
            var pointIds ='';
            $(".pointIdClass").each(function(){
                if($(this).is(":checked")){
                    if(pointIds == ''){
                        pointIds = $(this).val();
                    }else{
                        pointIds = pointIds+";"+$(this).val();
                    }

                }
            })
            var startExpDate = $("#startDate").val();
            var endExpDate = $('#endDate').val();
            $.ajax({
                type:"POST",
                url:'/work/exportDataRecodeDo',
                dataType:'json',
                async:false,
                traditional: true,
                data:{'startExpDate':startExpDate,'endExpDate':endExpDate,'pointIds':pointIds},
                success: function(data) {

                }
            });
        }

    </script>
</head>
<body>
<form action="/work/exportDataRecodeDo" method="post">
    <div style="width: 100%">
        <div style="width:1100px;margin:auto;margin-top:20px;">
            <table>
                <tr>
                <td style="width: 200px">请选择要导出数据的点:</td>
                <td>
                  <%--  <ul class="pagination" style="width: 100%;margin:0px">
                        <c:forEach items="${pointList}" var="res" varStatus="index">
                            <li class=""><input type="checkbox" class="pointIdClass" name="pointIds" id="pointIndex${index.index}"  value="${res.pointId}"><label for="pointIndex${index.index}">${res.pointName}</label></li>
                        </c:forEach>
                    </ul>--%>
                    <div id="pointId_Select"></div>
                </td>
            </tr>
            <tr>
                <td style="width: 200px">请选择日期间隔:</td>
                <td>
                    <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%;margin-left:5px;">
                        <input class="input-warning form-control" style="width:180px;height: 41px;" id="startDate" name="startExpDate" value="${startModDate}" size="20"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',startDate:'', minDate:'',maxDate:''})" type="text">
                    </div>
                    <div style="float:left;line-height: 35px;width:1%;text-align:center;">~</div>
                    <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%">
                        <input class="input-warning form-control" style="width:180px;height: 41px;" id="endDate" name="endExpDate" value="${endModDate}" size="20"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',startDate:'', minDate:'',maxDate:''})" type="text">
                    </div>
                </td>
            </tr>
            </table>
            <div><input type="button" class="btn btn-default btn-success" onclick="makeReport()" value="生成报告"></div>
        </div>
    </div>

</form>
</body>
</html>
