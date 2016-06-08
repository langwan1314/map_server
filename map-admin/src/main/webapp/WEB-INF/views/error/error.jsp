<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<h1>
		<!-- <spring:message code="error_subject" />  -->
		Oops
	</h1>
	<p>
		<!-- <spring:message code="error_desc" /> -->
		It seems that something went wrong. We are sorry about the inconvenience.		
	</p>


	Failed URL: ${url} <br> Exception: ${exception.message}
	<%--  <c:forEach items="${exception.stackTrace}" var="ste">    ${ste} 
    </c:forEach> --%>

</div>