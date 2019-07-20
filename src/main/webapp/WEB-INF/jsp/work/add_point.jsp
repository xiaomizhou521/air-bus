<%--
  Created by IntelliJ IDEA.
  User: xiaox
  Date: 2019/7/13
  Time: 21:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="../static/css/bootstrap/bootstrap.css">
    <script type="text/javascript" src="../static/css/bootstrap/jquery-1.11.1.js" ></script>
    <script type="text/javascript" src="../static/css/bootstrap/bootstrap.min.js"></script>
    <title>增加点信息页面</title>
    <script>
        $(function(){
            $("#pointTypeId").val('${pointVo.pointType}');

            $("#pointNameId").blur(function(){
                var pointName = $(this).val().trim();
                if(pointName!=null&&pointName!=''){
                    var collectionUrl = "/work/checkPointNameRepeat";
                    $.post(collectionUrl,
                        {'pointName':pointName},
                        function(data) {
                            var code  = data.code;
                            var mess = "";
                            if(code==-1){
                                mess="<font color='red'>点名称已存在</font>";
                            }else if(code==1){
                                mess="<font color='green'>点名称有效<font>";
                            }else{
                                alert("error");
                            }
                            $("#messId").html(mess);

                        },"json");
                }else{
                    $("#messId").html("");
                }
            });
        })
        function goBack(){
            history.go(-1)
        }


        function checkForm(){
            var pointNameId = $("#pointNameId").val().trim();
            if(pointNameId==null||pointNameId == ''){
                alert("请输入点名称");
                return false;
            }

            var flg = true;
            $.ajax({
                url:'/work/checkPointNameRepeat',
                type:'GET',
                dataType: "json",
                async:false,
                data:{pointName:pointNameId},
                success:function(result){
                    var code  = result.code;
                    if(code==-1){
                        alert("点名称已存在");
                        flg = false;
                    }
                }
            })

            if(!flg){
                return false;
            }

            var pointTypeId = $("#pointTypeId").val();
            if(pointTypeId==null||pointTypeId==''){
                alert("请选择点类型");
                return false;
            }
            var fileRelativePathId = $("#fileRelativePathId").val();
            if(fileRelativePathId==null||fileRelativePathId==''){
                alert("请输入点文件相对路径");
                return false;
            }
            var filePrefixNameId = $("#filePrefixNameId").val();
            if(filePrefixNameId==null||filePrefixNameId==''){
                alert("请输入CSV文件名前缀");
                return false;
            }
        }

    </script>
</head>
<body>
<form action="/work/addPointDo" method="post">
    <div style="width: 100%">
        <div style="margin:auto;width:800px;margin-top:20px;">
         <table class="table table-bordered table-striped table-condensed" style="width:800px" >
           <tr>
              <td style="width:20%">点名称：</td>
              <td>
                  <div style="float:left;"> <input type="text" class="form-control" value="" style="width: 300px;" id="pointNameId" name="pointName"/></div>
                  <div style="float:left;"><span id="messId"></span></div>
              </td>
           </tr>
           <tr>
               <td>点别名：</td>
               <td><input type="text" class="form-control" value="" id="remarksNameId" name="remarksName"/></td>
           </tr>
           <tr>
               <td>点类型：</td>
               <td>
                   <select  class="form-control" name="pointType" id="pointTypeId">
                       <option value="">请选择</option>
                       <option value="instant">瞬时点</option>
                       <option value="usage">用量点</option>
                   </select>
               </td>
           </tr>
           <tr>
               <td>点单位：</td>
               <td><input type="text" class="form-control" value="" name="pointUnit"/></td>
           </tr>
           <tr>
               <td>所属栋号：</td>
               <td><input type="text" class="form-control" value="" name="blockNo"/></td>
           </tr>
           <tr>
               <td>点文件相对路径：</td>
               <td><input type="text" class="form-control" value="" id="fileRelativePathId" name="fileRelativePath"/></td>
           </tr>
           <tr>
               <td>CSV文件名前缀：</td>
               <td><input type="text" class="form-control" value="" id="filePrefixNameId" name="filePrefixName"/></td>
           </tr>
           <tr>
               <td colspan="2" style="text-align: center">
                   <input type="button" style="width:100px" class="btn form-control btn-default" onclick="goBack()" value="取消">
                   <input type="submit" style="width:100px" class="btn form-control btn-default btn-primary" onclick="return checkForm()" value="确定">
               </td>
           </tr>
       </table>
      </div>
     </div>
</form>
</body>
</html>
