(function($, win) {

	var ynf = {};
	var _ynf_c = YNF_CONFIG;

	ynf.init_skin = function(){ //初始化皮肤
        if(window.location.origin.indexOf("www.bellooo.com") == -1 ){ //测试环境
			document.title= document.title + "--测试环境" ;
			$("#header .navbar-brand").html( $(".navbar-brand").html()+"-测试环境" );
        }else{
           document.title= document.title + "--正式环境" ;
           $("#header .navbar-brand").html( $(".navbar-brand").html()+"-正式环境" );
        }
	};


	ynf.ajax = function(args) {
		if (!args)
			return false;

		if (args.contentType && args.contentType === "application/json") {
			if (typeof args.data != "string")
				args.data = JSON.stringify(args.data);
		}

		var old_success_fn = function(json) {
		};
		var success_fn = function(json) {
		};

		if (args.success) {
			old_success_fn = args.success;
			var success_fn = function(json) {

				if (json.code && json.code == 401) {
					window.location.href = json.data.url;
					return false;
				}
				if (json.code && json.code != 200) {
					console.log(json);
					ynf.small_modal({
						show : true,
						body_cont : "<span class='c-red'>" + json.msg
								+ "</span>"
					});
				}
			};
			delete args.success;
		}
		console.log(args, "ynf_ajax", "-------");
		var ajax = $.ajax(args);

		ajax.done(function(json) {
			old_success_fn(json);
			success_fn(json);
		});


     if(!args.not_tip_error){  //args.not_tip_error =true 不提示异常 一般做定时请求
     	ajax.error(function(xhr) {
			console.log("error", xhr);
			ynf.small_modal({
				show : true,
				body_cont : "<span class='c-red'>" + "请求发生异常" + "</span>"
			});

		});
     }
		
		return ynf;

	};
	ynf.init_menu_active = function() {
		if (window.YNF_CONFIG_MENU == undefined)
			return false;
		console.log("window.YNF_CONFIG_MENU", window.YNF_CONFIG_MENU);
		var cur_tag = YNF_CONFIG_MENU;
		var cur_li = $("#ynf_nav li[data-active-flag=" + cur_tag + "]");
		cur_li.addClass('active');
		var cur_ul = cur_li.parent();
		if (cur_ul.hasClass('submenu')) {
			cur_ul.parent().addClass('active open');
		}
		if (cur_ul.parent().parent().hasClass('submenu')) {
			cur_ul.parent().parent().parent().addClass('active open');
		}
	};
      // 表格加载完成后的初始化图标
	ynf.init_table_icon= function(table){
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

	    updateActionIcons(table);
	    updatePagerIcons(table);
	    enableTooltips(table);

	};

	var modal_html = [
			'<div class="modal fade" id="ynf_change_table_modal" tabindex="-1"',
			'role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">',
			'<div class="modal-dialog">',
			'<div class="modal-content">',
			'<div class="modal-header">',
			'<button type="button" class="close" data-dismiss="modal">',
			'<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>',
			'</button>',
			'<h4 class="modal-title" id="myModalLabel">确认修改？</h4>', '</div>',
			'<div class="modal-body"></div>', '<div class="modal-footer">',
			'<span data-y-role="modal_ajax_tip" class="c-green"',
			'style="display:none"></span>',
			'<button type="button" data-y-role="modal_cancel_btn"',
			'class="btn btn-default" data-dismiss="modal">取消</button>',
			'<button type="button" data-y-role="modal_sure_btn"',
			'class="btn btn-primary">保存</button>', '</div>', '</div>',
			'</div>', '</div>' ].join("");

	var grid_modal_html = [
			'<div class="modal fade" id="ynf_grid_modal" tabindex="-1"',
			'role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">',
			'<div class="modal-dialog">',
			'<div class="modal-content">',
			'<div class="modal-header">',
			'<button type="button" class="close" data-dismiss="modal">',
			'<span aria-hidden="true">&times;</span><span class="sr-only">关闭</span>',
			'</button>',
			'<h4 class="modal-title" id="myModalLabel">列表</h4>', '</div>',
			'<div class="modal-body">',
            '<div class="ynf-modal-grid-nav clearfix" style="padding-bottom:10px; "></div>',
            '<table class="ynf-modal-grid-list" id="ynf_modal_grid_list"></table>',
            '<div id="ynf_modal_list_pager"></div>',
			'</div>',
		    '<div class="modal-footer">',
			'<span data-y-role="modal_ajax_tip" class="c-green"',
			'style="display:none"></span>',
			'<button type="button" data-y-role="modal_cancel_btn"',
			'class="btn btn-default" data-dismiss="modal">取消</button>',
			'<button type="button" data-y-role="modal_sure_btn"',
			'class="btn btn-primary">保存</button>', '</div>', '</div>',
			'</div>', '</div>' ].join("");


	var small_modal_html = [
			'<div id="ynf_small_tip_modal" style="position: fixed; top:20%; margin-left: -200px; width:400px;" class="modal-dialog modal-sm">',
			' <div class="modal-content">',
			'<div class="modal-header">',
			' <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>',
			'<h4 class="modal-title" id="mySmallModalLabel">提示您</h4>',
			'</div>', '<div class="modal-body">', '</div>', '</div> ', '</div>' ]
			.join("");

	var small_sure_modal_html = [
			'<div id="ynf_small_tip_modal" style="position: fixed; top:20%; margin-left: -200px; width:400px;" class="modal-dialog modal-sm">',
			' <div class="modal-content">',
			'<div class="modal-header">',
			' <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>',
			'<h4 class="modal-title" id="mySmallModalLabel">提示您</h4>',
			'</div>', '<div class="modal-body">', '</div>',
			'<div class="modal-footer">',
			'<button type="button" data-y-role="modal_sure_btn"',
			'class="btn btn-primary">确定</button>', '</div>', '</div> ',
			'</div>' ].join("");

	ynf.has_modal_html = false;
	ynf.has_grid_modal_html = false;
	ynf.has_small_modal_html = false;
	ynf.init_modal_click = false;

	function _config_small_modal(args) { // 做提示框用
		/*
		 * args:{ "header_text"：标题 "body_cont"："恩恩", [ need_sure:true, sure_fn:
		 * function(){} ] } }
		 */

		if (args.need_sure && args.need_sure === true) {

		}

		$("#ynf_small_tip_modal").remove();
		args.need_sure === true ? $(document.body)
				.append(small_sure_modal_html) : $(document.body).append(
				small_modal_html);

		var modal = $("#ynf_small_tip_modal"), $title = modal
				.find(".modal-title");
		$body = modal.find(".modal-body");
		if (!args.body_cont)
			args.body_cont = "";
		if (args.header_text)
			$title.text(args.header_text);
		if (args.body_cont)
			$body.html(args.body_cont);

		if (args.show) {
			modal.modal("show");
		} else {
			modal.modal("hide");
			

		}

		if (args.need_sure && args.need_sure === true) {
			modal.find("button.close").hide();
			var $footer = modal.find(".modal-footer");
			var $sure_btn = $footer.find("button[data-y-role=modal_sure_btn]");
			$sure_btn.on("click", function() {
				if ($.isFunction(args.sure_fn)) {
					args.sure_fn();
					modal.modal("hide");
			        

				}

			});

		}

	}

	ynf.small_modal = function(args) {
		_config_small_modal(args);
	};

	ynf.set_table_width = function() {
		$(".ui-jqgrid-htable").css("width", $(".ui-jqgrid-btable").width() + 2);
	};



	ynf._config_grid_modal=function(args) {


		/*
		 * args:{ "header_text"：标题 ,
		        grid_config:{}, 
		        grid_search:{
	              single_search:{
		            	target: $modal_grid_nav ,
		                special: {
		                    user_name: "cn",
		                    order_sn: "cn"
		                }
		            },
	              single_filter:{ // 下拉过滤
	
	              },
	              time_filter:{//时间搜索
                     
	               },

		        },
		        ajax:{ url:"", type : "",
		 * data:{ } } }
		 */

		if (ynf.has_grid_modal_html == false) {
			$(document.body).append(grid_modal_html);
			ynf.has_grid_modal_html = true;
		}
 

		if(!args.grid_config || $.isEmptyObject(args.grid_config) ){
			console.log("----不存在表格初始化参数");
            return false ; 
		}

		var modal = $("#ynf_grid_modal"), 
		$title = modal.find(".modal-title");
		$body = modal.find(".modal-body");
		$footer = modal.find(".modal-footer");
		 
		$modal_grid_nav =  modal.find(".ynf-modal-grid-nav");
		$modal_grid_list = modal.find(".ynf-modal-grid-list");
		$sure_btn = $footer.find("button[data-y-role=modal_sure_btn]");
		$cancel_btn = $footer.find("button[data-y-role=modal_cancel_btn]");
		$modal_tip = $footer.find("span[data-y-role=modal_ajax_tip]");


		function init_modal_grid_search(){
			var obj =  {
			  target:$modal_grid_nav ,
			  list: $modal_grid_list,
			  nav: $modal_grid_nav,
			} ; 
		    $.extend(args.single_filter , obj );
		    $.extend(args.single_search , obj);
            // 配置表导航过滤搜索  
           if( args.single_filter)  ynf.init_single_filter(args.single_filter);
           if( args.single_search)  ynf.init_single_search(args.single_search);

		}

		if(args.grid_config.grid_search){
			$modal_grid_nav.html(args.grid_config.grid_search.html);
            init_modal_grid_search();
		}

	 
		if (args.header_text)
			$title.text(args.header_text);
	 
		if (args.cancel_btn_text)
			$cancel_btn.text(args.cancel_btn_text);
		if (args.sure_btn_text)
			$sure_btn.text(args.sure_btn_text);
		if (args.header_text)
			$title.text( args.header_text );
		if (args.ajax && !args.ajax.type)
			args.ajax.type = 'post';
		if (args.ajax && args.ajax.url && args.ajax.data)
			$sure_btn.data("ajax", args.ajax);
		if (args.cancel_fn) {
			$cancel_btn.data("cancel_fn", args.cancel_fn);
		}
		 
 
		if (args.show) {
			modal.modal("show");
		} else {
			modal.modal("hide");

		}

		setTimeout(function(){
			$modal_grid_list.jqGrid(args.grid_config);

          //navButtons
           $modal_grid_list.jqGrid('navGrid', "#ynf_modal_list_pager", { //navbar options
            edit: false,
            add: false,
            del: false,
            refresh: true,
            search: false,
            refreshicon: 'icon-refresh green',
            view: false,
            viewicon: 'icon-zoom-in grey',
        }, {
            recreateForm: true,
            beforeShowForm: function(e) {
                var form = $(e[0]);
                form.closest('.ui-jqdialog').find('.ui-jqdialog-title').wrap('<div class="widget-header" />')
            }
        });
		}, 500);

	};


	ynf._config_modal=function(args) {
		/*
		 * args:{ "header_text"：标题 "body_cont"："恩恩", ajax:{ url:"", type : "",
		 * data:{ } } }
		 */
		if (ynf.has_modal_html == false) {
			$(document.body).append(modal_html);
			ynf.has_modal_html = true;
		}

		var modal = $("#ynf_change_table_modal"), 
		$title = modal.find(".modal-title");
		$body = modal.find(".modal-body");
		$footer = modal.find(".modal-footer");
		$sure_btn = $footer.find("button[data-y-role=modal_sure_btn]");
		$cancel_btn = $footer.find("button[data-y-role=modal_cancel_btn]");
		$modal_tip = $footer.find("span[data-y-role=modal_ajax_tip]");
		$body.empty();
		if (!args.body_cont)
			args.body_cont = "";
		if (args.header_text)
			$title.text(args.header_text);
		if (args.body_cont)
			$body.html(args.body_cont);
		if (args.cancel_btn_text)
			$cancel_btn.text(args.cancel_btn_text);
		if (args.sure_btn_text)
			$sure_btn.text(args.sure_btn_text);
		if (args.header_text)
			$title.text(args.header_text);
		if (args.ajax && !args.ajax.type)
			args.ajax.type = 'post';
		if (args.ajax && args.ajax.url && args.ajax.data)
			$sure_btn.data("ajax", args.ajax);
		if (args.cancel_fn) {
			$cancel_btn.data("cancel_fn", args.cancel_fn);
		}

		if($.isFunction(args.bind_fn) ){
			args.bind_fn(args, modal);
		}

		if (args.modal_default_data) { // 在body_cont 里面设置默认数据
			$.each(args.modal_default_data,
							function(i, v) {
								if ($body.find("div[data-val=" + i + "]").text(
										v).length > 0) {
									$body.find("div[data-val=" + i + "]").attr(
											"old-val", v);
								}
								if ($body.find("input[data-val=" + i + "]")
										.val(v).length > 0) {
									$body.find("input[data-val=" + i + "]")
											.attr("old-val", v);
								}
								if ($body.find("textarea[data-val=" + i + "]")
										.val(v).length > 0) {
									$body.find("textarea[data-val=" + i + "]")
											.attr("old-val", v);
								}
							});
		}

		if (args.show_change_fn) { // 显示变化 没有变化不提交
			if ($.isFunction(ynf.modal_fn[args.show_change_fn])) {
				var result = false;
				result = ynf.modal_fn[args.show_change_fn](modal, args);
				if (result === false)
					return false;
			}
		}

		if (args.show) {
			modal.modal("show");
		} else {
			modal.modal("hide");
			

		}
	};

	ynf.modal_fn = {
		refund_input : function(modal, data) {
			var modal_body = modal.find(".modal-body");
			var old_amount = $.trim(modal_body.find(
					"input[data-y-role=ynf_old_money]").val());
			var amount = $.trim(modal_body.find("input[data-y-role=ynf_money]")
					.val());
			var reply = $.trim(modal_body.find(
					"textarea[data-y-role=ynf_reason]").val());
			var modal_input_tip = modal_body
					.find("div[data-y-role=modal_input_tip]");
			if (amount == "") {
				modal_input_tip.text("输入金额不能为空");
				return false;
			}
			if (parseFloat(amount) > parseFloat(old_amount)) {
				modal_input_tip.text("退款金额大于订单金额！！！");
				return false;
			}
			if (reply == "") {
				modal_input_tip.text("请输入退款理由");
				return false;
			}
			$.extend(data, {
				amount : amount,
				reply : reply
			});
			return true;
		},

		refuse_reason : function(modal, data) {

			var modal_body = modal.find(".modal-body");
			var reply = $.trim(modal_body.find(
					"textarea[data-y-role=ynf_reason]").val());
			var modal_input_tip = modal_body
					.find("div[data-y-role=modal_input_tip]");
			if (reply == "") {
				modal_input_tip.text("请输入退款理由");
				return false;
			}
			$.extend(data, {
				reply : reply
			});
			return true;
		},

		order_change_info : function(modal, data) {
			console.log("order_change_info--data", data);
			var modal_body = modal.find(".modal-body");
			if ($.isEmptyObject(data.prev_data._new)) {
				modal_body.html('<span class="c-red"> 无更新信息 </span>');
				return false;
			}
			var html_arr = [];
			var add_change_info = function(key) {
				var item_arr = [
						data.prev_data.key_name[key],
						':由<span class="c-red">',
						data.prev_data._old[key] == "" ? "空"
								: data.prev_data._old[key], ,
						'</span>更改为:<span class="c-green">',
						data.prev_data._new[key], '</span> <br> <br>' ];
				html_arr.push(item_arr.join(""));
			};
			$.each(data.prev_data._new, function(i, v) {
				add_change_info(i);
			});

			modal_body.html(html_arr.join(""));
			return true;
		}

	};

	function _ynf_init_modal(args, modal) {
		ynf._config_modal(args);
		var modal = modal || $("#ynf_change_table_modal");
		

		if (ynf.init_modal_click == true) {
			return false;
		} else {
			ynf.init_modal_click = true;
		}
		
		modal.on("click", "button[data-y-role=modal_sure_btn]", function(ev) {
			var _this = $(this);
			var ajax_agrs = _this.data("ajax") || {};

			if (ajax_agrs.url) {

				if (args.extend_data) { // 数据校验
					if ($.isFunction(ynf.modal_fn[args.extend_data])) {
						var result = false;

						result = ynf.modal_fn[args.extend_data]( modal , 	ajax_agrs.data    );
						if (result === false) return false;
					}
				}

				

				$.ynf.ajax({
					url : ajax_agrs.url,
					type : ajax_agrs.type || "post",
					beforeSend: ajax_agrs.beforeSend || function(){ }, 
					dataType : "json",
					contentType : "application/json",
					data : JSON.stringify(ajax_agrs.data),
					success : function(json) {
						if (args.success && $.isFunction(args.success)) {
							args.success(json)
						}
						_this.data("ajax", {}); // 清空ajax数据
						var $modal_tip = modal
								.find("span[data-y-role=modal_ajax_tip]");
						if (json.code && json.code != 200)
							{
							//返回异常时 会有业务exception抛出   此时将所有modal 隐藏   add by atao 2015-03-07
							modal.modal("hide");
							$(".modal-backdrop ").hide();
							return false;
							}
							
						$modal_tip.show().text(json.msg || "提交成功");
						if (args.table && args.table.length)
							args.table.trigger("reloadGrid");
						setTimeout(function() {
							modal.modal("hide");
							$modal_tip.hide().text("");
						}, 400);
					},
					error : function(error) {
						
						seajs.log(error);
						_this.data("ajax", {}); // 清空ajax数据
						var $modal_tip = modal
								.find("span[data-y-role=modal_ajax_tip]");
					}
				});
			} else {
				ynf.small_modal({
					body_cont : _ynf_c.tip.no_ajax_data,
					show : true
				});
			}
		});

		modal.on("click", "button[data-y-role=modal_cancel_btn]", function() {
			var _this = $(this);
			var cancel_fn = _this.data("cancel_fn") || function() {
				console.log("没有cancel_fn");
			};
			cancel_fn();

		});
	}

	ynf.modal = function(args) { // modal
		console.log("args-2",args);
		_ynf_init_modal(args);

	};

	ynf.ali = function(html, target) {
		target.html(html);
	};

	// ynf.deal_success_upload()

	ynf.dropzone = function(args) { // 文件上传
		/*
		 * { target: ".desc-img", config: {}, fn:{ addedfile: function(file){ },
		 * success: function(json){ }, sending: function(file){ }, removefile:
		 * function(file){ } }
		 *  }
		 */
		args.fn = args.fn || {};
		var $dom = $(args.target);
		if ($dom.length == 0)
			return false;
		var config = {
			paramName : "file", // The name that will be used to transfer the
								// file
			maxFilesize : 3, // MB
			autoProcessQueue : true,
			addRemoveLinks : true,
			dictDefaultMessage : '<span class="bigger-150 bolder"><i class="icon-caret-right red"></i> Drop files</span> to upload \
	              <span class="smaller-80 grey">(or click)</span> <br /> \
	              <i class="upload-icon icon-cloud-upload blue icon-3x"></i>',
			dictResponseError : '文件上传错误!',
			init : function() {
				if ($.isFunction(args.fn.addedfile)) {
					this.on("addedfile", function(file) {
						args.fn.addedfile(file);
					});
				}
				if ($.isFunction(args.fn.success)) {
					this.on("success", function(json) {
						args.fn.success(json);
					});
				}

				if ($.isFunction(args.fn.error)) {
					this.on("error", function(file) {
						args.fn.error(file);
					});
				}

				if ($.isFunction(args.fn.sending)) {
					this.on("sending", function(file) {
						args.fn.sending(file);
					});
				}
				if ($.isFunction(args.fn.removedfile)) {
					this.on("removedfile", function(file) {
						args.fn.removedfile(file);
					});
				}

			},
			previewTemplate : [
					'<div class=\"dz-preview dz-file-preview\">',
					' <div class=\"dz-details\">',
					' <div class=\"dz-filename\"><span data-dz-name></span></div>',
					' <div class=\"dz-size\" data-dz-size></div>',
					' <img data-dz-thumbnail />',
					' </div>',
					' <div class=\"progress progress-small progress-striped active\">',
					' <div class=\"progress-bar progress-bar-success\" data-dz-uploadprogress></div></div>',
					' <div class=\"dz-success-mark\"><span></span></div>',
					' <div class=\"dz-error-mark\"><span></span></div>',
					' <div class=\"dz-error-message\"><span data-dz-errormessage></span></div>',
					' </div>' ].join("")
		};

		if (!$.isEmptyObject(args.config)) {
			$.extend(config, args.config);
		}

		$dom.dropzone(config);
	};

	 

	ynf.get_rules = function(args) { // 得到过滤规则
		    args = args || {};
		var $ynf_table_nav = args.nav  || $("#ynf_table_nav");

		var rules = "", selects = $ynf_table_nav
				.find("select[data-y-role=search_filter]"), l = selects.length;

		if (l === 1) { // 只有一个select
			if ($.trim(selects.val()) === "")
				return "";
			rules += '{"field":"' + selects.attr("sidx")
					+ '" , "op":"eq" , "data":"' + $.trim(selects.val()) + '"}';
			return rules;
		}

		$.each(selects, function(i, v) {

			var val = $.trim($(v).val());
			if (val != "" && i < l - 1) {
				if (val === "")
					return "";
				rules += '{"field":"' + $(v).attr("sidx")
						+ '" , "op":"eq" , "data":"' + val + '"}';
			}
			if (val != "" && i == l - 1) {
				if (val === "")
					return "";
				rules == "" ? rules += '{"field":"' + $(v).attr("sidx")
						+ '" , "op":"eq" , "data":"' + val + '"}'
						: rules += ',{"field":"' + $(v).attr("sidx")
								+ '" , "op":"eq" , "data":"' + val + '"}';
			}
		});

		return rules;
	};

	ynf.oper_ynf_list_default_args = {

		oper_refuse : {
			header_text : "拒绝退款",
			cancel_btn_text : "取消",
			sure_btn_text : "确定",
			body_cont : [
					'<span class="ynf-lable-five" >理由：</span>',
					'<textarea data-y-role="ynf_reason" rows="3" cols="40"   data-val="reply" ></textarea><br/>',
					'<div data-y-role="modal_input_tip" class="c-red"><div>' ]
					.join(""),
			extend_data : "refuse_reason",
		},

		oper_drawback : {
			header_text : "确定退款",
			cancel_btn_text : "取消",
			sure_btn_text : "确定退款",
			body_cont : [
					'<span class="ynf-lable-five">最大可退金额：</span>',
					'<input type="text" data-y-role="ynf_old_money"  data-val="old_amount" readonly value="" /><br/><br/>',
					'<span class="ynf-lable-five" >退款金额：</span>',
					'<input type="text" data-y-role="ynf_money"  data-val="amount" readonly value="" /><br/><br/>',
					'<span class="ynf-lable-five" >理由：</span>',
					'<textarea data-y-role="ynf_reason"  rows="3" cols="40"   data-val="reply"  readonly ></textarea><br/>',
					'<div data-y-role="modal_input_tip" class="c-red"><div>' ]
					.join("")
		},
		oper_agree : {
			header_text : "同意退款",
			cancel_btn_text : "取消",
			sure_btn_text : "同意退款",
			body_cont : [
					'<span class="ynf-lable-five">用户：</span>',
					'<input type="text" data-y-role="ynf_user" data-val="user" value=""  readonly /><br/><br/>',
					'<span class="ynf-lable-five">最大可退金额：</span>',
					'<input type="number"  data-y-role="ynf_old_money"  data-val="old_amount" readonly value="" /><br/><br/>',
					'<span class="ynf-lable-five" >退款金额：</span>',
					'<input type="text" onkeyup="$.ynf.input_valid(this,',
					"'number'",
					")",
					'"  data-y-role="ynf_money" data-val="amount" value="" /><br/><br/>',
					'<span class="ynf-lable-five" >理由：</span>',
					'<textarea data-y-role="ynf_reason"  rows="3" cols="40" data-val="reply" ></textarea><br/>',
					'<div data-y-role="modal_input_tip" class="c-red"><div>' ]
					.join(""),
			extend_data : "refund_input",
		},
		oper_order_update : { // 订单详情 更新订单 信息
			header_text : "更新订单",
			cancel_btn_text : "取消",
			sure_btn_text : "更新",
		},
		oper_order_close : { // 订单详情 更新订单 信息
			header_text : "取消订单",
			cancel_btn_text : "放弃",
			sure_btn_text : "确定",
		},
		update_order_info : {
			header_text : "更新订单信息",
			cancel_btn_text : "取消",
			sure_btn_text : "更新"
		},

	};

	ynf.init_oper_order = function(args) {
		
		if (!args.$el || args.$el.length == 0) {
			return false
		}
		var $el = args.$el;

		$el.on("click", ".oper", function(ev) {
			seajs.log("執行 ", "click", ".oper");
			var _this = $(ev.target);
			var o = {};
			var modal_default_data = {}; // 用来扩展模态框展示数据
			var role = _this.attr("data-y-role");

			if (role && ynf.oper_ynf_list_default_args[role] ) {
				$.extend(args, ynf.oper_ynf_list_default_args[role]);
			}

			if (_this.attr("data-ajax-data")) {
				var arr = _this.attr("data-ajax-data").split(",");
				$.each(arr, function(i, v) {
					o[v.substr(5)] = _this.attr(v);
				});
			}

			if (_this.attr("data-modal-default-data")) {
				var arr = _this.attr("data-modal-default-data").split(",");
				$.each(arr, function(i, v) {
					modal_default_data[v.substr(5)] = _this.attr(v);
				});
			}

			args.show = true;
			args.ajax = args.ajax || {};
			args.ajax.dataType = args.ajax.dataType || "json";

			if (!$.isEmptyObject(modal_default_data)) {
				args.modal_default_data = modal_default_data;
			}
			args.success = function(json) {
				args.$el.trigger("reloadGrid");
				ynf.ali(json.data, $("#ynf_change_table_modal").find(
						".modal-body"));
			};
			args.ajax.url = _this.attr("data-ajax-url");
			args.ajax.data = o;

			console.log("args-ynf.init_oper_order--", args);
			if (args.ajax)
				ynf.modal(args);
		});
	};

	ynf.init_close_order = function(args) {
		if (!args.$el || args.$el.length == 0) {
			return false
		}
		var $el = args.$el;

		$el.on("click", ".oper", function(ev) {
			seajs.log("執行 ", "click", ".oper");
			var _this = $(ev.target);
			var o = {};
			var modal_default_data = {}; // 用来扩展模态框展示数据
			var role = _this.attr("data-y-role");

			if (role && ynf.oper_ynf_list_default_args[role]) {
				$.extend(args, ynf.oper_ynf_list_default_args[role]);
			}

			if (_this.attr("data-ajax-data")) {
				var arr = _this.attr("data-ajax-data").split(",");
				$.each(arr, function(i, v) {
					o[v.substr(5)] = _this.attr(v);
				});
			}

			if (_this.attr("data-modal-default-data")) {
				var arr = _this.attr("data-modal-default-data").split(",");
				$.each(arr, function(i, v) {
					modal_default_data[v.substr(5)] = _this.attr(v);
				});
			}

			args.show = true;
			args.ajax = args.ajax || {};
			args.ajax.dataType = args.ajax.dataType || "json";

			if (!$.isEmptyObject(modal_default_data)) {
				args.modal_default_data = modal_default_data;
			}
			if (!args.success) {
				args.success = function(json) {
				}
			}

			args.ajax.url = _this.attr("data-ajax-url");
			args.ajax.data = o;

			console.log("ynf.init_close_order", args);
			if (args.ajax)
				ynf.modal(args);
		});
	};

	// 订单详情页试用
	ynf.init_oper_order_info = function(args) {
		if (!args.$el || args.$el.length == 0) {
			return false
		}
		var $el = args.$el;

		$el.on("click", ".oper-order", function(ev) {
			var _this = $(ev.target);
			var o = {};
			var modal_default_data = {}; // 用来扩展模态框展示数据
			var role = _this.attr("data-y-role");

			if (role && ynf.oper_ynf_list_default_args[role]) {
				$.extend(args, ynf.oper_ynf_list_default_args[role]);
			}

			if (args.prev_fn) { // 提前处理函数 用数据替代原来的
				args.prev_data = args.prev_fn();
			}

			if (_this.attr("data-ajax-data")) {
				var arr = _this.attr("data-ajax-data").split(",");
				$.each(arr, function(i, v) {
					o[v.substr(5)] = _this.attr(v);
				});
			}

			args.show = true;
			args.ajax = args.ajax || {};
			args.ajax.dataType = args.ajax.dataType || "json";

			args.success = function(json) {
				ynf.small_modal({
					body_cont : "更新成功！",
					show : true
				});
			};
			args.ajax.data = {};
			args.ajax.contentType = "application/json";
			args.ajax.url = _this.attr("data-ajax-url");
			args.ajax.data.id = args.prev_data.id;
			$.extend(args.ajax.data, args.prev_data._new);

			console.log("ynf.init_oper_order_info--args", args);

			if (args.ajax)
				ynf.modal(args);
		});
	};

	ynf.init_oper_page_list = function(args) { // 编辑商品列表

		if (!args.$el || args.$el.length == 0) {
			return false;
		}
		var $el = args.$el;

		$el.on("click", ".oper", function(ev) {
			seajs.log("執行 ", "click", "init_oper_page_list");
			var _this = $(ev.target);
			var o = {};
			if (_this.attr("data-ajax-data")) {
				var arr = _this.attr("data-ajax-data").split(",");
				$.each(arr, function(i, v) {
					o[v.substr(5)] = _this.attr(v);
				});
			}
			args.ajax = args.ajax || {};
			args.ajax.contentType = "application/json";
			args.ajax.dataType = args.ajax.dataType || "json";
			args.ajax.type = "post";
			args.ajax.data = o;

			args.ajax.success = args.success || function(json) {

			};
			args.ajax.url = _this.attr("data-ajax-url");

			if (_this.attr("data-need_modal") == "true") {
				args.header_text = _this.attr("data-header_text");
				args.show = true;
				if (args.ajax)
					ynf.modal(args);
			} else {
				ynf.ajax(args.ajax);
			}

		});
	};


	ynf.init_quick_oper_list = function(args) { //  直接发送请求 无需modal 确认

		if (!args.$el || args.$el.length == 0) {
			return false;
		}
		var $el = args.$el;
		args.target = args.target || "";
		$el.on("click",  args.target, function(ev) {
		    console.log("quick_oper_list" );
			var _this = $(ev.target);
			var o = {};
			if (_this.attr("data-ajax-data")) {
				var arr = _this.attr("data-ajax-data").split(",");
				$.each(arr, function(i, v) {
					o[v.substr(5)] = _this.attr(v);
				});
			}
			args.ajax = args.ajax || {};
			args.ajax.contentType = args.ajax.contentType || "application/json";
			args.ajax.dataType = args.ajax.dataType || "json";
			args.ajax.type = args.ajax.type|| "post";
			args.ajax.data = o;

			args.ajax.success = args.success || function(json) {

			};
			args.ajax.url = _this.attr("data-ajax-url");
		    ynf.ajax(args.ajax);
		});
	};

	 ynf.parse_url = function (url) {  
			 var a =  document.createElement('a');  
			 a.href = url;  
			 return {  
			 source: url,  
			 protocol: a.protocol.replace(':',''),  
			 host: a.hostname,  
			 port: a.port,  
			 query: a.search,  
			 params: (function(){  
			     var ret = {},  
			         seg = a.search.replace(/^\?/,'').split('&'),  
			         len = seg.length, i = 0, s;  
			     for (;i<len;i++) {  
			         if (!seg[i]) { continue; }  
			         s = seg[i].split('=');  
			         ret[s[0]] = s[1];  
			     }  
			     return ret;  
			 })(),  
			 file: (a.pathname.match(/\/([^\/?#]+)$/i) || [,''])[1],  
			 hash: a.hash.replace('#',''),  
			 path: a.pathname.replace(/^([^\/])/,'/$1'),  
			 relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [,''])[1],  
			 segments: a.pathname.replace(/^\//,'').split('/')  
			 };  
			};


	ynf.init_oper_cicular_list = function(args) { // 编辑轮播列表

		if (!args.$el || args.$el.length == 0) {
			return false;
		}
		var $el = args.$el;
		var target = args.target;

		$el.on("click", target, function(ev) {
			var _this = $(ev.target);
			var o = {};
			if (_this.attr("data-ajax-data")) {
				var arr = _this.attr("data-ajax-data").split(",");
				$.each(arr, function(i, v) {
					o[v.substr(5)] = _this.attr(v);
				});
			}

			if (_this.attr("data-header_text")) {
				args.header_text = _this.attr("data-header_text");
			}

			args.show = true;
			args.ajax = args.ajax || {};
			args.ajax.dataType = args.ajax.dataType || "json";
			args.ajax.data = {};
			args.ajax.contentType = "application/json";
			args.ajax.url = _this.attr("data-ajax-url");

			args.success = function(json) {
				if (json.code == 200) {
					_this.addClass('active');
					_this.siblings().removeClass('active');

				} else {
					ynf.small_modal({
						body_cont : "提交失败",
						show : true
					});
				}
			};

			$.extend(args.ajax.data, o);

			console.log("ynf.init_oper_cicular_list--args", args);

			if (args.ajax)
				ynf.modal(args);

		});
	};

	ynf.init_oper_list = function(args) { // 通用表格操作

		if (!args.$el || args.$el.length == 0) {
			return false;
		}
		var $el = args.$el;
		var target = args.target;

		$el.on("click", target, function(ev) {
			var _this = $(ev.target);
			 
			if(_this.hasClass('active')){
				ynf.small_modal({"show":true, body_cont:"已经是当前状态" });
				return false ; 
			}
			var o = {};
			if (_this.attr("data-ajax-data")) {
				var arr = _this.attr("data-ajax-data").split(",");
				$.each(arr, function(i, v) {
					o[v.substr(5)] = _this.attr(v);
				});
			}

			if (_this.attr("data-header_text")) {
				args.header_text = _this.attr("data-header_text");
			}

			if (_this.attr("data-body_cont")) {
				args.body_cont = _this.attr("data-body_cont");
			}

			if (_this.attr("data-extend_data") ) {
				args.extend_data = _this.attr("data-extend_data");
			}

			args.show = true;
			args.ajax = args.ajax || {};
			args.ajax.dataType = args.ajax.dataType || "json";
			args.ajax.data = {};
			args.ajax.contentType = "application/json";
			args.ajax.url = _this.attr("data-ajax-url");

			if( $.isFunction( args.beforeSend ) ) {
				args.ajax.beforeSend = args.beforeSend ;
			}

			args.success = args.success || function(json) {
				if (json.code == 200) {
					_this.addClass('active');
					_this.siblings().removeClass('active');

				} else {
					ynf.small_modal({
						body_cont : "提交失败",
						show : true
					});
				}
			};

			$.extend(args.ajax.data, o);

			console.log("ynf.init_oper_cicular_list--args", args);


			if (args.ajax)
				ynf.modal(args);

		});
	};

	ynf.init_single_search = function(args) {
		/*
		 * { special:{"goods_name":"cn"}, }
		 */
		$ynf_table_nav = args.target ||  $(document.body).find("#ynf_table_nav");
		if ($ynf_table_nav.length === 0)
			return false;
		var $search = $ynf_table_nav.find(".select-input-search");
		var select = $search.find("select[data-y-role=search_select]");
		var input = $search.find("input[data-y-role=search_input]");
		var $ynf_list = args.list ||  $("#ynf_list");

		function search() {
			var sidx = select.val();
			var data = $.trim(input.val());
			if (data == "") {
				input.attr("placeholder", _ynf_c.tip.no_val);
				return false;
			}
			if (sidx == "") {
				ynf.small_modal({
					body_cont : _ynf_c.tip.no_search_select,
					show : true
				});
				return false;
			}
			var config = {};
			config._search = true;

			if (args.special && args.special[sidx]) {
				type = args.special[sidx];
			} else {
				type = "eq";
			}
			
			config.filters = '{ "groupOp":"AND" , "rules":[{"field":"' + sidx
					+ '" , "op":"' + type + '", "data":"' + data + '"}] }';
		    if(YNF_CONFIG.ynf_table_nav.filters != config.filters ){
			   config.page= 1;
		    }

			YNF_CONFIG.ynf_table_nav = config;
			$ynf_list.data("ynf_table_nav_args",config);
			$ynf_list.trigger("reloadGrid");
		}

		$ynf_table_nav.on("click", "button[data-y-role=search_btn]", function(
				ev) {
			search();
		});
		$ynf_table_nav.on("keydown", "input[data-y-role=search_input]",
				function(ev) {
					if (ev.keyCode == 13)
						search();
				});

	};

	 ynf.localData = {
          hname:location.hostname?location.hostname:'localStatus',
          isLocalStorage:window.localStorage?true:false,
          dataDom:null,
  
        initDom:function(){ //初始化userData
              if(!this.dataDom){
                try{
                     this.dataDom = document.createElement('input');//这里使用hidden的input元素
                    this.dataDom.type = 'hidden';
                   this.dataDom.style.display = "none";
                    this.dataDom.addBehavior('#default#userData');//这是userData的语法
                    document.body.appendChild(this.dataDom);
                    var exDate = new Date();
                    exDate = exDate.getDate()+30;
                    this.dataDom.expires = exDate.toUTCString();//设定过期时间
                }catch(ex){
                    return false;
               }             }
             return true;
         },
         set:function(key,value){
             if(this.isLocalStorage){
                window.localStorage.setItem(key,value);
             }else{
                 if(this.initDom()){
                     this.dataDom.load(this.hname);
                     this.dataDom.setAttribute(key,value);
                     this.dataDom.save(this.hname)
                 }
             }
         },
         get:function(key){
             if(this.isLocalStorage){
                 return window.localStorage.getItem(key);
             }else{
                 if(this.initDom()){
                     this.dataDom.load(this.hname);
                     return this.dataDom.getAttribute(key);
                 }
             }
         },
         remove:function(key){
             if(this.isLocalStorage){
                 localStorage.removeItem(key);
             }else{
                 if(this.initDom()){
                    this.dataDom.load(this.hname);
                     this.dataDom.removeAttribute(key);
                     this.dataDom.save(this.hname)
                 }
             }
         }
     };

	ynf.empty_datepicker_val = function($target) {
		console.log("--执行 ynf.empty_datepicker_val--");
		$target.find('.date-picker').val("");
	};

	ynf.init_single_filter = function(args) {
		args = args || {};
		var $ynf_table_nav = args.target || $(document.body).find("#ynf_table_nav");
		var $ynf_list = args.list || $(document.body).find("#ynf_list");
		args = args || {};

		if ($ynf_table_nav.length === 0)
			return false;

		$ynf_table_nav
				.on(
						"change",
						"select[data-y-role=search_filter]",
						function(ev) {
							if (args.empty_datepicker == true) {
								ynf.empty_datepicker_val($ynf_table_nav);
							}

							var _this = $(ev.target), rules = "", cur_val = _this
									.val();
							config = {};

							$ynf_table_nav.find(
									"select[data-y-role=search_filter]")
									.val("");

							$search_input = $ynf_table_nav
									.find("input[data-y-role=search_input]");
							if ($search_input.length > 0) {
								$search_input.val(""); // 清空 下拉选择框 的数值
							}

							_this.val(cur_val);
							rules = ynf.get_rules({nav:args.nav});
							if (rules) {
								config._search = true;
								 
								config.filters = '{ "groupOp":"AND" , "rules":['
										+ rules + '] }';
							} else {
								console.log(false);
								config._search = false;
							}
			                
			                 if(YNF_CONFIG.ynf_table_nav.filters != config.filters ){
								   config.page= 1;
							  }

							YNF_CONFIG.ynf_table_nav = config;
							$ynf_list.data("ynf_table_nav_args",config);
							// $ynf_list.data("ynf_table_nav_args",config);
							$ynf_list.trigger("reloadGrid");

						});

	};

	ynf.get_time_rules = function(args) { // 得到时间过滤规则
		var rules;
		var $ynf_table_nav = args.target ||   $(document.body).find("#ynf_table_nav");

		args = args || {};
		if ($ynf_table_nav.length === 0)
			return false;
		if (args.only_time == true) {
			rules = "";
		} else {
			rules = ynf.get_rules({nav:args.nav}); 
		}

		$time_search = $ynf_table_nav.find(".ynf-time-range-search"),
				$begin = $time_search.find(".ynf-search-time-begin"),
				$end = $time_search.find(".ynf-search-time-end"),
				begin_sidx = $begin.attr("sidx"), end_sidx = $end.attr("sidx");

		var begin_date = $begin.find(".date-picker").val(), begin_time = $begin
				.find("#timepicker_start").val(),

		end_date = $end.find(".date-picker").val(), end_time = $end.find(
				"#timepicker_end").val();

		if (begin_date == "" || begin_time == "" || end_date == ""
				|| end_date == "") {
			return "error";
		}
		var begin_time_num = (begin_date.replace(/[\-\s]/g, "") + begin_time
				.replace(/[\:\s]/g, ""));
		var end_time_num = (end_date.replace(/[\-\s]/g, "") + end_time.replace(
				/[\:\s]/g, ""));
		if (parseInt(begin_time_num) > parseInt(end_time_num)) {
			return "not_valid";
		}

		if (rules != "") {
			rules += ",";
		}

		rules += '{"field":"' + begin_sidx + '" , "op":"ge" , "data":"'
				+ begin_date + " " + begin_time + '"}';
		rules += ',{"field":"' + end_sidx + '" , "op":"le" , "data":"'
				+ end_date + " " + end_time + '"}';

		return rules;
	};

	ynf.init_time_start = function() {
		return "00:00:00";
	};

	ynf.init_time_string = function(change,split) {
		var date = new Date();
		 change = change || 0;
		split = split || ":";
		var hour = date.getHours() + "";
		var minute = date.getMinutes() + "";
		var second = (date.getSeconds() +change )+ "";

		hour.length == 1 ? hour = "0" + hour : hour = hour;
		minute.length == 1 ? minute = "0" + minute : minute = minute;
		second.length == 1 ? second = "0" + second : second = second;
		return (hour) + split + (minute) + split + (second);
	};

	ynf.init_date_string = function(change,split) {

		var date = new Date();
		change = change || 0;
		split = split || "-";
		var month = date.getMonth() + 1 + "";
		var day = date.getDate() + change + "";
		month.length == 1 ? month = "0" + month : month = month;
		day.length == 1 ? day = "0" + day : day = day;
		return date.getFullYear() + split + (month) + split + (day);

	};

	// 表导航时间范围搜索
	ynf.init_time_filter = function(args) {
		args = args || {};
		var $ynf_table_nav = args.target ||  $(document.body).find("#ynf_table_nav");
		var $ynf_list = args.list || $(document.body).find("#ynf_list");

		args = args || {};
		if ($ynf_table_nav.length === 0)
			return false;

		// 日期初始化
		$ynf_table_nav.find('.date-picker').val(ynf.init_date_string());

		$ynf_table_nav.find('.date-picker').datepicker({
			autoclose : true,
			defaultDate : new Date()
		}).next().on(ace.click_event, function() {
			$(this).prev().focus();
		});
		// 时间初始化
		$ynf_table_nav.find('#timepicker_start,#timepicker_end').timepicker({
			minuteStep : 1,
			showSeconds : true,
			showMeridian : false
		}).next().on(ace.click_event, function() {
			$(this).prev().focus();
		});
		$ynf_table_nav.find('#timepicker_start').val(ynf.init_time_start());

		$ynf_table_nav
				.on(
						"click",
						"button[data-y-role=time_range_search_btn]",
						function(ev) {
							var _this = $(ev.target), rules = ynf.get_time_rules(args), config = {};
							if (rules === "error") {
								ynf.small_modal({
									body_cont : _ynf_c.tip.no_all_time_range,
									show : true
								});
								return false;
							}

							if (rules === "not_valid") {
								ynf
										.small_modal({
											body_cont : _ynf_c.tip.endtime_lt_starttime,
											show : true
										});
								return false;
							}

							if (rules) {
								config._search = true;
								 
								config.filters = '{ "groupOp":"AND" , "rules":['
										+ rules + '] }';
							} else {
								config._search = false;
							}
							if(YNF_CONFIG.ynf_table_nav.filters != config.filters ){
								   config.page= 1;
							  }
			                
							YNF_CONFIG.ynf_table_nav = config;
							$ynf_list.data("ynf_table_nav_args",config);
							$ynf_list.trigger("reloadGrid");
						});

	};

	ynf.get_info_by_k_v = function(list,key,val){
       if( !$.isArray(list) && list.length ==0   )  return false ; 
       var l = list.length ; 
       if(key== null || list[0][key]== undefined   )  return false ; 
       var result ={};
       for(var i = 0 ; i<l ; i++ ){
          if(list[i][key] == val ){
          	result = list[i];
          	break ; 
          }
       }
       if(!$.isEmptyObject(result)){
          return result ; 
       }else{
           return false ;
       }
         
	};

	ynf.input_valid = function(input, type) {
		if (input.tagName.toLowerCase() !== "input")
			return false;
		if (type == "number" && !$.isNumeric(input.value)) {
			$(input).addClass('error');
		} else {
			$(input).removeClass('error');
		}
	};

	ynf.refresh = function() {
		window.location.reload();
	};

	ynf.init = function() {
		ynf.init_menu_active();
		// 请不要再删除这个方法调用了
		ynf.update_menu_refund();
	    ynf.init_skin();

	};

	// yiyan 退款小红标 请不要再删掉这段代码了
	ynf.update_menu_refund = function() {

		if( !window.YNF_COUNT_PENDING ) return false ;

	  	ynf.ajax({
            url: YNF_COUNT_PENDING,
            type: "get",
            dataType: "json",        
            success: function(json) {
            	$('#ynf_count_pending').text(json.data);
            }          

	  	});

	  };
		  
	ynf.delete_remote_image = function(imageUrl) {
		ynf.ajax({
			contentType : "application/json",
			url : "../image/delete",
			type : "post",
			data : JSON.stringify({
				"imageName" : imageUrl
			}),
			async : false,
			dataType : "text",
			success : function(data) {
				console.log("delete_remote_image -- success");
			}

		});
	};

 
  //时间日期转换  atao  2015-01-09
  ynf.timeStampFormat=function(time)
  {
	  if(time)
	  {
		  time= time.replace("CST","GMT+0800");
		  var datetime = new Date(time);  
		  //datetime.setTime(time); 
		  var year = datetime.getFullYear(); 
		  var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
		  var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate(); 
		  var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
		  var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes(); 
		  var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();  
		  return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
	  }else
	  {
		  return "";
	  }
  };
  ynf.init();
  $.ynf = ynf;

})(jQuery, window);