<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>

<!-- 
在具体views 定义里记得添加 titleKey 属性 	
-->
<c:set var="titleKey">
	<tiles:getAsString name="titleKey" />
</c:set>
<c:set var="menuTag">
	<tiles:getAsString name="menuTag" />
</c:set>

<title><spring:message code="${titleKey}"/></title>
<link rel="shortcut icon" href="./assets/images/favicon.ico"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
 
<!-- CSS -->
<spring:url value="/resources/assets/css/bootstrap.min.css"
	var="bootstrap_css" />
<spring:url value="/resources/assets/css/font-awesome.min.css"
	var="font_awesome_css" />
<spring:url value="/resources/assets/css/ui.jqgrid.css"
	var="ui_jqgrid_css" />
<spring:url value="/resources/assets/css/bootstrap-timepicker.css"
	var="bootstrap_timepicker_css" />
<spring:url value="/resources/assets/css/ace.min.css" var="ace_css" />
<spring:url value="/resources/assets/css/ace-rtl.min.css"
	var="ace_rtl_css" />
<spring:url value="/resources/assets/css/ace-skins.min.css"
	var="ace_skins_css" />
<spring:url value="/resources/assets/css/youngo.css" var="youngo_css" />

<link rel="stylesheet" type="text/css" href="${bootstrap_css}" />
<link rel="stylesheet" type="text/css" href="${font_awesome_css}" />
<link rel="stylesheet" type="text/css" href="${ui_jqgrid_css}" />
<link rel="stylesheet" type="text/css"
	href="${bootstrap_timepicker_css}" />
<link rel="stylesheet" type="text/css" href="${ace_css}" />
<link rel="stylesheet" type="text/css" href="${ace_rtl_css}" />
<link rel="stylesheet" type="text/css" href="${ace_skins_css}" />
<link rel="stylesheet" type="text/css" href="${youngo_css}" />

<!-- 兼容IE CSS  -->
<spring:url value="/resources/assets/css/font-awesome-ie7.min.css"
	var="font-awesome-ie7_css" />

<spring:url value="/resources/assets/js/jquery-1.11.1.min.js"
	var="jquery_url" />


<!-- JS -->
<spring:url value="/resources/assets/js/bootstrap.min.js"
	var="bootstrap_url" />
<spring:url value="/resources/assets/js/ace.min.js" var="ace_url" />
<spring:url value="/resources/assets/js/ace-elements.min.js"
	var="ace_elements_url" />
<spring:url value="/resources/assets/js/jqGrid/jquery.jqGrid.js"
	var="jqGrid_url" />
<spring:url value="/resources/assets/js/jqGrid/grid.locale-en.js"
	var="grid_locale_en_url" />
<spring:url value="/resources/assets/js/bootstrap-datepicker.min.js"
	var="bootstrap_datepicker_url" />
<spring:url value="/resources/assets/js/seajs-2.3.0/seajs.js"
	var="seajs_url" />
<spring:url value="/resources/assets/youngo/config.js"
	var="youngo_config_url" />
<spring:url value="/resources/assets/youngo/jquery.youngo.js"
	var="jquery_youngo_url" />
	
<spring:url value="/resources/assets/js/youngo_datetimepicker.js"
	var="youngo_datetimepicker_url" />
	

<body class="">
	<div id="headerWrapper">
		<tiles:insertAttribute name="header" ignore="true" />
	</div>
	<div class="main-container" id="main-container">
		<script>
			try {
				ace.settings.check('main-container', 'fixed');
			} catch (e) {
			}
		</script>

		<div class="main-container-inner">
			<a class="menu-toggler" id="menu-toggler" href="javascript:void(0)">
				<span class="menu-text"></span> </a>
				
			<!-- 菜单模板 注意这里是模板而不是属性，因为需要传递 menuTag 到下一级 -->
			<tiles:insertTemplate  template="/WEB-INF/views/template/menu.jsp">
				<tiles:putAttribute name="menuTag" value="${menuTag}" />
			</tiles:insertTemplate>
						
			

			<!-- 主要内容 -->
			<div id="main-content" class="main-content">

				<script src="${jquery_url}">
					<jsp:text/>
				</script>

				<script src="${bootstrap_url}">
					<jsp:text/>
				</script>
				<script src="${ace_url}">
					<jsp:text/>
				</script>
				<script src="${ace_elements_url}">
					<jsp:text/>
				</script>
				<script src="${jqGrid_url}">
					<jsp:text/>
				</script>
				<script src="${grid_locale_en_url}">
					<jsp:text/>
				</script>
				<script src="${bootstrap_datepicker_url}">
					<jsp:text/>
				</script>
				<script src="${seajs_url}">
					<jsp:text/>
				</script>
				<script src="${youngo_config_url}">
					<jsp:text/>
				</script>
				<script src="${jquery_youngo_url}">
					<jsp:text/>
				</script>
				
			   


				<tiles:insertAttribute name="body" />
				<tiles:insertAttribute name="footer" ignore="true" />
			</div>

		</div>

		<a href="javascript:void(0)" id="btn-scroll-up"
			class="btn-scroll-up btn btn-sm btn-inverse"> <i
			class="icon-double-angle-up icon-only bigger-110"></i> </a>

	</div>
</body>
</html>
