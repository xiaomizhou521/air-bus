<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html  xmlns:th="http://www.thymeleaf.org">
<body>
<header th:fragment="header">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-header">
            <a href="javascript:;" class="layui-hide-xs"><a href="/" style="font-weight: bold;"><div class="layui-logo">后台管理系统</div></a>
            </a>
            <a href="javascript:;" class="layui-hide-xs">
                <div class="switchMenu" style="display: none;"><i class="fa fa-indent"></i></div>
            </a>
            <ul class="layui-nav layui-layout-right">
                <li class="layui-nav-item">
                    <a href="javascript:;">
                        <img src="/images/logo.jpg" class="layui-nav-img"></img>
                        <shiro:principal property="sysUserName"></shiro:principal>
                    </a>
                    <dl class="layui-nav-child">
                        <dd id="updUsePwdDd" class="layui-this"><a href="javascript:;" onclick="updateUsePwd();">修改密码</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item"><a href="/logout">退出</a></li>
            </ul>
        </div>

        <div class="layui-side layui-bg-black">
            <div class="layui-side-scroll">
                <ul class="layui-nav layui-nav-tree"  lay-filter="test">
                    <li class="layui-nav-item layui-nav-itemed">
                        <a href="javascript:;">系统管理</a>
                        <dl class="layui-nav-child">
                            <dd><a href="/user/userManage">账号管理</a></dd>
                            <dd><a href="/role/roleManage">角色管理</a></dd>
                            <dd><a href="/permission/permissionManage">权限管理</a></dd>
                        </dl>
                    </li>
                    <li class="layui-nav-item">
                        <a href="javascript:;">基本设置</a>
                        <dl class="layui-nav-child">
                            <dd><a href="/goodsCategory/goodsCategoryManage">服务类目管理 </a></dd>
                            <dd><a href="/serviceType/serviceTypeManage">服务类型管理</a></dd>
                            <dd><a href="/payplatform/payplatManage">支付方式</a></dd>
                            <dd><a href="/bank/bankManage">银行管理</a></dd>
                            <dd><a href="/position/positionManage">省市区管理</a></dd>
                        </dl>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <script type="text/javascript" src="../static/js/layui/head.js"></script>
</header>
</body>
</html>