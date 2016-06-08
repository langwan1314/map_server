<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<div class="breadcrumbs" id="breadcrumbs">
	<script type="text/javascript">
		try {
			ace.settings.check('breadcrumbs', 'fixed')
		} catch (e) {
		}
	</script>

	<ul class="breadcrumb">

		<li><a href="javascript:void(0)">版本更新</a></li>

	</ul>
	<!-- .breadcrumb -->


</div>

<div class="page-content">
	<div class="col-xs-12 header green bg-f clearfix" id="product_header">
		<h4 class=" col-xs-8">更新app版本</h4>
		<div class=" col-xs-4">
			<button class="btn btn-primary" id="submit">提交发送</button>
		</div>
	</div>
	<form:form method="post" enctype="multipart/form-data" modelAttribute="version">
		<div class="row">
			<div class="col-xs-12">
				<div class="form-horizontal" data-role="form"
					style="margin-bottom:100px; ">
					<!-- 请选择所要升级的平台：：  -->
					<div class="control-group clearfix">
						<label for="form-field-select-1"
							class="control-label bolder blue col-xs-2">请选择所要升级的平台：</label>
						<div class=" col-xs-6">

							<form:select path = "clientType" id ="clientType" name="clientType" onchange="versionAdd.clientType();">
								<option value="android" name="android">Android</option>
								<option value="ios" name="ios" >IOS</option>
							</form:select>
						</div>

					</div>
					
                    <div class="hr hr-18 dotted hr-double"></div>
                    

					<!-- 当前版本号：  -->
					<div class="control-group clearfix">
						<label for="form-field-select-1"
							class="control-label bolder blue col-xs-2">当前版本名称：</label>
						<div class=" col-xs-3">
							<input class="form-control" id="androidversion" value="${latestVersion.android}" />
							<input class="form-control" id="iosversion" value="${latestVersion.ios}" style="display:none" />
						</div>
					</div>
                    <div class="hr hr-18 dotted hr-double"></div>
					<!-- 更新后版本号：  -->
					<div class="control-group clearfix">
						<label for="form-field-select-1"
							class="control-label bolder blue col-xs-2">更新后版本号：</label>
						<div class=" col-xs-3">
							<form:input class="form-control"  path="versionNumber"  name ="versionNumber"/>
						</div>
					</div>
					<div class="hr hr-18 dotted hr-double"></div>
					<!-- 更新后版本名称：  -->
					<div class="control-group clearfix">
						<label for="form-field-select-1"
							class="control-label bolder blue col-xs-2">更新后版本名称：</label>
						<div class=" col-xs-3">
							<form:input  class="form-control" name="versionName" path="versionName" />
						</div>
					</div>
					<div class="hr hr-18 dotted hr-double"></div>
					<!-- 更新后下载地址：  -->
					<div class="control-group clearfix">
						<label for="form-field-select-1"
							class="control-label bolder blue col-xs-2">更新后下载地址：</label>
						<div class=" col-xs-3">
							<form:input value="" class="form-control" name="url" path="url" />
						</div>
					</div>
					<div class="hr hr-18 dotted hr-double"></div>
					<div class="control-group clearfix">
						<label for="form-field-select-1"
							class="control-label bolder blue col-xs-2">升级提示语：</label>
						<div class=" col-xs-3">
							<form:textarea  class="form-control" name="description" path="description"></form:textarea>
						</div>
					</div>
					
					<div class="control-group clearfix">
						<label class="control-label bolder blue col-xs-2">是否强制升级：</label>
						<div class="radio col-xs-2">
							<label><form:radiobutton name="forcibly" path="forcibly" class="ace" value="true" /><span class="lbl"> 是</span></label>
						</div>
                       <div class="radio col-xs-2">
							<label><form:radiobutton name="forcibly" path="forcibly" class="ace" value="false" /><span class="lbl"> 否</span></label>
						</div>	
					</div>
					
				</div>
			</div>

		</div>
	</form:form>
	<div class="hr hr-18 dotted hr-double"></div>


</div>

<!-- /.page-content -->
<script>
	window.onload = function() {
		seajs.use("version_update");
	};
</script>

