var versionAdd = versionAdd || {};
define("version_update", function (require, exports, module) {

    //require("dropzone");

    var bannerImageUrl = "";

    var $ = window.jQuery;


    $("#submit").on("click", function (ev) {

        if (!validate()) {
            return;
        }

        var formData = getFormData();
        $.ynf.ajax({
            url: "update",
            type: "post",
            contentType: "application/json",
            data: JSON.stringify(formData),
            dataType: "json",
            success: function (json) {
                if (json.code == 200) {
                    $.ynf.small_modal({
                        show: true,
                        body_cont: "提交成功",
                        need_sure: true,
                        sure_fn: function () {
                            window.location.href = "list";
                        }
                    });
                }
            }

        })

    });

    /**
     * 获取form表单数据
     */
    function getFormData() {
        var data = {};
        data.forcibly = $("input[name=forcibly]:checked").val();

        data.versionName = $.trim($("#versionName").val());
        data.versionNumber = $.trim($("#versionNumber").val());
        data.url = $.trim($("#url").val());
        data.clientType = $("#clientType option:selected").val();
        data.description = $.trim($("#description").val());

        return data;

    }

    /**
     * 提交时进行校验
     */
    function validate() {
        var formInfo = getFormData();

        if ("" == formInfo.forcibly || formInfo.forcibly == null) {

            $.ynf.small_modal({
                body_cont: "是否强制升级不能为空",
                show: true
            });
            return false;
        }
        if ("" == formInfo.versionName || formInfo.versionName == null) {

            $.ynf.small_modal({
                body_cont: "版本名称不能为空",
                show: true
            });
            return false;
        }
        if ("" == formInfo.versionNumber || formInfo.versionNumber == null) {

            $.ynf.small_modal({
                body_cont: "版本号不能为空",
                show: true
            });
            return false;
        }
        if ("" == formInfo.url || formInfo.url == null) {

            $.ynf.small_modal({
                body_cont: "下载链接不能为空",
                show: true
            });
            return false;
        }
        if ("" == formInfo.clientType || formInfo.clientType == null) {

            $.ynf.small_modal({
                body_cont: "客户端类型不能为空",
                show: true
            });
            return false;
        }
        if ("" == formInfo.description || formInfo.description == null) {

            $.ynf.small_modal({
                body_cont: "升级提示语不能为空",
                show: true
            });
            return false;
        }

        return true;
    }
});

versionAdd.clientType = function () {
    if ($("#androidversion").is(":hidden")) {
        $("#iosversion").hide();
        $("#androidversion").show();
    } else {
        $("#androidversion").hide();
        $("#iosversion").show();
    }
};