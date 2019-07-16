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
   /* $("#"+"${selectId}"+"-pointIds").parent().find('input[type=text]').keyup(function(){
		var minItemLength=1;
		if($(this).val().length>=minItemLength){
			$.post('/work/findPoints',{
                pointName:$(this).val()
			},function(data){
			    var result = data.data;
				$("#"+"${selectId}"+"-pointIds").html('<option value="-1">--全部--</option>');
				for(var i=0;i<result.length;i++){
					$("#"+"${requestScope.selectId}"+"-pointIds").append("<option value=\""+result[i].pointId+"\">"+result[i].pointName+"</option>");
				}
				$("#"+"${selectId}"+"-pointIds").selectpicker('refresh');

			},"json");
		}else{
			$("#"+"${selectId}"+"-pointIds").html('<option value="-1">--全部--</option>');
			$("#"+"${selectId}"+"-pointIds").selectpicker('refresh');
		}
	});*/

});

</script>
<body>
<div class="input-group" style="float:left;" id="proc_dep_id1_id">
    <select class="selectpicker"  data-live-search="true"  id="data-recode-pointIds">
        <option value="-1">--全部--</option>
        <c:forEach items="${pointList}" var="level1Code">
            <option value="${level1Code.pointId}">${level1Code.pointName}</option>
        </c:forEach>
    </select>
</div>
</body>
</html>