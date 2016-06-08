<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <spring:url value="/version/" var="versionUrl"/>

    <div class="col-xs-12">
        <div class="breadcrumbs" id="breadcrumbs">
            <script type="text/javascript">
                try {
                    ace.settings.check('breadcrumbs', 'fixed')
                } catch (e) {
                }
            </script>

            <ul class="breadcrumb">
                <li><i class="icon-home home-icon"></i> <a
                        href="./list">版本管理</a>
                </li>
                <li><a href="javascript:void(0)">版本管理列表</a>
                </li>

            </ul>
        </div>

        <table id="ynf_list"></table>
        <div id="ynf_list_pager"></div>
    </div>

</div>

<script>
    (function () {

        var table = {};
        table.url = "${versionUrl}listgrid";
        table.editurl = "${versionUrl}listgrid";

        YNF_CONFIG.table = {};
        YNF_CONFIG.table = table;
        seajs.use("version_list");

    })();

</script>