<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="menuTag">
    <tiles:getAsString name="menuTag"/>
</c:set>

<spring:url value="/version/list" var="versionListUrl"/>
<spring:url value="/version/update" var="versionUpdateUrl"/>
<spring:url value="/prosecute/list" var="prosecuteListUrl"/>

<script>
    var YNF_CONFIG_MENU = "${menuTag}";
    var YNF_COUNT_PENDING = "${countPendingUrl}";
</script>


<div class="sidebar" id="sidebar">
    <script type="text/javascript">
        try {
            ace.settings.check('sidebar', 'fixed');
        } catch (e) {
        }
    </script>

    <ul class="nav nav-list" id="ynf_nav">
        <li class="ynf-nav-item" data-y-page="prosecute_list"><a
                href="${prosecuteListUrl}" class="dropdown-toggle"> <i
                class="icon-list"></i> <span class="menu-text"> 投诉举报 </span> <b
                class="arrow icon-angle-down"></b> </a> <!-- 三级菜单 -->
            <ul class="submenu">
                <li class="ynf-nav-item" data-active-flag="prosecute_list"><a
                        href="${prosecuteListUrl}"> <i class="icon-double-angle-right"></i>
                    投诉举报 </a></li>
            </ul>
        </li>

        <li class="ynf-nav-item" data-y-page="version_update"><a
                href="${versionListUrl}" class="dropdown-toggle"> <i
                class="icon-list"></i> <span class="menu-text"> 版本管理 </span> <b
                class="arrow icon-angle-down"></b> </a> <!-- 三级菜单 -->
            <ul class="submenu">
                <li class="ynf-nav-item" data-active-flag="version_update"><a
                        href="${versionUpdateUrl}"> <i
                        class="icon-double-angle-right"></i> 版本更新 </a></li>
                <li class="ynf-nav-item" data-active-flag="version_list"><a
                        href="${versionListUrl}"> <i class="icon-double-angle-right"></i>
                    版本更新列表 </a></li>
            </ul>
        </li>
    </ul>

    <div class="sidebar-collapse" id="sidebar-collapse">
        <i class="icon-double-angle-left" data-icon1="icon-double-angle-left"
           data-icon2="icon-double-angle-right"></i>
    </div>
    <script type="text/javascript">
        // try{ace.settings.check('sidebar' , 'collapsed')}catch(e){};
    </script>
</div>
