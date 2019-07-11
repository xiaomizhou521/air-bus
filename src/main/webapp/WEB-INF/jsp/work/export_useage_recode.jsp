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
    <script>
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
            var takeTime = $("#takeTimeId").val();
            $.ajax({
                type:"POST",
                url:'/work/exportUsageRecodeDo',
                dataType:'json',
                async:false,
                traditional: true,
                data:{'startExpDate':startExpDate,'endExpDate':endExpDate,'pointIds':pointIds,'takeTime':takeTime},
                success: function(data) {

                }
            });
        }

    </script>
</head>
<body>
<form action="/work/exportUsageRecodeDo" method="post">
    <div style="width: 100%">
        <div style="width:1100px;margin:auto;margin-top:20px;">
            <table>
                <tr>
                <td style="width: 200px">请选择要导出数据的点:</td>
                <td>
                    <ul class="pagination" style="width: 100%;margin:0px">
                        <c:forEach items="${pointList}" var="res" varStatus="index">
                            <li class=""><input type="checkbox" class="pointIdClass" name="pointIds" id="pointIndex${index.index}"  value="${res.pointId}"><label for="pointIndex${index.index}">${res.pointName}</label></li>
                        </c:forEach>
                    </ul>
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
            <tr>
                <td style="width: 200px">请选择采样时间:</td>
                <td>
                    <select id="takeTimeId" class="form-control" style="width:150px;">
                        <option value="00:00:00">00:00</option>
                        <option value="01:00:00">01:00</option>
                        <option value="02:00:00">02:00</option>
                        <option value="03:00:00">03:00</option>
                        <option value="04:00:00">04:00</option>
                        <option value="05:00:00">05:00</option>
                        <option value="06:00:00">06:00</option>
                        <option value="07:00:00">07:00</option>
                        <option value="08:00:00">08:00</option>
                        <option value="09:00:00">09:00</option>
                        <option value="10:00:00">10:00</option>
                        <option value="11:00:00">11:00</option>
                        <option value="12:00:00">12:00</option>
                        <option value="13:00:00">13:00</option>
                        <option value="14:00:00">14:00</option>
                        <option value="15:00:00">15:00</option>
                        <option value="16:00:00" selected>16:00</option>
                        <option value="17:00:00">17:00</option>
                        <option value="18:00:00">18:00</option>
                        <option value="19:00:00">19:00</option>
                        <option value="20:00:00">20:00</option>
                        <option value="21:00:00">21:00</option>
                        <option value="22:00:00">22:00</option>
                        <option value="23:00:00">23:00</option>
                    </select>
                </td>
            </tr>
            </table>
            <div style="margin-top:30px;"><input type="button" class="btn btn-default btn-success" onclick="makeReport()" value="生成报告"></div>
        </div>
    </div>
</form>
</body>
</html>
