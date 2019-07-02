<%--
  Created by IntelliJ IDEA.
  User: xiaox
  Date: 2019/7/2
  Time: 21:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!-- Bootstrap core CSS -->
    <link href="asserts/css/bootstrap.min.css" th:href="@{/webjars/bootstrap/4.0.0/css/bootstrap.css}" rel="stylesheet">
    <title>登陆页面</title>
</head>
<body>
   <div>
       <form action="/air-bus/loginDo" method="post">
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
