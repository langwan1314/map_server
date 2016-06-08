<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bello</title>

    <!-- Bootstrap -->
    <link href="assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style type="text/css">
    	@media (min-width: 768px) {.container {width: 420px;}}
		@media (min-width: 992px) {.container {width: 420px;}}
		@media (min-width: 1200px) {.container {width: 420px;}}
		html,body{height:97%;}
		.centerImageParent{height: 100%;position:relative;}
		.centerImage{margin: auto;position: absolute;top: 0;left: 0;bottom: 0;right: 0;}
		.pageNum{width:100%; height:2em; line-height:2em; text-align: center; color: #fff; filter: alpha(opacity = 50); -moz-opacity: 0.5; opacity: 0.5; position: absolute; bottom: 0px; z-index: 1}
    </style>
</head>
<body  style="background-color: black;">
    <div class="container" style="padding: 0px; position: relative; vertical-align:middle; height: 100%;">
    	<div>
    	</div>
    
	    <div id="myCarousel" class="carousel slide" style=" height: 100%;">
	    <!-- 轮播（Carousel）指标 -->
	   
	    <div id="imageNum" class="pageNum">
	   		<span id="currentNum">${pageNum + 1}</span>/${size}
	    </div>
	   
	    <!-- 轮播（Carousel）项目 -->
	    <div class="carousel-inner" style="height: 100%;" >
			<c:forEach items="${images}" var="url" varStatus="status">
		    	<div class="item centerImageParent <c:if test='${status.index == pageNum}'>active</c:if>">
		        	<img class="centerImage" src="${url}" alt="First slide" />
		    	</div>
			</c:forEach>
	   </div>
	   
	   <!-- 轮播（Carousel）导航 -->
	   <a class="carousel-control left" href="#myCarousel"
	      data-slide="prev" style="background-image: url('image/personalpage/list_arrow_l.png');background-repeat: no-repeat; background-position: center;"></a>
	   <a class="carousel-control right" href="#myCarousel"
	      data-slide="next"  style="background-image: url('image/personalpage/list_arrow_r.png');background-repeat: no-repeat; background-position: center;"></a>
		</div> 
    </div>

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
	
	<script>
		$(function(){
		      $('#myCarousel').on('slide.bs.carousel', function () {
		    	  var currentItem = $(this).find('.item.active')
		    	  var items = $(currentItem).parent().children('.item');
		    	  var num = $(items).index(currentItem)
		    	  num = num +1 + 1;
		    	  var currentNum = num % items.length;
		    	  if(currentNum == 0){
		    		  currentNum = items.length;
		    	  }
		    	  $('#currentNum').html(currentNum);
		      });
		});
	</script>
</body>
</html>