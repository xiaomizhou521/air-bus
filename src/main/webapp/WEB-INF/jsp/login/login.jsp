<%--
  Created by IntelliJ IDEA.
  User: xiaox
  Date: 2019/7/2
  Time: 21:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <link rel="stylesheet" type="text/css" href="../static/css/bootstrap/bootstrap.css">
    <script type="text/javascript" src="../static/css/bootstrap/jquery-1.11.1.js" ></script>
    <script type="text/javascript" src="../static/css/bootstrap/bootstrap.min.js"></script>
    <title>登陆页面</title>
    <script>
        $(function () {

        })

        function checkSubumit(){
            var userName = $("#userName").val().trim();
            if(userName == null||userName == ""){
                $("#messageId").text("请输入用户名!");
                return false;
            }
            var passWord = $("#passWord").val().trim();
            if(passWord == null||passWord == ""){
                $("#messageId").text("请输入密码!");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
   <div style="height: 100%;    background: url(../static/images/ariplan.png);">
       <div style="width:400px;margin-left:60%;height: 200px;padding-top: 200px">
       <form action="/login/loginDo" method="post">
           <table class="" style="width:400px;height: 300px;background: url(../static/images/bg.png);">
               <tr style="background-color: #428bca;height: 50px;">
                   <td style="vertical-align:middle;text-align: left;color: white;" colspan="2"><span style="font-size: 20px;font-weight:bold">&nbsp;&nbsp;&nbsp;&nbsp;用户登陆</span></td>
               </tr>
               <tr style="height: 70px;">
                   <td style="vertical-align: middle;">
                       <div style="width:100%">
                           <div style="width:370px;margin:auto;"><input type="text"  placeholder="用户名" class ="form-control" style="height: 50px;" placeholder="" id="userName" name="userName" value=""/></div>
                       </div>
                   </td>
               </tr>
               <tr style="height: 70px;">
                   <td style="vertical-align: middle;">
                       <div style="width:100%">
                           <div style="width:370px;margin:auto;">
                               <input type="text" placeholder="密码" class ="form-control" style="height: 50px;" placeholder="" id="passWord" name="passWord" value=""/>
                           </div>
                       </div>
                   </td>
               </tr>
               <tr style="height: 70px;">
                   <td >
                       <div style="width:100%">
                           <div style="width:370px;margin:auto;">
                               <input type="submit" style="height: 50px;" onclick="return checkSubumit();" class="btn form-control btn-primary" value="登陆">
                           </div>
                       </div>
                       </td>
               </tr>
               <tr style="height: 30px;">
                   <td  style="color:red;" >
                       <div style="width:100%">
                           <div style="width:370px;margin:auto;" id="messageId">
                               ${message}
                           </div>
                       </div>
                   </td>
               </tr>
           </table>
       </form>
       </div>
   </div>
</body>
</html>
