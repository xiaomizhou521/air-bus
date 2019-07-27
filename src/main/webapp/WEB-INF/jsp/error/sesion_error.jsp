<%@page import="cn.com.enorth.utility.app.web.strutsx.config.ConfigStrutsX"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Enumeration"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%--<%
    Enumeration<String> parameterNames = request.getParameterNames();
    List<String> params = new ArrayList<String>();
    boolean hasContent = false;
    while (parameterNames.hasMoreElements()) {
        String parameterName = parameterNames.nextElement();
        if (parameterName.indexOf("content") > -1) {
            hasContent = false;
            break;
        }
        String[] parameterValues = request.getParameterValues(parameterName);
        String strValues = "";
        for (String value : parameterValues) {
            strValues += value + ",";
        }

    }
    String targetUrl = request.getRequestURL().toString();
%>--%>

<!DOCTYPE html >
<html >
<head>
    <link rel="stylesheet" type="text/css" href="../static/js/bootstrap/css/bootstrap.css">
    <script type="text/javascript" src="../static/js/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="../static/js/jquery/jquery-1.11.1.js" ></script>
    <style type="text/css">
        /* CSS Document */

        /*@charset "gb2312";*/
        body {
            margin: 0px;
            font-family: "宋体",Arial, Helvetica, sans-serif;
            text-decoration: none;
        }
        td {
            font-family: "宋体",Arial, Helvetica, sans-serif;
            font-size: 12px;
            color: #000000;
            line-height: 22px;
        }
        /*链接样式开始*/

        a { color:#000000; text-decoration:none; }
        a:visited {text-decoration: none;}
        a:link {text-decoration: none;}
        a:hover {text-decoration: none;}

        .yahei{ font-family:微软雅黑}
        .zi28{ font-size:28px}
        .hanggao30{ line-height:30px}
        .zi14{ font-size:14px}
        .huise{ color:#676767}
        .baise{ color:#ffffff}
        .lanse{ color:#4d6aa4}
    </style>

    <script>
        function jumpToLogin(){
            top.location.href='/toLogin';
        }
    </script>
    <title>访问失败提示页面</title>
</head>
<body bgcolor="#f0f1f3">
<table width="617" height="100" border="0" align="center" cellpadding="0" cellspacing="0"></table>
<table width="617" height="399" border="0" align="center" cellpadding="0" cellspacing="0">
    <tbody>
    <tr>
        <td valign="top" background="../static/images/dlts_03.jpg"><table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tbody>
            <tr>
                <td width="210" height="340" align="center"><img src="../static/images/bg_06.jpg" width="58" height="208" alt=""/></td>
                <td><table width="90%" border="0" cellspacing="0" cellpadding="0">
                    <tbody>
                    <tr>
                        <td height="60" class="yahei zi28" style="border-bottom:1px solid #C1C1C1">登录失效</td>
                    </tr>
                    </tbody>
                </table>
                    <table width="90%" border="0" cellspacing="0" cellpadding="0">
                        <tbody>
                        <tr>
                            <td width="50%" valign="top" class="hanggao30 huise" style="padding:15px 0"><span class="yahei zi14">可能原因：</span><br />
                               可能您长时间没有操作，登陆信息已失效，请重新登陆！
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <table width="138" border="0" cellspacing="0" cellpadding="0">
                        <tbody>
                        <tr>
                            <td height="36" align="center">
                                <a href="javascript:void(0);" onclick="jumpToLogin()" class="yahei zi14 baise" style="display: inline-block;width:138px;height:36px;line-height:36px;background:url(../static/images/dlts_11.jpg);">退出</a>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <table width="138" border="0" cellspacing="0" cellpadding="0">
                        <tbody>
                        <tr>
                            <td height="36" align="center">
                            </td>
                        </tr>
                        </tbody>
                    </table></td>
            </tr>
            </tbody>
        </table></td>
    </tr>
    </tbody>
</table>
<table width="1000" border="0" align="center" cellpadding="0" cellspacing="0" background="images/dlts_22.jpg">
    <tbody>
    <tr>
        <td height="10"></td>
    </tr>
    </tbody>
</table>
</body>
</html>
