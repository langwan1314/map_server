<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="row">
	<div id="greeting">
	   <h3 style="text-align:center">${greeting}</h3>
	   <img alt="" src="./assets/images/welcome1.gif" style="display:block; margin:0 auto;
	   ">
	   <a href="./goods/list" class="btn btn-info btn-block" style="display:block;width:600px;margin:0 auto; ">进入系统</a>
	</div>
</div>
<script>
	$("#main-content").css("marginLeft",0);
</script>
<!-- /.row -->