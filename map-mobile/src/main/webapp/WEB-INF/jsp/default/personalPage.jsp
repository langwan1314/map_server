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
    <link href="assets/css/youngo.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style type="text/css">
    	@media (min-width: 768px) {
		   .container {
		   	  margin-top: 100px;
		      width: 420px;
			}
		}
		@media (min-width: 992px) {
		   .container {
		   	  margin-top: 150px;
		      width: 420px;
			}
		}
		@media (min-width: 1200px) {
		   .container {
		      margin-top: 200px;
		      width: 420px;
			}
		}
    </style>
</head>
<body>
    <div class="container" style="padding: 0px; position: relative;">
    	<div class="personalShade">
    	</div>
     	<div id="info" class="info">
    		<div id="infoBackground"  style="background-image: url('${userImage}');">
    		</div>
    		<div class="infoShade">
    		</div>
    		<div  style="width:100%; height:100%; position: relative;">
	    		<div class="infoFoot">
	    		</div>
	    		<div id="userName" >${niciName}</div>
	    		<div id="userImage" style="background-image: url('${userImage}');"></div>
	    		<div style="width: 100%; height: 2em; padding: 0.8em 0em 0em 0em; overflow-x:hidden ;" class="row">
					<div class="col-xs-6" style="height:100%;">
						<div class="row" style="height:100%;">
							<div id="userCountry" class="col-xs-2 col-xs-push-6" style="background-image: url('image/country/${country}.png');">
							</div>
							<div id="userSex" class="col-xs-2 col-xs-push-6" <c:if test="${sex =='man'}"> style="background-image: url('image/personalpage/male.png');"</c:if>>
							</div>
							<div id="userAge" class="col-xs-2 col-xs-push-6">
								${age}
							</div>
						</div>
					</div>
	    			<div class="col-xs-6" style="height:100%;">
	    				<div class="row" style="height:100%;">
	    					<div class="col-xs-2 locationImage">
	    					</div>
	    					<div id="userLocation" class="col-xs-9" style="height: 100%;">
	    						<div  style="height: 100%; display: inline;" >
									<nobr>${userCurrentCountry}·${userCurrentCity}</nobr>
	    						</div>
							</div>
	    				</div>
	    			</div>
	    		</div>
	    		<div style="width: 100%; height: 2em; padding: 0.8em 0em 0em 0em" class="row">
					<div class="col-xs-6" style="height:100%;">
						<div class="row" style="height:100%;">
							<div id="userGoodAtLanguage" class="col-xs-11">
								<nobr>
									<c:forEach items="${goodAtLanguages}" var="language" varStatus="status" >
					    				${language} <c:if test="${!status.last }">;</c:if>
				    				</c:forEach>
				    			</nobr>
							</div>
							<div class="col-xs-1 exchange">
							</div>
						</div>
					</div>    		
	    			<div class="col-xs-6" style="height:100%;">
	    				<div class="row" style="height:100%;">
	    					<div id="#userInterLanguage" class="col-xs-12">
	    						<nobr>
									<c:forEach items="${interestLanguages}" var="language" varStatus="status" >
					    				${language} <c:if test="${!status.last }">;</c:if>
				    				</c:forEach>
			    				</nobr>
							</div>
	    				</div>
	    			</div>
	    		</div>
    		</div>
    	</div>
    	
    	<div class="introduction">
    		<div id="nav">
    			<div class="introNavImage"></div>
    			<div class="introTitle">自我介绍	</div>
    			<div style="float: right; ">
	    			<div id="introVoice"></div>
	    			<div id="userVoice">${voiceLength}</div>
    			</div>
    		</div>
    		<c:choose>
    			<c:when test="${null == images && null == text}">
    				<div>
    					<img alt="" src="image/personalpage/profile_empty_en.png" style="width: 100%;">
    				</div>
    			</c:when>
    			<c:otherwise>
		    		<c:if test="${null != images}">
		    		<div class="introPicContainer">
		    			<div id="pictures"> 
		    				<c:forEach items="${images}" var="url" varStatus="status">
			    				<div class="pictureDiv" style="left:${status.index * 8}em;">
			    					<img alt="" src="${url}" class="img-thumbnail" onclick="pictureOnClick(${status.index})">
			    				</div>
		    				</c:forEach>
		    			</div>
		    		</div>
		    		<div class="sepDesc">
						<div class="sepRight"></div>
					</div>
					</c:if>
		    		<div id="text">
		    			${text}
		    		</div>
    			</c:otherwise>
    		</c:choose>
    	</div>
    	
    	<footer class="footer navbar-fixed-bottom">
			<div class="downloadButton"><p>点击下载</p></div>
		</footer>
    	
    </div>
	
	

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
    
    <script type="text/javascript">
    	function pictureOnClick(index){
    		window.open("imagePage?userId=" + "${userId}" + "&pageNum=" + index);
    	}
    
    
    </script>
</body>
</html>