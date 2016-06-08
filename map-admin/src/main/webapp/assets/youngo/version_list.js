define("version_list", function (require, exports, module) {
    require("timepicker");

    jQuery(function ($) {
        var grid_selector = "#ynf_list";
        pager_selector = "#ynf_list_pager",
            ynf_table_nav = "#ynf_table_nav",
            $ynf_list = $(grid_selector),
            $ynf_list_pager = $(pager_selector),
            $ynf_table_nav = $(ynf_table_nav),
            table = YNF_CONFIG.table,
            _ynf_c = YNF_CONFIG;

        $ynf_list.jqGrid({
            url: table.url,
            editurl: table.editurl, // nothing is saved
            jsonReader: {
                root: "data",
                page: "currentPage",
                total: "totalPages",
                records: "totalRecords",
                repeatitems: false,
                id: "id"
            },
            datatype: "json",
            height: 500,
            caption: "版本列表",
            autowidth: true,
            colNames: ['ID', '版本号', '版本名称', "下载地址", "是否强制升级", "升级提示语", "更新时间"],
            colModel: [{
                name: 'id',
                index: 'id',
                width: 50,
                sorttype: "int"
            }, {
                name: 'versionNumber',
                index: 'versionNumber',
                width: 50,
                editable: false
            }, {
                name: 'versionName',
                index: 'versionName',
                width: 50,
                sortable: false,
                editable: false
            }, {
                name: 'url',
                index: 'url',
                width: 250,
                sortable: false,
                editable: false
                //formatter:send_status_formatter
            }, {
                name: 'forcibly',
                index: 'forcibly',
                width: 80,
                sortable: false,
                editable: false,
                formatter: forcibly_formatter
            }, {
                name: 'description',
                index: 'description',
                width: 100,
                sortable: true
            }, {
                name: 'createtime',
                index: 'createtime',
                width: 100,
                sortable: true,
                formatter: createtime_formatter
            }
            ],

            viewrecords: true,
            rowNum: 20,
            rowList: [10, 20, 30, 50],
            pager: pager_selector,
            altRows: true,
            toppager: true,

            multiselect: true,
            multiboxonly: true,
            gridComplete: function (json) {
            },
            loadError: function (json) {
            },
            loadBeforeSend: function (json) {
            },
            beforeProcessing: function (json) { //渲染数据前
            },
            onHeaderClick: function (json) {
            },

            loadComplete: function (json) {
                var table = this;
                setTimeout(function () {

                    updateActionIcons(table);
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
            }
        });

        function forcibly_formatter(cellvalue, options, rowdata) {

            if (rowdata.forcibly == "1") {
                return '<span class="ynf-f-s c-green ">' + '是' + '</span>';
            } else {
                return '<span class="ynf-f-s c-blue ">' + '否' + '</span>';
            }
        }

        function createtime_formatter(cellvalue, options, rowdata) {
            if (rowdata.createtime != null) {
                return rowdata.createtime;
            } else {
                return "无数据";
            }
        }

        // navButtons
        $ynf_list.jqGrid('navGrid', pager_selector, { // navbar options
            edit: false,
            editicon: 'icon-pencil blue',
            add: false,
            addicon: 'icon-plus-sign purple',
            del: false,
            delicon: 'icon-trash red',
            search: false,
            searchicon: 'icon-search orange',
            refresh: true,
            refreshicon: 'icon-refresh green',
            view: true,
            viewicon: 'icon-zoom-in grey',
        });


        // unlike navButtons icons, action icons in rows seem to be
        // hard-coded
        // you can change them like this in here if you want
        function updateActionIcons(table) {

            var replacement = {
                'ui-icon-pencil': 'icon-pencil blue',
                'ui-icon-trash': 'icon-trash red',
                'ui-icon-disk': 'icon-ok green',
                'ui-icon-cancel': 'icon-remove red'
            };
            $(table).find('.ui-pg-div span.ui-icon').each(function () {
                var icon = $(this);
                var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
                if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
            })

        }

        // replace icons with FontAwesome icons like above
        function updatePagerIcons(table) {
            var replacement = {
                'ui-icon-seek-first': 'icon-double-angle-left bigger-140',
                'ui-icon-seek-prev': 'icon-angle-left bigger-140',
                'ui-icon-seek-next': 'icon-angle-right bigger-140',
                'ui-icon-seek-end': 'icon-double-angle-right bigger-140'
            };
            $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
                var icon = $(this);
                var $class = $.trim(icon.attr('class').replace('ui-icon', ''));

                if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
            })
        }

        function enableTooltips(table) {
            $('.navtable .ui-pg-button').tooltip({
                container: 'body'
            });
            $(table).find('.ui-pg-div').tooltip({
                container: 'body'
            });
        }
    });

    // 配置表导航过滤搜索
    $.ynf.init_single_filter({
        empty_datepicker: true
    });

});