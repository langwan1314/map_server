define("jquery_ui_timepicker-addon",function(require, exports, module) {
 require("jquery_ui");
 require("jquery_ui_slide");
!function($){function Timepicker(){this.regional=[],this.regional[""]={currentText:"当前时间",closeText:"确定",ampm:!1,amNames:["AM","A"],pmNames:["PM","P"],timeFormat:"hh:mm tt",timeSuffix:"",timeOnlyTitle:"选择时间",timeText:"时间",hourText:"时",minuteText:"分",secondText:"秒",millisecText:"毫秒",timezoneText:"Time Zone"},this._defaults={showButtonPanel:!0,timeOnly:!1,showHour:!0,showMinute:!0,showSecond:!1,showMillisec:!1,showTimezone:!1,showTime:!0,stepHour:1,stepMinute:1,stepSecond:1,stepMillisec:1,hour:0,minute:0,second:0,millisec:0,timezone:"+0000",hourMin:0,minuteMin:0,secondMin:0,millisecMin:0,hourMax:23,minuteMax:59,secondMax:59,millisecMax:999,minDateTime:null,maxDateTime:null,onSelect:null,hourGrid:0,minuteGrid:0,secondGrid:0,millisecGrid:0,alwaysSetTime:!0,separator:" ",altFieldTimeOnly:!0,showTimepicker:!0,timezoneIso8609:!1,timezoneList:null,addSliderAccess:!1,sliderAccessArgs:null},$.extend(this._defaults,this.regional[""])}function extendRemove(e,i){$.extend(e,i);for(var t in i)(null===i[t]||void 0===i[t])&&(e[t]=i[t]);return e}$.extend($.ui,{timepicker:{version:"0.9.9"}}),$.extend(Timepicker.prototype,{$input:null,$altInput:null,$timeObj:null,inst:null,hour_slider:null,minute_slider:null,second_slider:null,millisec_slider:null,timezone_select:null,hour:0,minute:0,second:0,millisec:0,timezone:"+0000",hourMinOriginal:null,minuteMinOriginal:null,secondMinOriginal:null,millisecMinOriginal:null,hourMaxOriginal:null,minuteMaxOriginal:null,secondMaxOriginal:null,millisecMaxOriginal:null,ampm:"",formattedDate:"",formattedTime:"",formattedDateTime:"",timezoneList:null,setDefaults:function(e){return extendRemove(this._defaults,e||{}),this},_newInst:function($input,o){var tp_inst=new Timepicker,inlineSettings={};for(var attrName in this._defaults){var attrValue=$input.attr("time:"+attrName);if(attrValue)try{inlineSettings[attrName]=eval(attrValue)}catch(err){inlineSettings[attrName]=attrValue}}if(tp_inst._defaults=$.extend({},this._defaults,inlineSettings,o,{beforeShow:function(e,i){return $.isFunction(o.beforeShow)?o.beforeShow(e,i,tp_inst):void 0},onChangeMonthYear:function(e,i,t){tp_inst._updateDateTime(t),$.isFunction(o.onChangeMonthYear)&&o.onChangeMonthYear.call($input[0],e,i,t,tp_inst)},onClose:function(e,i){tp_inst.timeDefined===!0&&""!=$input.val()&&tp_inst._updateDateTime(i),$.isFunction(o.onClose)&&o.onClose.call($input[0],e,i,tp_inst)},timepicker:tp_inst}),tp_inst.amNames=$.map(tp_inst._defaults.amNames,function(e){return e.toUpperCase()}),tp_inst.pmNames=$.map(tp_inst._defaults.pmNames,function(e){return e.toUpperCase()}),null===tp_inst._defaults.timezoneList){for(var timezoneList=[],i=-11;12>=i;i++)timezoneList.push((i>=0?"+":"-")+("0"+Math.abs(i).toString()).slice(-2)+"00");tp_inst._defaults.timezoneIso8609&&(timezoneList=$.map(timezoneList,function(e){return"+0000"==e?"Z":e.substring(0,3)+":"+e.substring(3)})),tp_inst._defaults.timezoneList=timezoneList}return tp_inst.hour=tp_inst._defaults.hour,tp_inst.minute=tp_inst._defaults.minute,tp_inst.second=tp_inst._defaults.second,tp_inst.millisec=tp_inst._defaults.millisec,tp_inst.ampm="",tp_inst.$input=$input,o.altField&&(tp_inst.$altInput=$(o.altField).css({cursor:"pointer"}).focus(function(){$input.trigger("focus")})),(0==tp_inst._defaults.minDate||0==tp_inst._defaults.minDateTime)&&(tp_inst._defaults.minDate=new Date),(0==tp_inst._defaults.maxDate||0==tp_inst._defaults.maxDateTime)&&(tp_inst._defaults.maxDate=new Date),void 0!==tp_inst._defaults.minDate&&tp_inst._defaults.minDate instanceof Date&&(tp_inst._defaults.minDateTime=new Date(tp_inst._defaults.minDate.getTime())),void 0!==tp_inst._defaults.minDateTime&&tp_inst._defaults.minDateTime instanceof Date&&(tp_inst._defaults.minDate=new Date(tp_inst._defaults.minDateTime.getTime())),void 0!==tp_inst._defaults.maxDate&&tp_inst._defaults.maxDate instanceof Date&&(tp_inst._defaults.maxDateTime=new Date(tp_inst._defaults.maxDate.getTime())),void 0!==tp_inst._defaults.maxDateTime&&tp_inst._defaults.maxDateTime instanceof Date&&(tp_inst._defaults.maxDate=new Date(tp_inst._defaults.maxDateTime.getTime())),tp_inst},_addTimePicker:function(e){var i=this.$altInput&&this._defaults.altFieldTimeOnly?this.$input.val()+" "+this.$altInput.val():this.$input.val();this.timeDefined=this._parseTime(i),this._limitMinMaxDateTime(e,!1),this._injectTimePicker()},_parseTime:function(e,i){var t,s=this._defaults.timeFormat.toString().replace(/h{1,2}/gi,"(\\d?\\d)").replace(/m{1,2}/gi,"(\\d?\\d)").replace(/s{1,2}/gi,"(\\d?\\d)").replace(/l{1}/gi,"(\\d?\\d?\\d)").replace(/t{1,2}/gi,this._getPatternAmpm()).replace(/z{1}/gi,"(z|[-+]\\d\\d:?\\d\\d)?").replace(/\s/g,"\\s?")+this._defaults.timeSuffix+"$",a=this._getFormatPositions(),n="";if(this.inst||(this.inst=$.datepicker._getInst(this.$input[0])),i||!this._defaults.timeOnly){var l=$.datepicker._get(this.inst,"dateFormat"),r=new RegExp("[.*+?|()\\[\\]{}\\\\]","g");s="^.{"+l.length+",}?"+this._defaults.separator.replace(r,"\\$&")+s}if(t=e.match(new RegExp(s,"i"))){if(-1!==a.t&&(void 0===t[a.t]||0===t[a.t].length?(n="",this.ampm=""):(n=-1!==$.inArray(t[a.t].toUpperCase(),this.amNames)?"AM":"PM",this.ampm=this._defaults["AM"==n?"amNames":"pmNames"][0])),-1!==a.h&&(this.hour="AM"==n&&"12"==t[a.h]?0:"PM"==n&&"12"!=t[a.h]?(parseFloat(t[a.h])+12).toFixed(0):Number(t[a.h])),-1!==a.m&&(this.minute=Number(t[a.m])),-1!==a.s&&(this.second=Number(t[a.s])),-1!==a.l&&(this.millisec=Number(t[a.l])),-1!==a.z&&void 0!==t[a.z]){var d=t[a.z].toUpperCase();switch(d.length){case 1:d=this._defaults.timezoneIso8609?"Z":"+0000";break;case 5:this._defaults.timezoneIso8609&&(d="0000"==d.substring(1)?"Z":d.substring(0,3)+":"+d.substring(3));break;case 6:this._defaults.timezoneIso8609?"00:00"==d.substring(1)&&(d="Z"):d="Z"==d||"00:00"==d.substring(1)?"+0000":d.replace(/:/,"")}this.timezone=d}return!0}return!1},_getPatternAmpm:function(){var e=[];return o=this._defaults,o.amNames&&$.merge(e,o.amNames),o.pmNames&&$.merge(e,o.pmNames),e=$.map(e,function(e){return e.replace(/[.*+?|()\[\]{}\\]/g,"\\$&")}),"("+e.join("|")+")?"},_getFormatPositions:function(){var e=this._defaults.timeFormat.toLowerCase().match(/(h{1,2}|m{1,2}|s{1,2}|l{1}|t{1,2}|z)/g),i={h:-1,m:-1,s:-1,l:-1,t:-1,z:-1};if(e)for(var t=0;t<e.length;t++)-1==i[e[t].toString().charAt(0)]&&(i[e[t].toString().charAt(0)]=t+1);return i},_injectTimePicker:function(){var e=this.inst.dpDiv,i=this._defaults,t=this,s=parseInt(i.hourMax-(i.hourMax-i.hourMin)%i.stepHour,10),a=parseInt(i.minuteMax-(i.minuteMax-i.minuteMin)%i.stepMinute,10),n=parseInt(i.secondMax-(i.secondMax-i.secondMin)%i.stepSecond,10),l=parseInt(i.millisecMax-(i.millisecMax-i.millisecMin)%i.stepMillisec,10),r=this.inst.id.toString().replace(/([^A-Za-z0-9_])/g,"");if(0===e.find("div#ui-timepicker-div-"+r).length&&i.showTimepicker){var d,o=' style="display:none;"',u='<div class="ui-timepicker-div" id="ui-timepicker-div-'+r+'"><dl><dt class="ui_tpicker_time_label" id="ui_tpicker_time_label_'+r+'"'+(i.showTime?"":o)+">"+i.timeText+'</dt><dd class="ui_tpicker_time" id="ui_tpicker_time_'+r+'"'+(i.showTime?"":o)+'></dd><dt class="ui_tpicker_hour_label" id="ui_tpicker_hour_label_'+r+'"'+(i.showHour?"":o)+">"+i.hourText+"</dt>",m=0,c=0,h=0,_=0;if(u+='<dd class="ui_tpicker_hour"><div id="ui_tpicker_hour_'+r+'"'+(i.showHour?"":o)+"></div>",i.showHour&&i.hourGrid>0){u+='<div style="padding-left: 1px"><table class="ui-tpicker-grid-label"><tr>';for(var p=i.hourMin;s>=p;p+=parseInt(i.hourGrid,10)){m++;var f=i.ampm&&p>12?p-12:p;10>f&&(f="0"+f),i.ampm&&(0==p?f="12a":f+=12>p?"a":"p"),u+="<td>"+f+"</td>"}u+="</tr></table></div>"}if(u+="</dd>",u+='<dt class="ui_tpicker_minute_label" id="ui_tpicker_minute_label_'+r+'"'+(i.showMinute?"":o)+">"+i.minuteText+'</dt><dd class="ui_tpicker_minute"><div id="ui_tpicker_minute_'+r+'"'+(i.showMinute?"":o)+"></div>",i.showMinute&&i.minuteGrid>0){u+='<div style="padding-left: 1px"><table class="ui-tpicker-grid-label"><tr>';for(var g=i.minuteMin;a>=g;g+=parseInt(i.minuteGrid,10))c++,u+="<td>"+(10>g?"0":"")+g+"</td>";u+="</tr></table></div>"}if(u+="</dd>",u+='<dt class="ui_tpicker_second_label" id="ui_tpicker_second_label_'+r+'"'+(i.showSecond?"":o)+">"+i.secondText+'</dt><dd class="ui_tpicker_second"><div id="ui_tpicker_second_'+r+'"'+(i.showSecond?"":o)+"></div>",i.showSecond&&i.secondGrid>0){u+='<div style="padding-left: 1px"><table><tr>';for(var M=i.secondMin;n>=M;M+=parseInt(i.secondGrid,10))h++,u+="<td>"+(10>M?"0":"")+M+"</td>";u+="</tr></table></div>"}if(u+="</dd>",u+='<dt class="ui_tpicker_millisec_label" id="ui_tpicker_millisec_label_'+r+'"'+(i.showMillisec?"":o)+">"+i.millisecText+'</dt><dd class="ui_tpicker_millisec"><div id="ui_tpicker_millisec_'+r+'"'+(i.showMillisec?"":o)+"></div>",i.showMillisec&&i.millisecGrid>0){u+='<div style="padding-left: 1px"><table><tr>';for(var k=i.millisecMin;l>=k;k+=parseInt(i.millisecGrid,10))_++,u+="<td>"+(10>k?"0":"")+k+"</td>";u+="</tr></table></div>"}u+="</dd>",u+='<dt class="ui_tpicker_timezone_label" id="ui_tpicker_timezone_label_'+r+'"'+(i.showTimezone?"":o)+">"+i.timezoneText+"</dt>",u+='<dd class="ui_tpicker_timezone" id="ui_tpicker_timezone_'+r+'"'+(i.showTimezone?"":o)+"></dd>",u+="</dl></div>",$tp=$(u),i.timeOnly===!0&&($tp.prepend('<div class="ui-widget-header ui-helper-clearfix ui-corner-all"><div class="ui-datepicker-title">'+i.timeOnlyTitle+"</div></div>"),e.find(".ui-datepicker-header, .ui-datepicker-calendar").hide()),this.hour_slider=$tp.find("#ui_tpicker_hour_"+r).slider({orientation:"horizontal",value:this.hour,min:i.hourMin,max:s,step:i.stepHour,slide:function(e,i){t.hour_slider.slider("option","value",i.value),t._onTimeChange()}}),this.minute_slider=$tp.find("#ui_tpicker_minute_"+r).slider({orientation:"horizontal",value:this.minute,min:i.minuteMin,max:a,step:i.stepMinute,slide:function(e,i){t.minute_slider.slider("option","value",i.value),t._onTimeChange()}}),this.second_slider=$tp.find("#ui_tpicker_second_"+r).slider({orientation:"horizontal",value:this.second,min:i.secondMin,max:n,step:i.stepSecond,slide:function(e,i){t.second_slider.slider("option","value",i.value),t._onTimeChange()}}),this.millisec_slider=$tp.find("#ui_tpicker_millisec_"+r).slider({orientation:"horizontal",value:this.millisec,min:i.millisecMin,max:l,step:i.stepMillisec,slide:function(e,i){t.millisec_slider.slider("option","value",i.value),t._onTimeChange()}}),this.timezone_select=$tp.find("#ui_tpicker_timezone_"+r).append("<select></select>").find("select"),$.fn.append.apply(this.timezone_select,$.map(i.timezoneList,function(e){return $("<option />").val("object"==typeof e?e.value:e).text("object"==typeof e?e.label:e)})),this.timezone_select.val("undefined"!=typeof this.timezone&&null!=this.timezone&&""!=this.timezone?this.timezone:i.timezone),this.timezone_select.change(function(){t._onTimeChange()}),i.showHour&&i.hourGrid>0&&(d=100*m*i.hourGrid/(s-i.hourMin),$tp.find(".ui_tpicker_hour table").css({width:d+"%",marginLeft:d/(-2*m)+"%",borderCollapse:"collapse"}).find("td").each(function(){$(this).click(function(){var e=$(this).html();if(i.ampm){var s=e.substring(2).toLowerCase(),a=parseInt(e.substring(0,2),10);e="a"==s?12==a?0:a:12==a?12:a+12}t.hour_slider.slider("option","value",e),t._onTimeChange(),t._onSelectHandler()}).css({cursor:"pointer",width:100/m+"%",textAlign:"center",overflow:"hidden"})})),i.showMinute&&i.minuteGrid>0&&(d=100*c*i.minuteGrid/(a-i.minuteMin),$tp.find(".ui_tpicker_minute table").css({width:d+"%",marginLeft:d/(-2*c)+"%",borderCollapse:"collapse"}).find("td").each(function(){$(this).click(function(){t.minute_slider.slider("option","value",$(this).html()),t._onTimeChange(),t._onSelectHandler()}).css({cursor:"pointer",width:100/c+"%",textAlign:"center",overflow:"hidden"})})),i.showSecond&&i.secondGrid>0&&$tp.find(".ui_tpicker_second table").css({width:d+"%",marginLeft:d/(-2*h)+"%",borderCollapse:"collapse"}).find("td").each(function(){$(this).click(function(){t.second_slider.slider("option","value",$(this).html()),t._onTimeChange(),t._onSelectHandler()}).css({cursor:"pointer",width:100/h+"%",textAlign:"center",overflow:"hidden"})}),i.showMillisec&&i.millisecGrid>0&&$tp.find(".ui_tpicker_millisec table").css({width:d+"%",marginLeft:d/(-2*_)+"%",borderCollapse:"collapse"}).find("td").each(function(){$(this).click(function(){t.millisec_slider.slider("option","value",$(this).html()),t._onTimeChange(),t._onSelectHandler()}).css({cursor:"pointer",width:100/_+"%",textAlign:"center",overflow:"hidden"})});var v=e.find(".ui-datepicker-buttonpane");if(v.length?v.before($tp):e.append($tp),this.$timeObj=$tp.find("#ui_tpicker_time_"+r),null!==this.inst){var D=this.timeDefined;this._onTimeChange(),this.timeDefined=D}var x=function(){t._onSelectHandler()};if(this.hour_slider.bind("slidestop",x),this.minute_slider.bind("slidestop",x),this.second_slider.bind("slidestop",x),this.millisec_slider.bind("slidestop",x),this._defaults.addSliderAccess){var T=this._defaults.sliderAccessArgs;setTimeout(function(){if(0==$tp.find(".ui-slider-access").length){$tp.find(".ui-slider:visible").sliderAccess(T);var e=$tp.find(".ui-slider-access:eq(0)").outerWidth(!0);e&&$tp.find("table:visible").each(function(){var i=$(this),t=i.outerWidth(),s=i.css("marginLeft").toString().replace("%",""),a=t-e,n=s*a/t+"%";i.css({width:a,marginLeft:n})})}},0)}}},_limitMinMaxDateTime:function(e,i){var t=this._defaults,s=new Date(e.selectedYear,e.selectedMonth,e.selectedDay);if(this._defaults.showTimepicker){if(null!==$.datepicker._get(e,"minDateTime")&&void 0!==$.datepicker._get(e,"minDateTime")&&s){var a=$.datepicker._get(e,"minDateTime"),n=new Date(a.getFullYear(),a.getMonth(),a.getDate(),0,0,0,0);(null===this.hourMinOriginal||null===this.minuteMinOriginal||null===this.secondMinOriginal||null===this.millisecMinOriginal)&&(this.hourMinOriginal=t.hourMin,this.minuteMinOriginal=t.minuteMin,this.secondMinOriginal=t.secondMin,this.millisecMinOriginal=t.millisecMin),e.settings.timeOnly||n.getTime()==s.getTime()?(this._defaults.hourMin=a.getHours(),this.hour<=this._defaults.hourMin?(this.hour=this._defaults.hourMin,this._defaults.minuteMin=a.getMinutes(),this.minute<=this._defaults.minuteMin?(this.minute=this._defaults.minuteMin,this._defaults.secondMin=a.getSeconds()):this.second<=this._defaults.secondMin?(this.second=this._defaults.secondMin,this._defaults.millisecMin=a.getMilliseconds()):(this.millisec<this._defaults.millisecMin&&(this.millisec=this._defaults.millisecMin),this._defaults.millisecMin=this.millisecMinOriginal)):(this._defaults.minuteMin=this.minuteMinOriginal,this._defaults.secondMin=this.secondMinOriginal,this._defaults.millisecMin=this.millisecMinOriginal)):(this._defaults.hourMin=this.hourMinOriginal,this._defaults.minuteMin=this.minuteMinOriginal,this._defaults.secondMin=this.secondMinOriginal,this._defaults.millisecMin=this.millisecMinOriginal)}if(null!==$.datepicker._get(e,"maxDateTime")&&void 0!==$.datepicker._get(e,"maxDateTime")&&s){var l=$.datepicker._get(e,"maxDateTime"),r=new Date(l.getFullYear(),l.getMonth(),l.getDate(),0,0,0,0);(null===this.hourMaxOriginal||null===this.minuteMaxOriginal||null===this.secondMaxOriginal)&&(this.hourMaxOriginal=t.hourMax,this.minuteMaxOriginal=t.minuteMax,this.secondMaxOriginal=t.secondMax,this.millisecMaxOriginal=t.millisecMax),e.settings.timeOnly||r.getTime()==s.getTime()?(this._defaults.hourMax=l.getHours(),this.hour>=this._defaults.hourMax?(this.hour=this._defaults.hourMax,this._defaults.minuteMax=l.getMinutes(),this.minute>=this._defaults.minuteMax?(this.minute=this._defaults.minuteMax,this._defaults.secondMax=l.getSeconds()):this.second>=this._defaults.secondMax?(this.second=this._defaults.secondMax,this._defaults.millisecMax=l.getMilliseconds()):(this.millisec>this._defaults.millisecMax&&(this.millisec=this._defaults.millisecMax),this._defaults.millisecMax=this.millisecMaxOriginal)):(this._defaults.minuteMax=this.minuteMaxOriginal,this._defaults.secondMax=this.secondMaxOriginal,this._defaults.millisecMax=this.millisecMaxOriginal)):(this._defaults.hourMax=this.hourMaxOriginal,this._defaults.minuteMax=this.minuteMaxOriginal,this._defaults.secondMax=this.secondMaxOriginal,this._defaults.millisecMax=this.millisecMaxOriginal)}if(void 0!==i&&i===!0){var d=parseInt(this._defaults.hourMax-(this._defaults.hourMax-this._defaults.hourMin)%this._defaults.stepHour,10),o=parseInt(this._defaults.minuteMax-(this._defaults.minuteMax-this._defaults.minuteMin)%this._defaults.stepMinute,10),u=parseInt(this._defaults.secondMax-(this._defaults.secondMax-this._defaults.secondMin)%this._defaults.stepSecond,10),m=parseInt(this._defaults.millisecMax-(this._defaults.millisecMax-this._defaults.millisecMin)%this._defaults.stepMillisec,10);this.hour_slider&&this.hour_slider.slider("option",{min:this._defaults.hourMin,max:d}).slider("value",this.hour),this.minute_slider&&this.minute_slider.slider("option",{min:this._defaults.minuteMin,max:o}).slider("value",this.minute),this.second_slider&&this.second_slider.slider("option",{min:this._defaults.secondMin,max:u}).slider("value",this.second),this.millisec_slider&&this.millisec_slider.slider("option",{min:this._defaults.millisecMin,max:m}).slider("value",this.millisec)}}},_onTimeChange:function(){var e=this.hour_slider?this.hour_slider.slider("value"):!1,i=this.minute_slider?this.minute_slider.slider("value"):!1,t=this.second_slider?this.second_slider.slider("value"):!1,s=this.millisec_slider?this.millisec_slider.slider("value"):!1,a=this.timezone_select?this.timezone_select.val():!1,n=this._defaults;"object"==typeof e&&(e=!1),"object"==typeof i&&(i=!1),"object"==typeof t&&(t=!1),"object"==typeof s&&(s=!1),"object"==typeof a&&(a=!1),e!==!1&&(e=parseInt(e,10)),i!==!1&&(i=parseInt(i,10)),t!==!1&&(t=parseInt(t,10)),s!==!1&&(s=parseInt(s,10));var l=n[12>e?"amNames":"pmNames"][0],r=e!=this.hour||i!=this.minute||t!=this.second||s!=this.millisec||this.ampm.length>0&&12>e!=(-1!==$.inArray(this.ampm.toUpperCase(),this.amNames))||a!=this.timezone;r&&(e!==!1&&(this.hour=e),i!==!1&&(this.minute=i),t!==!1&&(this.second=t),s!==!1&&(this.millisec=s),a!==!1&&(this.timezone=a),this.inst||(this.inst=$.datepicker._getInst(this.$input[0])),this._limitMinMaxDateTime(this.inst,!0)),n.ampm&&(this.ampm=l),this.formattedTime=$.datepicker.formatTime(this._defaults.timeFormat,this,this._defaults),this.$timeObj&&this.$timeObj.text(this.formattedTime+n.timeSuffix),this.timeDefined=!0,r&&this._updateDateTime()},_onSelectHandler:function(){var e=this._defaults.onSelect,i=this.$input?this.$input[0]:null;e&&i&&e.apply(i,[this.formattedDateTime,this])},_formatTime:function(e,i){e=e||{hour:this.hour,minute:this.minute,second:this.second,millisec:this.millisec,ampm:this.ampm,timezone:this.timezone};var t=(i||this._defaults.timeFormat).toString();return t=$.datepicker.formatTime(t,e,this._defaults),arguments.length?t:void(this.formattedTime=t)},_updateDateTime:function(e){e=this.inst||e;var i=$.datepicker._daylightSavingAdjust(new Date(e.selectedYear,e.selectedMonth,e.selectedDay)),t=$.datepicker._get(e,"dateFormat"),s=$.datepicker._getFormatConfig(e),a=null!==i&&this.timeDefined;this.formattedDate=$.datepicker.formatDate(t,null===i?new Date:i,s);var n=this.formattedDate;void 0!==e.lastVal&&e.lastVal.length>0&&0===this.$input.val().length||(this._defaults.timeOnly===!0?n=this.formattedTime:this._defaults.timeOnly!==!0&&(this._defaults.alwaysSetTime||a)&&(n+=this._defaults.separator+this.formattedTime+this._defaults.timeSuffix),this.formattedDateTime=n,this._defaults.showTimepicker?this.$altInput&&this._defaults.altFieldTimeOnly===!0?(this.$altInput.val(this.formattedTime),this.$input.val(this.formattedDate)):this.$altInput?(this.$altInput.val(n),this.$input.val(n)):this.$input.val(n):this.$input.val(this.formattedDate),this.$input.trigger("change"))}}),$.fn.extend({timepicker:function(e){e=e||{};var i=arguments;return"object"==typeof e&&(i[0]=$.extend(e,{timeOnly:!0})),$(this).each(function(){$.fn.datetimepicker.apply($(this),i)})},datetimepicker:function(e){e=e||{};var i=arguments;return"string"==typeof e?"getDate"==e?$.fn.datepicker.apply($(this[0]),i):this.each(function(){var e=$(this);e.datepicker.apply(e,i)}):this.each(function(){var i=$(this);i.datepicker($.timepicker._newInst(i,e)._defaults)})}}),$.datepicker.formatTime=function(e,i,t){t=t||{},t=$.extend($.timepicker._defaults,t),i=$.extend({hour:0,minute:0,second:0,millisec:0,timezone:"+0000"},i);var s=e,a=t.amNames[0],n=parseInt(i.hour,10);return t.ampm&&(n>11&&(a=t.pmNames[0],n>12&&(n%=12)),0===n&&(n=12)),s=s.replace(/(?:hh?|mm?|ss?|[tT]{1,2}|[lz])/g,function(e){switch(e.toLowerCase()){case"hh":return("0"+n).slice(-2);case"h":return n;case"mm":return("0"+i.minute).slice(-2);case"m":return i.minute;case"ss":return("0"+i.second).slice(-2);case"s":return i.second;case"l":return("00"+i.millisec).slice(-3);case"z":return i.timezone;case"t":case"tt":return t.ampm?(1==e.length&&(a=a.charAt(0)),"T"==e.charAt(0)?a.toUpperCase():a.toLowerCase()):""}}),s=$.trim(s)},$.datepicker._base_selectDate=$.datepicker._selectDate,$.datepicker._selectDate=function(e,i){var t=this._getInst($(e)[0]),s=this._get(t,"timepicker");s?(s._limitMinMaxDateTime(t,!0),t.inline=t.stay_open=!0,this._base_selectDate(e,i),t.inline=t.stay_open=!1,this._notifyChange(t),this._updateDatepicker(t)):this._base_selectDate(e,i)},$.datepicker._base_updateDatepicker=$.datepicker._updateDatepicker,$.datepicker._updateDatepicker=function(e){var i=e.input[0];if(!($.datepicker._curInst&&$.datepicker._curInst!=e&&$.datepicker._datepickerShowing&&$.datepicker._lastInput!=i||"boolean"==typeof e.stay_open&&e.stay_open!==!1)){this._base_updateDatepicker(e);var t=this._get(e,"timepicker");t&&t._addTimePicker(e)}},$.datepicker._base_doKeyPress=$.datepicker._doKeyPress,$.datepicker._doKeyPress=function(e){var i=$.datepicker._getInst(e.target),t=$.datepicker._get(i,"timepicker");if(t&&$.datepicker._get(i,"constrainInput")){var s=t._defaults.ampm,a=$.datepicker._possibleChars($.datepicker._get(i,"dateFormat")),n=t._defaults.timeFormat.toString().replace(/[hms]/g,"").replace(/TT/g,s?"APM":"").replace(/Tt/g,s?"AaPpMm":"").replace(/tT/g,s?"AaPpMm":"").replace(/T/g,s?"AP":"").replace(/tt/g,s?"apm":"").replace(/t/g,s?"ap":"")+" "+t._defaults.separator+t._defaults.timeSuffix+(t._defaults.showTimezone?t._defaults.timezoneList.join(""):"")+t._defaults.amNames.join("")+t._defaults.pmNames.join("")+a,l=String.fromCharCode(void 0===e.charCode?e.keyCode:e.charCode);return e.ctrlKey||" ">l||!a||n.indexOf(l)>-1}return $.datepicker._base_doKeyPress(e)},$.datepicker._base_doKeyUp=$.datepicker._doKeyUp,$.datepicker._doKeyUp=function(e){var i=$.datepicker._getInst(e.target),t=$.datepicker._get(i,"timepicker");if(t&&t._defaults.timeOnly&&i.input.val()!=i.lastVal)try{$.datepicker._updateDatepicker(i)}catch(s){$.datepicker.log(s)}return $.datepicker._base_doKeyUp(e)},$.datepicker._base_gotoToday=$.datepicker._gotoToday,$.datepicker._gotoToday=function(e){var i=this._getInst($(e)[0]),t=i.dpDiv;this._base_gotoToday(e);var s=new Date,a=this._get(i,"timepicker");if(a&&a._defaults.showTimezone&&a.timezone_select){var n=s.getTimezoneOffset(),l=n>0?"-":"+";n=Math.abs(n);var r=n%60;n=l+("0"+(n-r)/60).slice(-2)+("0"+r).slice(-2),a._defaults.timezoneIso8609&&(n=n.substring(0,3)+":"+n.substring(3)),a.timezone_select.val(n)}this._setTime(i,s),$(".ui-datepicker-today",t).click()},$.datepicker._disableTimepickerDatepicker=function(e){var i=this._getInst(e),t=this._get(i,"timepicker");$(e).datepicker("getDate"),t&&(t._defaults.showTimepicker=!1,t._updateDateTime(i))},$.datepicker._enableTimepickerDatepicker=function(e){var i=this._getInst(e),t=this._get(i,"timepicker");$(e).datepicker("getDate"),t&&(t._defaults.showTimepicker=!0,t._addTimePicker(i),t._updateDateTime(i))},$.datepicker._setTime=function(e,i){var t=this._get(e,"timepicker");if(t){var s=t._defaults,a=i?i.getHours():s.hour,n=i?i.getMinutes():s.minute,l=i?i.getSeconds():s.second,r=i?i.getMilliseconds():s.millisec;(a<s.hourMin||a>s.hourMax||n<s.minuteMin||n>s.minuteMax||l<s.secondMin||l>s.secondMax||r<s.millisecMin||r>s.millisecMax)&&(a=s.hourMin,n=s.minuteMin,l=s.secondMin,r=s.millisecMin),t.hour=a,t.minute=n,t.second=l,t.millisec=r,t.hour_slider&&t.hour_slider.slider("value",a),t.minute_slider&&t.minute_slider.slider("value",n),t.second_slider&&t.second_slider.slider("value",l),t.millisec_slider&&t.millisec_slider.slider("value",r),t._onTimeChange(),t._updateDateTime(e)}},$.datepicker._setTimeDatepicker=function(e,i,t){var s=this._getInst(e),a=this._get(s,"timepicker");if(a){this._setDateFromField(s);var n;i&&("string"==typeof i?(a._parseTime(i,t),n=new Date,n.setHours(a.hour,a.minute,a.second,a.millisec)):n=new Date(i.getTime()),"Invalid Date"==n.toString()&&(n=void 0),this._setTime(s,n))}},$.datepicker._base_setDateDatepicker=$.datepicker._setDateDatepicker,$.datepicker._setDateDatepicker=function(e,i){var t=this._getInst(e),s=i instanceof Date?new Date(i.getTime()):i;this._updateDatepicker(t),this._base_setDateDatepicker.apply(this,arguments),this._setTimeDatepicker(e,s,!0)},$.datepicker._base_getDateDatepicker=$.datepicker._getDateDatepicker,$.datepicker._getDateDatepicker=function(e,i){var t=this._getInst(e),s=this._get(t,"timepicker");if(s){this._setDateFromField(t,i);var a=this._getDate(t);return a&&s._parseTime($(e).val(),s.timeOnly)&&a.setHours(s.hour,s.minute,s.second,s.millisec),a}return this._base_getDateDatepicker(e,i)},$.datepicker._base_parseDate=$.datepicker.parseDate,$.datepicker.parseDate=function(e,i,t){var s;try{s=this._base_parseDate(e,i,t)}catch(a){if(!(a.indexOf(":")>=0))throw a;s=this._base_parseDate(e,i.substring(0,i.length-(a.length-a.indexOf(":")-2)),t)}return s},$.datepicker._base_formatDate=$.datepicker._formatDate,$.datepicker._formatDate=function(e,i,t,s){var a=this._get(e,"timepicker");if(a){if(i){this._base_formatDate(e,i,t,s)}return a._updateDateTime(e),a.$input.val()}return this._base_formatDate(e)},$.datepicker._base_optionDatepicker=$.datepicker._optionDatepicker,$.datepicker._optionDatepicker=function(e,i,t){var s=this._getInst(e),a=this._get(s,"timepicker");if(a){var n,l,r;"string"==typeof i?"minDate"===i||"minDateTime"===i?n=t:"maxDate"===i||"maxDateTime"===i?l=t:"onSelect"===i&&(r=t):"object"==typeof i&&(i.minDate?n=i.minDate:i.minDateTime?n=i.minDateTime:i.maxDate?l=i.maxDate:i.maxDateTime&&(l=i.maxDateTime)),n?(n=0==n?new Date:new Date(n),a._defaults.minDate=n,a._defaults.minDateTime=n):l?(l=0==l?new Date:new Date(l),a._defaults.maxDate=l,a._defaults.maxDateTime=l):r&&(a._defaults.onSelect=r)}return void 0===t?this._base_optionDatepicker(e,i):this._base_optionDatepicker(e,i,t)},$.timepicker=new Timepicker,$.timepicker.version="0.9.9"}(jQuery);
});