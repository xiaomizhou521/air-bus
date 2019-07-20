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
    <title>修改点信息页面</title>
    <script>
        $(function(){
            $("#pointTypeId").val('${pointVo.pointType}');
        })
        function goBack(){
            history.go(-1)
        }

    </script>
</head>
<body>
<form action="/work/editPointDo" method="post">
    <div style="width: 100%">
        <div style="margin:auto;width:800px;margin-top:20px;">
         <table class="table table-bordered table-striped table-condensed" >
           <tr>
              <td style="width: 20%;">点ID：</td>
              <td>${pointVo.pointId}<input type="hidden" value="${pointVo.pointId}" name="pointId"/></td>
           </tr>
           <tr>
              <td>点名称：</td>
              <td>${pointVo.pointName}</td>
           </tr>
           <tr>
               <td>备注：</td>
               <td><input type="text" class="form-control" value="${pointVo.remarksName}" name="remarksName"/></td>
           </tr>
           <tr>
               <td>点类型：</td>
               <td>
                   <select  class="form-control" name="pointType" id="pointTypeId">
                       <option value="instant">瞬时点</option>
                       <option value="usage">用量点</option>
                   </select>
               </td>
           </tr>
           <tr>
               <td>点单位：</td>
               <td><input type="text" class="form-control" value="${pointVo.pointUnit}" name="pointUnit"/></td>
           </tr>
           <tr>
               <td>所属栋号：</td>
               <td><input type="text" class="form-control" value="${pointVo.blockNo}" name="blockNo"/></td>
           </tr>
           <tr>
               <td>点文件相对路径：</td>
               <td><input type="text" class="form-control" value="${pointVo.fileRelativePath}" name="fileRelativePath"/></td>
           </tr>
           <tr>
               <td>CSV文件名前缀：</td>
               <td><input type="text" class="form-control" value="${pointVo.filePrefixName}" name="filePrefixName"/></td>
           </tr>
           <tr>
               <td colspan="2" style="text-align: center">
                   <input type="button" style="width:100px" class="btn form-control btn-default" onclick="goBack()" value="取消">
                   <input type="submit" style="width:100px" class="btn form-control btn-default btn-primary" value="确定">
               </td>
           </tr>
       </table>
      </div>
     </div>
</form>
</body>
</html>
