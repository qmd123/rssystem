<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>餐品列表</title>
    <link rel="stylesheet" type="text/css" href="${ctp}/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctp}/css/index.css"/>
    <script type="text/javascript" src="${ctp}/js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="${ctp}/js/bootstrap.min.js"></script>
</head>
<body>
<div  class="navbar-fixed-top" id="TitleArea">餐品列表</div>
<!-- 过滤条件 -->
<div id="TopMainArea">
    <br><br>
        <form action="${ctp}/business/productList" method="post">
            &nbsp;
            <input type="search" name="name" value="${name}" placeholder="请输入菜品名称">
            <input type="submit" value="搜索">
            <br>
        </form>
    <table class="table table-hover" align="center">
        <!-- 表头-->
        <thead>
        <tr align="center" valign="middle" id="TableTitle">
            <td>餐品编号</td>
            <td>餐品名称</td>
            <td>价格</td>
            <td>餐品类型</td>
            <td>图片</td>
            <td>评分</td>
            <td>操作</td>
        </tr>
        </thead>
        <!--显示数据列表 -->
        <tbody id="TableData">
        <c:forEach items="${sessionScope.products}" var="product" varStatus="u">
            <tr height="60" align="center">
                <td>${u.count}</td>
                <td>${product.productName}</td>
                <td>${product.productPrice}</td>
                <td>菜&nbsp;</td>
                <td>
                    <img border="0" width="20" height="20" src="${ctp}/${product.photosrc}?id=<%=Math.random()%>"
                         alt="暂无图片"/>&nbsp;
                </td>
                <td></td>
                <td>
                    <a href="${ctp}/business/updateProducts?id=${product.id}">更新</a>
                    <a href="${ctp}/product/delete?id=${product.id}" onclick="return confirm('确定要删除吗？')">删除</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div id="TableTail" align="center">
        <div class="btn">
            <a href="${ctp}/business/uploadProducts">添加</a>
        </div>
    </div>
</div>

</body>
</html>
