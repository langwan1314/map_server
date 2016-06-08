<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="row">
    <div class="col-sm-10 col-sm-offset-1">
        <div class="login-container">
            <div class="center">
                <h1>
                    <i class="icon-leaf green"></i> <span class="red">洋光教育</span> <span
                        class="white">App</span>
                </h1>
                <h4 class="blue">&copy; 洋光教育</h4>
            </div>

            <div class="space-6"></div>

            <div class="position-relative">
                <div id="login-box" class="login-box visible widget-box no-border">
                    <div class="widget-body">
                        <div class="widget-main">
                            <h4 class="header blue lighter bigger">
                                <i class="icon-coffee green"></i> 请输入您的信息
                            </h4>

                            <div class="space-6"></div>
                            <form action="j_spring_security_check" method="post">
                                <fieldset>
                                    <label class="block clearfix"> <span
                                            class="block input-icon input-icon-right"> <input
                                            id="username" name="username" type="text"
                                            class="form-control" placeholder="username"/> <i
                                            class="icon-user"></i> </span> </label> <label class="block clearfix">
										<span class="block input-icon input-icon-right"> <input
                                                id="password" name="password" type="password"
                                                class="form-control" placeholder="password"/> <i
                                                class="icon-lock"></i> </span> </label>

                                    <div class="space"></div>

                                    <div class="clearfix">


                                        <button type="submit"
                                                class="width-35 pull-right btn btn-sm btn-primary">
                                            <i class="icon-key"></i> 登录
                                        </button>
                                    </div>

                                    <div class="space-4"></div>
                                </fieldset>
                            </form>


                        </div>
                        <!-- /widget-main -->

                    </div>
                    <!-- /widget-body -->
                </div>
                <!-- /login-box -->

            </div>
            <!-- /position-relative -->
        </div>
    </div>
    <!-- /.col -->
</div>
<!-- /.row -->
<script type="text/javascript">
    $("#main-content").css("marginLeft", 0);
</script>