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
    <link rel="stylesheet" type="text/css" href="../static/css/bootstrap/bootstrap.css">
    <script type="text/javascript" src="../static/css/bootstrap/jquery-1.11.1.js" ></script>
    <script type="text/javascript" src="../static/css/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="../static/js/datepicker/WdatePicker.js" ></script>
    <script type="text/javascript" src="../static/css/dist/js/bootstrap-select.js"></script>
    <link rel="stylesheet" type="text/css" href="../static/css/dist/css/bootstrap-select.css">
    <script>
        $(function(){
            $("#pointId_Select").load("/work/toLoadPointSelect",{
                selectId:'data-recode',
                pointType:'usage'
            },function(){
            })

            $("#showDownBtn").show();
            $("#useDownBtn").hide();
            $("#showImg").hide();

             $("#date_mod").show();
             $("#week_mod").hide();
             $("#mon_mod").hide();
             //统计周期下拉框 绑定事件
            $("#intervalTypeId").on('change',function(){
                if($(this).val() == 'week'){
                    $("#date_mod").hide();
                    $("#week_mod").show();
                    $("#mon_mod").hide();
                }else if($(this).val() == 'date'){
                    $("#date_mod").show();
                    $("#week_mod").hide();
                    $("#mon_mod").hide();
                }else if($(this).val() == 'mon'){
                    $("#date_mod").hide();
                    $("#week_mod").hide();
                    $("#mon_mod").show();
                }
            })
        })

        function makeReport(reportType){
            var pointIds = $("#data-recode-pointIds").val();
            if(pointIds==null||pointIds==''){
                alert("请选择点！");
                return;
            }
            var startExpDate;
            var endExpDate;
            var intervalTypeId = $("#intervalTypeId").val();
            if(intervalTypeId == 'week'){
                startExpDate = $("#d122").val();
                endExpDate = $('#d123').val();
            }else if(intervalTypeId == 'date'){
                startExpDate = $("#startDate").val();
                endExpDate = $('#endDate').val();
            }else if(intervalTypeId == 'mon'){
                startExpDate = $("#startMon").val();
                endExpDate = $('#endMon').val();
            }
            if(startExpDate==null||startExpDate==''){
                alert("请选择开始时间！");
                return;
            }
            if(endExpDate==null||endExpDate==''){
                alert("请选择结束时间！");
                return;
            }
            var takeTime = $("#takeTimeId").val();
            var words = (pointIds+'').split(',');
            var pointIdsValue = "";
            for(var i=0 ;i < words.length; i++){
                if(pointIdsValue == ""){
                    pointIdsValue = words[i];
                }else{
                    pointIdsValue = pointIdsValue +";"+ words[i];
                }
            }
            $.ajax({
                type:"POST",
                url:'/work/exportUsageChartDo',
                dataType:'json',
                async:false,
                traditional: true,
                data:{'startExpDate':startExpDate,'endExpDate':endExpDate,'pointIds':pointIdsValue,'type':intervalTypeId,'reportType':reportType},
                success: function(data) {
                    $("#makeReportId").attr("disabled",false);
                    var code = data.code;
                    var result = data.data;
                    if(code ==1){
                        $("#csvFilePath").val(result)
                        $("#showFilePath").html("生成报告位置："+result);
                        $("#showDownBtn").hide();
                        $("#useDownBtn").show();
                        $("#showImg").show();
                        var src = "/work/readImgIo?filePath="+result;
                        $("#showImg").attr("src",src);
                    }else if(code ==-1){
                        $("#showFilePath").html("");
                        alert("报告生成失败:"+result);
                    }
                }
            });
        }

        function downLoadFile() {
            var csvFilePath = $("#csvFilePath").val();
            if(csvFilePath ==null ||csvFilePath==''){
                alert("请先生成报告！");
                return;
            }
            window.location.href = "/work/downloadCsv?filePath=" + csvFilePath;
        }

        function funccc122(){
            $('#d122').val($dp.cal.getP('y')+$dp.cal.getP('W','WW'));
        }

        function funccc123(){
            $('#d123').val($dp.cal.getP('y')+$dp.cal.getP('W','WW'));
        }
    </script>
