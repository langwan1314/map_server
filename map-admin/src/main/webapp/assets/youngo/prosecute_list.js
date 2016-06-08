define("prosecute_list", function(require, exports, module) {

    jQuery(function($) {
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
            height: 400,
            caption: "商品分类列表",
            autowidth: true,
            colNames: ['ID', "被举报用户",'举报用户', '举报时间', "被举报次数", "查看被举报用户消息", "操作"],
            colModel: [ {
                name: 'id',
                index: 'id',
                width: 50,
                sorttype: "int"
            }, {
                name: 'accusedName',
                index: 'accusedName',
                width: 80,
                sortable: false,
                editable: false
            }, {
                name: 'prosecutorName',
                index: 'prosecutorName',
                width: 80,
                editable: false
            }, {
                name: 'createtime',
                index: 'createtime',
                width: 80,
                sortable: true,
                editable: false,
                formatter:createtime_formatter
            }, {
                name: 'accusedTimes',
                index: 'accusedTimes',
                width: 80,
                sortable: false,
                editable: false
            }, {
                name: 'view',
                index: 'view',
                width: 100,
                sortable: false,
                formatter : view_formatter
            }, {
                name: 'oper',
                index: 'status',
                width: 100,
                sortable: false,
                formatter: oper_formatter
            }],
            viewrecords: true,
            rowNum: 200,
            rowList: [200],
            pager: pager_selector,
            altRows: true,

            multiselect: true,
            multiboxonly: true,
            gridComplete: function(json) {},
            loadError: function(json) {},
            loadBeforeSend: function(json) {},
            beforeProcessing: function(json) { // 渲染数据前
            },
            onHeaderClick: function(json) {},

            loadComplete: function(json) {
                var table = this;
                setTimeout(function() {
                    updateActionIcons(table);
                    updatePagerIcons(table);
                    enableTooltips(table);
                }, 0);
            }
        });

        function view_formatter(cellvalue, options, rowdata) {
            var oper_html_arr = [];
            var style = 'style="font-size:16px;cursor:pointer;padding:4px;display:inline-block;" ';
            var edit = ['<a  ',
                ' target="_blank" href="./audit?id=' + rowdata.id + '" ',
                style,
                ' class="c-green"   title="查看">查看</a>'
            ].join("");
            oper_html_arr.push(edit);
            return oper_html_arr.join("");
        }

        function createtime_formatter(cellvalue, options, rowdata) {
            if (rowdata.createtime != null) {
                return rowdata.createtime;
            } else {
                return "无数据";
            }
        }
        function oper_formatter(cellvalue, options, rowdata) {
            if(rowdata.status==0)
            {
                var oper_html_arr = [];
                var style = 'style="font-size:16px;cursor:pointer;padding:4px;display:inline-block;" ';

                var forb = ['<a ',
                    'href="javascript:void(0)" ',
                    style,
                    'data-id="', rowdata.id, '" ',
                    'data-status="', 3, '" ',
                    'data-ajax-data = "data-id,data-status"',
                    ' data-header_text="确定封禁"',
                    ' data-need_modal="true"',
                    ' class="c-red oper" data-ajax-url="./update" title="封禁">封禁 </a>'
                ].join("");

                var waring = ['<a ',
                    'href="javascript:void(0)" ',
                    style,
                    'data-id="', rowdata.id, '" ',
                    'data-status="', 2, '" ',
                    'data-ajax-data = "data-id,data-status"',
                    ' data-header_text="确定警告"',
                    ' data-need_modal="true"',
                    ' class="c-red oper" data-ajax-url="./update" title="警告">警告 </a>'
                ].join("");

                var nothing = ['<a ',
                    'href="javascript:void(0)" ',
                    style,
                    'data-id="', rowdata.id, '" ',
                    'data-status="', 1, '" ',
                    'data-ajax-data = "data-id,data-status"',
                    ' data-header_text="确定不处理"',
                    ' data-need_modal="true"',
                    ' class="c-red oper" data-ajax-url="./update" title="不处理">不处理 </a>'
                ].join("");

                oper_html_arr.push(forb);
                oper_html_arr.push(waring);
                oper_html_arr.push(nothing);

                return oper_html_arr.join("");
            }else if(rowdata.status ==1)
            {
                return "不做处理";
            }else if(rowdata.status==2)
            {
                return "已警告";
            }else if(rowdata.status==3)
            {
                return "已封禁";
            }
        }


        // you can change them like this in here if you want
        function updateActionIcons(table) {

            var replacement = {
                'ui-icon-pencil': 'icon-pencil blue',
                'ui-icon-trash': 'icon-trash red',
                'ui-icon-disk': 'icon-ok green',
                'ui-icon-cancel': 'icon-remove red'
            };
            $(table).find('.ui-pg-div span.ui-icon').each(function() {
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
            $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function() {
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


    $.ynf.init_oper_page_list({
        $el: $ynf_list,
        success: function(json) {
            if (json && json.code == 200) {
                $ynf_list.trigger("reloadGrid");
            }
        }
    });

});