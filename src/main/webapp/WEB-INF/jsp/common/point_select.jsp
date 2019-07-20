<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html style="height:100%;">
<html>
<head>
 <style>
 
 .dropdown-menu.open{
   max-height: 350px;
 }
 </style>
 
</head>

<script>
$(function(){
    $("#data-recode-pointIds").selectpicker({
        noneSelectedText : '--全部--'  ,
        windowPadding:'bottom'
    });
    $("#data-recode-pointIds").selectpicker('refresh');
    $("#data-recode-pointIds").on('changed.bs.select',function(e){
        var pointTest = $(".filter-option").html();
        var words = pointTest.split(',');
        $("#selectText").html("");
        for(var i=0 ;i < words.length; i++){
            $("#selectText").append("<li style='display:inline;float:left;padding:5px;'><div style='border:1px solid #0c5ce7;height: 25px;border-radius:5px;    line-height: 25px;'>"+words[i]+"</div></li>");
        }
    })



});

</script>
<body>
<div class="input-group" style="float:left;width:800px" id="proc_dep_id1_id">
    <select class="selectpicker" multiple  data-live-search="true" data-hide-disabled="true" data-size="5" style="width:500px;"  id="data-recode-pointIds">
        <c:forEach items="${pointList}" var="level1Code">
            <option value="${level1Code.pointId}">${level1Code.pointName}(${level1Code.remarksName})</option>
        </c:forEach>
    </select>
</div>
<div style="min-height:50px;height: auto">
    <ul id="selectText"></ul>
</div>
</body>
</html>