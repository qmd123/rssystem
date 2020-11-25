
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>餐品选购</title>
    <link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="../css/myStyle1.css">
    <script src="https://cdn.staticfile.org/jquery/2.1.1/jquery.min.js"></script>
    <script src="https://cdn.staticfile.org/twitter-bootstrap/3.3.7/js/bootstrap.min.js"></script>



</head>
<body>
<header class="container">
<div class="row">
<div class="col-md-10"><h1>河南大学餐厅服务系统</h1></div>
<div class="col-md-2"><a href=""><h1>退出登录</h1></a></div>
</div>
</header>
<!-- 导航栏 -->
<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>                        
      </button>
      <a class="navbar-brand" href="consumerhome.jsp">首页</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav">
        <li class="active"><a href="consumerhome.jsp">选择餐品</a></li>
        <li><a href="shopCart.jsp">购物车</a></li>
        <li><a href="PCenter.jsp">个人中心</a></li>
      </ul>
    </div>
  </div>
</nav>
<!-- 轮播图 -->
<div id="myCarousel" class="carousel slide">
    <!-- 轮播（Carousel）指标 -->
    <ol class="carousel-indicators">
        <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
        <li data-target="#myCarousel" data-slide-to="1"></li>
        <li data-target="#myCarousel" data-slide-to="2"></li>
        <li data-target="#myCarousel" data-slide-to="3"></li>
    </ol>   
    <!-- 轮播（Carousel）项目 -->
    <div class="carousel-inner">
        <div class="item active">
            <img src="../images/banner_1.jpg" alt="First slide">
            <div class="carousel-caption">北苑餐厅....</div>
        </div>
        <div class="item">

            <img src="${ctp}/images/banner_2.jpg" alt="Second slide">
            <div class="carousel-caption">南苑餐厅...</div>
        </div>
        <div class="item">
            <img src="../images/banner_3.jpg" alt="Third slide">
            <div class="carousel-caption">教苑餐厅...</div>
        </div>
        <div class="item">
            <img src="../images/banner_2.jpg" alt="Forth slide">
            <div class="carousel-caption">东苑餐厅...</div>
        </div>
    </div>
    <!-- 轮播（Carousel）导航 -->
    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
        <span class="sr-only">Previous</span>
    </a>
    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
        <span class="sr-only">Next</span>
    </a>
</div>
<br>
<!-- 搜索 -->
<div class="container">
<div class="input-group">
<input type="text" class="form-control input-lg"
placeholder="餐厅名/餐品关键字...">
<span class="input-group-addon btn btn-primary">搜索</span>
</div>
</div>
<!-- 中部的餐品显示 -->
<div class="middle container">
<ul>
<%!Connection conn = null; 
String username = "root";
String password = "root";
String sqlSelect = "";
ResultSet resultSet = null;
PreparedStatement psQuery=null;
//Product product  = null;
%>
<%

try{
	Class.forName("com.mysql.cj.jdbc.Driver");
	String url = "jdbc:mysql://127.0.0.1:3306/restaurantservice?useSSL=false&serverTimezone=UTC";
	conn = DriverManager.getConnection(url,username,password);
	//if(conn!=null){
		//out.print("数据库连接成功"+"<br>");
	//}else{
		//out.print("数据库连接"+"<br>");
	//}
	sqlSelect = "Select * from productinfo ";//where BsID=?
	psQuery = conn.prepareStatement(sqlSelect);
	//psQuery.setString(1, "DY00000001");
	
	resultSet = psQuery.executeQuery();
	while(resultSet.next()){
	%>
	<!-- <div class="col-md-10" style="border-bottom: 1px solid "> -->
	<li id="proinfo" class="col-md-offset-1 col-md-10 product">
	<a href="details.jsp">
		<%
		//product = new Product(resultSet.getString(2),resultSet.getString(4),"",resultSet.getDouble(5),"",1,"");
		//session.setAttribute("product", product);
		%>
		<div class="col-md-4">
			<img alt="" src="../<%=resultSet.getString(6)%>" class="col-md-3 img-rounded img-responsive">
		</div>
		<div class="col-md-6">
		<h5 >店铺号：<%= resultSet.getString("BsID")%></h5>
		
		<p style="color: red">评分：<%= resultSet.getFloat(7) %></p>
		<p><%= resultSet.getString(2)%></p>
		<p>商品ID:<%= resultSet.getString(3)%></p>
		<p style="color: red">¥：<%=resultSet.getFloat(5) %></p>
		
		</div>
	</a>
	</li>
	<!-- </div> -->	

<%
			
}
}catch(Exception e){
	e.printStackTrace();
}finally{
	if(psQuery!=null){
		psQuery.close();
	}
	if(resultSet!=null){
		resultSet.close();
	}
	if(conn!=null){
		
		conn.close();
	}
	
}
%>
</ul>
</div>
<!-- 友情链接 -->
<div class="container quick_link">
<div class="row col-md-offset-1 col-md-10" >
	<div class="col-md-4">
	<p >快速链接</p>
	</div>
	<div class="col-md-6">
	<ul>
		<li><a href="">河南大学官网</a></li>
		<li><a href="">教务系统</a></li>
		<li><a href="">英语学习</a></li>
	</ul>
	</div>
</div>
</div>
<!-- 底部 -->
<div class="container bottomdiv">
<p class="btmp">
XXX 版权所有 Copyright ©2020
<a href="https://beian.miit.gov.cn/#/Integrated/index">豫ICP备xxxxxxxx号xx</a>
豫公网安备 xxxxxxxxxxxxxx号
javaweb小组制作维护
</p>
</div>

</body>
</html>