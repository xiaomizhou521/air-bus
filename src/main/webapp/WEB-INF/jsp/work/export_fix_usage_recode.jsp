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
            $("#showDownBtn").show();
            // $("#useDownBtn").hide();
            $("#showImg").hide();

            $("#week_mod").show();
            $("#mon_mod").hide();
            //统计周期下拉框 绑定事件
            $("#intervalTypeId").on('change',function(){
                if($(this).val() == 'week'){
                    $("#week_mod").show();
                    $("#mon_mod").hide();
                }else if($(this).val() == 'mon'){
                    $("#week_mod").hide();
                    $("#mon_mod").show();
                }
            })
        })

        function makeReport(){
            var reportType  = $("#reportType").val();
            var startExpDate;
            var endExpDate;
            var intervalTypeId = $("#intervalTypeId").val();
            if(intervalTypeId == 'week'){
                startExpDate = $("#d122").val();
                endExpDate = $('#d123').val();
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

            $.ajax({
                type:"POST",
                url:'/work/exportFixPointUsageRecodeDo',
                dataType:'json',
                async:false,
                traditional: true,
                data:{'startExpDate':startExpDate,'endExpDate':endExpDate,'type':intervalTypeId,'reportType':reportType},
                success: function(data) {
                    $("#makeReportId").attr("disabled",false);
                    var code = data.code;
                    var imgPath = data.imgPath;
                    var csvPath = data.csvPath;
                    if(code ==1){
                        $("#csvFilePath").val(csvPath)
                        $("#imgFilePath").val(imgPath)
                        $("#showImagePath").html("生成图形报告位置："+imgPath);
                        $("#showCSVPath").html("生成CSV报告位置："+csvPath);
                        $("#showDownBtn").hide();
                        $("#useDownBtn").show();
                        $("#showImg").show();
                        var src = "/work/readImgIo?filePath="+imgPath;
                        $("#showImg").attr("src",src);
                    }else if(code ==-1){
                        $("#showFilePath").html("");
                        alert("报告生成失败:"+data.data);
                    }
                }
            });
        }

        function downLoadFile() {
            var csvFilePath = $("#imgFilePath").val();
            if(csvFilePath ==null ||csvFilePath==''){
                alert("请先生成报告！");
                return;
            }
            window.location.href = "/work/downloadCsv?filePath=" + csvFilePath;
        }

        function downLoadFCSVile() {
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
<form action="" method="post">
    <div style="width: 100%">
        <div style="width:1100px;margin:auto;margin-top:20px;">
            <table>
                <tr style="height: 80px;">
                    <td style="width: 200px">请选择统计类型:</td>
                    <td>
                        <select class="form-control" style="width:200px" id="reportType" name="reportType" />
                        <option value="dian">电表统计</option>
                        <option value="hot">热水表统计</option>
                        <option value="cole">冷水表统计</option>
                        </select>
                    </td>
                </tr>
                <tr style="height: 80px;">
                    <td style="width: 200px">请选择统计周期:</td>
                    <td>
                        <select class="form-control" style="width:200px" id="intervalTypeId" name="intervalType" />
                        <option value="week">按周统计</option>
                        <option value="mon">按月统计</option>
                        </select>
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

            </table>
            <div style="    height: 80px;">
                <div style="float:left;"><input type="button" id="makeReportId" class="btn btn-default btn-success" onclick="makeReport()" value="生成CSV报告"></div>
                <div style="float:left;margin-left:20px;">
                    <input id="useDownBtn1" type="button" class="btn btn-default btn-success" onclick="downLoadFCSVile()" value="下载CSV报告">
                </div>
            </div>
            <div id="">
                <span id="showCSVPath"></span>
                <input type="hidden" id="csvFilePath" />
            </div>
        </div>
    </div>
</form>
</body>
</html>
