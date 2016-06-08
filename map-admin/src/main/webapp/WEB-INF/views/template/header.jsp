<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:message code="label_logout" var="labelLogout"/>
<spring:message code="label_welcome" var="labelWelcome"/>
<spring:url value="/logout" var="logoutUrl"  />
<spring:url value="/assets/avatars/user.jpg" var="userHeadUrl"  />

<div id="header">
	<div class="navbar navbar-default" id="navbar">
		<script type="text/javascript">
			try {
				ace.settings.check('navbar', 'fixed');
			} catch (e) {
			}
		</script>

		<div class="navbar-container" id="navbar-container">
			<div class="navbar-header pull-left">
				<a href="javascript:void(0)" class="navbar-brand"> <small>
						<i class="icon-leaf"></i> Bello后台管理系统 </small> </a>
				<!-- /.brand -->
			</div>
			<!-- /.navbar-header -->
<sec:authorize access="isAuthenticated()">

        <div class="navbar-header pull-right" role="navigation">
				<ul class="nav ace-nav">

					<li class="light-blue">
					<a data-toggle="dropdown" href="javascript:void(0)"
						class="dropdown-toggle"> <img class="nav-user-photo"
							src="${userHeadUrl}" alt="Jason's Photo" /> <span
							class="user-info"> <small>${labelWelcome}</small> 
		            <sec:authentication property="principal.username" /> 
		          
  </span> <i class="icon-caret-down"></i> </a>

						<ul
							class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
							<li> 
							 <a href="${logoutUrl}">  <i class="icon-off"></i> ${labelLogout}</a></li>
						</ul></li>
				</ul>
				<!-- /.ace-nav -->
			</div>
		</div>
  </sec:authorize>

		 <!--    <div id="userinfo">
		        <sec:authorize access="isAuthenticated()">${labelWelcome}
		            <sec:authentication property="principal.username" />
		            <br/>
		            <a href="${logoutUrl}">${labelLogout}</a>
		        </sec:authorize>
		    </div> -->


		<!-- /.container -->
	</div>
</div>
