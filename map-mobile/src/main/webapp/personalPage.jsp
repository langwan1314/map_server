<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1.0, user-scalable=no"">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>Bootstrap 101 Template</title>

    <!-- Bootstrap -->
    <link href="assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">

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
		
		::-webkit-scrollbar-track {
			background-color: #b46868;
		} /* 滚动条的滑轨背景颜色 */
		 
		::-webkit-scrollbar-thumb {
			background-color: rgba(0, 0, 0, 0.2);
		} /* 滑块颜色 */
		 
		::-webkit-scrollbar-button {
			background-color: #7c2929;
		} /* 滑轨两头的监听按钮颜色 */
		
		::-webkit-scrollbar-corner {
			background-color: black;
		}/* 横向滚动条和纵向滚动条相交处尖角的颜色 */
		
    </style>
    
</head>
<body  style="">
    <div class="container" style="padding: 0px; position: relative;">
    	<div style="position: absolute; z-index: -10;  background-color: #e6eef4; width: 100%; height: 100%;">
    	</div>
     	<div id="info" style="width: 100%;height: 18em; position: relative;">
    		<div style="width:100%; height:100%;  background-image: url('http://47.89.28.120/image/personalPortrait/157/16040814464857390527484898.png'); background-position: center; background-repeat: no-repeat; position: absolute;z-index:-3">
    		</div>
    		<div style="width:100%; height:100%;  background-color:#3776cf; filter: alpha(opacity = 90); -moz-opacity: 0.9; opacity: 0.9; position: absolute;z-index:-2">
    		</div>
    		
    		<div  style="width:100%; height:100%; position: relative;">
	    		<div style="background-image: url('image/personalpage/profile_bg.png'); width: 100%; height: 9em; background-position: center; background-repeat: no-repeat; background-size:cover; position: absolute; bottom: 0em;z-index:-1">
	    		</div>
	    		<div style="width:100%; height: 2.5em; text-align: center; line-height: 2em; font-size:2em; color: #fff; overflow: hidden;">zhezhiren</div>
	    		<div style="width: 8em; height: 8em;border-radius: 50%; background-color:#fff;  border:5px solid #fff;  background-image: url('http://47.89.28.120/image/personalPortrait/157/16040814464857390527484898.png'); background-position: center; background-repeat: no-repeat; background-size:cover; margin-left: auto;margin-right: auto; position: relative;">
	    		</div>
	    		<div style="width: 100%; height: 2em; padding: 0.8em 0em 0em 0em" class="row">
					<div class="col-xs-6" style="height:100%;">
						<div class="row" style="height:100%;">
							<div class="col-xs-2 col-xs-push-6" style="height:100%;background-image: url('image/country/China.png'); background-repeat: no-repeat;background-size:auto 100%; background-position: left;">
							</div>
							<div class="col-xs-2 col-xs-push-6" style="height:100%;background-image: url('image/personalpage/female.png'); background-repeat: no-repeat;background-size:auto 100%; background-position: right;;">
							</div>
							<div class="col-xs-2 col-xs-push-6" style="height:100%; color: #7d899c;">
								25
							</div>
						</div>
					</div>    		
	    			<div class="col-xs-6" style="height:100%;">
	    				<div class="row" style="height:100%;">
	    					<div class="col-xs-2" style="height:100%;background-image: url('image/personalpage/profile_location.png'); background-repeat: no-repeat;background-size:auto 100%; background-position:right;">
	    					</div>
	    					<div class="col-xs-9" style="height:100%; color: #7d899c;">
								中国·长沙
							</div>
	    				</div>
	    			</div>
	    		</div>
	    		<div style="width: 100%; height: 2em; padding: 0.8em 0em 0em 0em" class="row">
					<div class="col-xs-6" style="height:100%;">
						<div class="row" style="height:100%;">
							<div class="col-xs-11" style="height:100%; color: #354156; text-align: right;">
								日本语
							</div>
							<div class="col-xs-1" style="height:100%;background-image: url('image/personalpage/language_change.png'); padding:0px; background-repeat: no-repeat; background-position: center;">
							</div>
						</div>
					</div>    		
	    			<div class="col-xs-6" style="height:100%;">
	    				<div class="row" style="height:100%;">
	    					<div class="col-xs-12" style="height:100%; color: #354156;">
								中文，英语，德语
							</div>
	    				</div>
	    			</div>
	    		</div>
    		</div>
    	</div>
    	
    	<div id="introduction" style="margin-top: 1em; margin-bottom:3em; width: 100%; min-height: 200px; padding: 1em 1em 1em 1em; background-color: #fff;" >
    		<div id="nav">
    			<div style="height: 1em; width: 1em; background-image: url('image/personalpage/profile_introduce.png');background-repeat: no-repeat; background-size:cover; display: inline-block; vertical-align:middle" ></div>
    			<div style="height: 1.5em; display: inline-block; line-height: 1.5em; font-size: 1.0em; vertical-align:middle;padding-left: 0.5em; color: #7d899c;">
    				自我介绍
    			</div>
    			<div style="float: right; ">
	    			<div style="height: 1.4em; width: 1.4em; background-image: url('image/personalpage/play_wave3.png');background-repeat: no-repeat; background-size:100% auto; display: inline-block; vertical-align:middle; cursor: pointer;" ></div>
	    			<div style="height: 1.5em; display: inline-block; line-height: 1.5em; font-size: 1.0em; vertical-align:middle;padding-left: 0.5em; color: #7d899c;">
	    				22
	    			</div>
    			</div>
    		</div>
    		
    		<div id="picture" style="width:100%; height: 8em; margin-top: 0.5em;">
    			<div style="height: 8em; overflow-x:scroll; overflow-y:hidden; position: relative;"> 
    				<div class="" style="display: inline-block; width: 8em; height: 8em; position: absolute; left:0em; cursor: pointer;">
    					<img alt="" src="http://47.89.28.120/image/personalPortrait/151/16032911359537390527482031.png" class="img-thumbnail">
    				</div>
    				<div class="" style="display: inline-block; width: 8em; height: 8em; position: absolute; left:8em; cursor: pointer;">
    					<img alt="" src="http://47.89.28.120/image/personalPortrait/155/16032911531687390527486704.png" class="img-thumbnail">
    				</div>
    				<div class="" style="display: inline-block; width: 8em; height: 8em; position: absolute; left:16em; cursor: pointer;">
    					<img alt="" src="http://47.89.28.120/image/personalPortrait/156/16032912012297390527484251.png" class="img-thumbnail">
    				</div>
    				<div class="" style="display: inline-block; width: 8em; height: 8em; position: absolute; left:24em; cursor: pointer;">
    					<img alt="" src="http://47.89.28.120/image/personalPortrait/157/16040814464857390527484898.png" class="img-thumbnail">
    				</div>
    				<div class="" style="display: inline-block; width: 8em; height: 8em; position: absolute; left:32em; cursor: pointer;">
    					<img alt="" src="http://47.89.28.120/image/personalPortrait/158/16032915254087390527486646.png" class="img-thumbnail">
    				</div>
    				<div class="" style="display: inline-block; width: 8em; height: 8em; position: absolute; left:40em; cursor: pointer;">
    					<img alt="" src="http://47.89.28.120/image/personalPortrait/159/16032916379607390527482992.png" class="img-thumbnail">
    				</div>
    			</div>
    		</div>
    		<div class="sepDesc" style="position: relative; width: 100%; line-height: 0px; height: 2px; margin: 12px 0px 12px 0px; padding: 0px;">
				<div class="sepRight" style="position: absolute; top: 1px; left: 0px; display: inline-block; z-index: 6; background-color: #e7e7e7; width: 100%; line-height: 0px; height: 1px; margin: 0px 0px 0px 0px; padding: 0px;"></div>
			</div>
    		<div id="text">
    			这是一锅很长的个人说明，然而也未必会很长，元芳你觉得呢。
    		</div>
    	</div>
    	
    	<footer class="footer navbar-fixed-bottom">
			<div style="text-align: center; background-color: #e6eef4; height: 3em; width: 100%; line-height: 3em;"><p>点击下载</p></div>
		</footer>
    	
    </div>
	
	

    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="assets/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>