<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <spring:url value="/prosecute/" var="prosecuteUrl"/>

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
                        href="./list">举报管理</a>
                </li>
                <li><a href="javascript:void(0)">举报列表</a>
                </li>

            </ul>
        </div>

        <div class="row ynf-table-nav" id="ynf_table_nav">
            <div class="col-xs-2">
                <select class="form-control" id="" data-y-role="search_filter"
                        sidx="status">
                    <option value="0">未处理</option>
                    <option value="3">已禁封</option>
                    <option value="2">已警告</option>
                    <option value="1">不处理</option>
                </select>
            </div>
        </div>

        <table id="ynf_list"></table>
        <div id="ynf_list_pager"></div>
    </div>

</div>

<script>
    (function () {

        var table = {};
        table.url = "${prosecuteUrl}listgrid";
        table.editurl = "${prosecuteUrl}listgrid";

        YNF_CONFIG.table = {};
        YNF_CONFIG.table = table;
        seajs.use("prosecute_list");

    })();

</script>