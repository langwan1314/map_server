<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div class="breadcrumbs" id="breadcrumbs">
    <script type="text/javascript">
        try {
            ace.settings.check('breadcrumbs', 'fixed')
        } catch (e) {
        }
    </script>

    <ul class="breadcrumb">
        <!-- <li><i class="icon-home home-icon"></i> <a href="./list">消息处理</a>
        </li> -->
        <li><a href="javascript:void(0)">举报审核</a>
        </li>

    </ul>
    <!-- .breadcrumb -->

</div>

<div>

    <table align="center">
        <tr>
            <td>
                <div id="chat_status_btns" class="clearfix status">
                    <div class="col-xs-6 btn " data-status="1" data-role="./urlistgrid">
                        聊天消息<br>
                    </div>
                    <div class="col-xs-6 btn " data-status="2" data-role="./mylistgrid">
                        全部消息<br>
                    </div>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="chat-window">
                    <div class="chat-messages">
                        <ol class="chat-messages-list">

                        </ol>
                    </div>
                </div>
            </td>
        </tr>
    </table>

</div>


<script src="../resources/assets/js/emoji-lib/emoji-list-with-image.js"></script>
<script src="../resources/assets/js/emoji-lib/punycode.js"></script>
<script src="../resources/assets/js/emoji-lib/punycode.min.js"></script>
<script src="../resources/assets/js/emoji-lib/emoji.js"></script>
<script>
    seajs.use("prosecute_audit");

    var session_id = $.ynf.parse_url(window.location.href).params.id;
    var sendImage = "./sendImage?session_id=" + session_id;

</script>