</head>
<body>
<form action="/work/exportUsageRecodeDo" method="post">
    <div style="width: 100%">
        <div style="width:1100px;margin:auto;margin-top:20px;">
            <table>
                <tr style="height: 80px;">
                    <td style="width: 200px">请选择点:</td>
                    <td>
                        <div id="pointId_Select"></div>
                    </td>
                </tr>
            <tr style="height: 80px;">
                <td style="width: 200px">请选择统计周期:</td>
                <td>
                   <select class="form-control" style="width:200px" id="intervalTypeId" name="intervalType" />
                       <option value="date">按日统计</option>
                       <option value="week">按周统计</option>
                       <option value="mon">按月统计</option>
                    </select>
                </td>
            </tr>
            <tr id="date_mod" style="height: 100px;">
                <td style="width: 200px">请选择日期间隔:</td>
                <td>
                    <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%;">
                        <input class="input-warning form-control" autocomplete="off" style="width:180px;height: 41px;" id="startDate" name="startExpDate" value="${startModDate}" size="20"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',startDate:'', minDate:'',maxDate:''})" type="text">
                    </div>
                    <div style="float:left;line-height: 35px;width:1%;text-align:center;">~</div>
                    <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%">
                        <input class="input-warning form-control" autocomplete="off" style="width:180px;height: 41px;" id="endDate" name="endExpDate" value="${endModDate}" size="20"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd',startDate:'', minDate:'',maxDate:''})" type="text">
                    </div>
                </td>
            </tr>
            <tr id="week_mod" style="height: 100px;">
                <td style="width: 200px">请选择周间隔:</td>
                <td >
                    <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%;">
                        <input  id="d122"  name="startWeek" autocomplete="off" type="text" style="width:180px;height: 41px;"  class="input-warning form-control" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'d122\')}',isShowWeek:true,onpicked:funccc122,errDealMode:3})"/>
                    </div>
                    <div style="float:left;line-height: 35px;width:1%;text-align:center;">~</div>
                    <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%">
                        <input  id="d123" name="endWeek" autocomplete="off" type="text" style="width:180px;height: 41px;"  class="input-warning form-control" onclick="WdatePicker({maxDate:'#F{$dp.$D(\'d123\')}',isShowWeek:true,onpicked:funccc123,errDealMode:3})"/>
                    </div>
                </td>
            </tr>
            <tr id="mon_mod" style="height: 100px;">
                <td style="width: 200px">请选择月间隔:</td>
                <td>
                    <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%;">
                        <input class="input-warning form-control" autocomplete="off" style="width:180px;height: 41px;" id="startMon" name="startMon" value="${startModDate}" size="20"  onclick="WdatePicker({dateFmt:'yyyy-MM',startDate:'', minDate:'',maxDate:''})" type="text">
                    </div>
                    <div style="float:left;line-height: 35px;width:1%;text-align:center;">~</div>
                    <div class="input-group" style="float:left;margin-bottom: 10px;line-height: 35px;width:10%">
                        <input class="input-warning form-control" autocomplete="off" style="width:180px;height: 41px;" id="endMon" name="endMon" value="${endModDate}" size="20"  onclick="WdatePicker({dateFmt:'yyyy-MM',startDate:'', minDate:'',maxDate:''})" type="text">
                    </div>
                </td>
            </tr>

            <%--<tr>
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
            </tr>--%>
            </table>
            <div style="    height: 80px;">
                <div style="float:left;"><input type="button" id="makeReportId" class="btn btn-default btn-success" onclick="makeReport('bar')" value="生成柱形图报告"></div>
                <div style="float:left;margin-left:20px;"><input type="button" id="makeReportId2" class="btn btn-default btn-success" onclick="makeReport('series')" value="生成折线图报告"></div>
                <div style="float:left;margin-left:20px;">
                    <%--<input id="showDownBtn" type="button" class="btn btn-default" value="下载文件">--%>
                    <input id="useDownBtn" type="button" class="btn btn-default btn-success" onclick="downLoadFile()" value="下载文件">
                </div>
            </div>
            <div id="">
                <span id="showFilePath"></span>
                <input type="hidden" id="csvFilePath" />
            </div>
            <div style="width: 100%">
                <div style="width: 800px;margin:auto">
                    <img id="showImg" src="" width="800px" height="400px" />
                </div>
            </div>
        </div>
    </div>
</form>
</body>
</html>
