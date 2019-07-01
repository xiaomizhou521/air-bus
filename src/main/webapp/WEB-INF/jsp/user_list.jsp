<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户列表</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%--     <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/4.0.0-beta/css/bootstrap.min.css">
     <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
       <script src="https://cdn.bootcss.com/popper.js/1.12.5/umd/popper.min.js"></script>
       <script src="https://cdn.bootcss.com/bootstrap/4.0.0-beta/js/bootstrap.min.js"></script>--%>
</head>
<body>

<div class="container">
    <table class="table table-hover">
        <thead>
        <tr>
            <th>编号</th>
            <th>姓名</th>
            <th>密码</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${userlist}" var="user">

            <tr>
                <td>${user.user_id}</td>
                <td>${user.user_name}</td>
                <td>${user.nick_name}</td>
            </tr>
        </c:forEach>


        </tbody>
    </table>
</div>

</body>
</html>