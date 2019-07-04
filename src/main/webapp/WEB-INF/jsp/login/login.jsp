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
    <link rel="stylesheet" type="text/css" href="../static/js/bootstrap/css/bootstrap.css">
    <script type="text/javascript" src="../static/js/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="../static/js/jquery/jquery-1.11.1.js" ></script>
    <title>登陆页面</title>
    <script>
        $(function () {

        })
    </script>
</head>
<body>
   <div>
       <form action="/login/loginDo" method="post">
           <table class=" table">
               <tr>
                   <td>用户名</td><td><input type="text" class ="form-control" placeholder="" id="userName" name="userName" value=""/></td>
               </tr>
               <tr>
                   <td>密码</td><td><input type="text" class ="form-control" placeholder="" id="passWord" name="passWord" value=""/></td>
               </tr>
               <tr>
                   <td colspan="2"><input type="submit" class="btn form-control" value="登陆"></td>
               </tr>
               <tr>
                   <td colspan="2">${message}</td>
               </tr>
           </table>
       </form>
   </div>
</body>
</html>
