define("prosecute_audit", function(require, exports, module) {

    require("chat_css");
    require("TweenMax");

    var $input=$(".chat-input")
        ,$messagesContainer=$(".chat-messages")
        ,$messagesList=$(".chat-messages-list")
        ,$chat_status_btns_wrap =$("#chat_status_btns")
        ,$chat_status_btns = $chat_status_btns_wrap.find(".btn")
        ,messages=0
        ;
    // var session =  $.ynf.parse_url(window.location.href).params.id;
    var admin_name = $.trim($(".nav .user-info").text().substr(3,100));
    var cur_chat_user = {};
    cur_chat_user.new_seq = "";
    cur_chat_user.old_seq = "";
    cur_chat_user.is_get_old  = true  ;
    cur_chat_user.session_id = getQueryString("id");
    cur_chat_user.old_seq = 1;

    $(document).ready(function(){
       // init_old_chat();
    });

    setTimeout(function(){
        $chat_status_btns_wrap.find(".btn").eq(0).trigger("click");
    },100);

    $chat_status_btns_wrap.on("click",".btn",function(ev){
        var $this = $(this);
        // if($this.hasClass('active') ) return ;
        $this.addClass("active").siblings().removeClass('active');

        console.log("click--.btn");
        if($this.attr("data-status") == 1){ //未接待
            url = "getbs";
            init_messages(url);
        }
        if($this.attr("data-status") == 2){ //我的接待
            url = "getAll";
            init_messages(url);
        }

    });


    function init_messages(url){
        //拉消息
        $.ynf.ajax({
            url: url,
            type: "get",
            data:  { id: cur_chat_user.session_id },
            // async: false,
            dataType: "json",
            success: function(json) {
                if(json.code == 200){
                    if(json.data.length == 0){
                        cur_chat_user.new_seq = 1;
                        old_seq = 0;

                    }else{
                        cur_chat_user.is_get_old  = true ;
                        cur_chat_user.new_seq = json.data[json.data.length-1].seq;
                        cur_chat_user.old_seq = json.data[0].seq;
                        for(var i = 0 ; i< json.data.length  ; i++ ){
                            if(json.data[i].type == 0  ){
                                cur_chat_user.user_name = json.data[i].sender.user_name;
                                cur_chat_user.icon =  json.data[i].sender.icon || "../assets/images/friend_icon.png";
                                break ;
                            }
                        }
                    }

                    $messagesList.html( create_chat_html(json.data) );
                }
            }
        });
    }


    function create_chat_html (data){

        var message_arr = [];

        if(data.length == 0 ){
            return "没有消息" ;
        }
        var user_info ,friend_info , self_info;
        $.each(data,function(i,v){

            var cont ="";

            friend_info = [ '<div class="chat-time">',v.createtime,'</div><div class="chat-user-info">',
                '<img class="chat-headimage" style="  margin-right:4px;" src="',v.sender && v.sender.icon ? v.sender.icon : "../assets/images/friend_icon.png",'" alt="">' ,
                '<span class="chat-nickname "> ',v.sender &&v.sender.user_name ? user_name(v.sender.user_name):"",'</span> ',
                '</div>'].join("");
            self_info = [ '<div class="chat-time">',v.createtime,'</div><div class="chat-user-info">',
                '<span class="chat-nickname ">',v.sender && v.sender.user_name ? v.sender.user_name: admin_name,'</span> ',
                '<img class="chat-headimage" style="margin-left:4px; " src="',v.sender && v.sender.icon?v.sender.icon:"../assets/images/admin_icon.png",'" alt="">' ,
                '</div>'].join("");
            if(v.session_id== v.sender.id)//表示是自己发的
            {
                switch(v.type){
                    case 1 :
                        cont =  ['<li class="chat-message chat-message-',  "self" ,'">',self_info,'<div class="chat-message-bubble">',render_msg_cont(v.content),'</div></li>'].join("");
                        break;
                    case 2 :
                        cont =  ['<li class="chat-message chat-message-',  "self",'">',self_info,'<div class="chat-message-bubble">',render_msg_cont("[语音]"),'</div></li>'].join("");
                        break;
                    case 3 :
                        cont =  ['<li class="chat-message chat-message-',  "self" ,'">',self_info,'<div class="chat-message-bubble"><img src="',render_msg_cont(v.content),'" style="width:100px; height:100px;"/></div></li>'].join("");
                        break;
                    case 4:
                        cont =  ['<li class="chat-message chat-message-',  "self",'">',self_info,'<div class="chat-message-bubble">',render_msg_cont("[在线语音]"),'</div></li>'].join("");
                        break;
                }
            }else
            {
                switch(v.type){
                    case 1 :
                        cont =  ['<li class="chat-message chat-message-',  "friend" ,'">',friend_info,'<div class="chat-message-bubble">',render_msg_cont(v.content),'</div></li>'].join("");
                        break;
                    case 2 :
                        cont =  ['<li class="chat-message chat-message-',  "friend",'">',friend_info,'<div class="chat-message-bubble">',render_msg_cont("[语音]"),'</div></li>'].join("");
                        break;
                    case 3 :
                        cont =  ['<li class="chat-message chat-message-',  "friend" ,'">',friend_info,'<div class="chat-message-bubble"><img src="',render_msg_cont(v.content),'" style="width:100px; height:100px;"/></div></li>'].join("");
                        break;
                    case 4:
                        cont =  ['<li class="chat-message chat-message-',  "friend",'">',friend_info,'<div class="chat-message-bubble">',render_msg_cont("[在线语音]"),'</div></li>'].join("");
                        break;
                }
            }
            message_arr.push(cont);
        });
        return message_arr.join("");

    }

    function render_msg_cont(c){
        c= c || "";
        c = $.trim(c);
        c = parseEmoji(c);
        return c ;
    }

    //转换常用emoji表情
    function parseEmoji(arg) {

        if (typeof ioNull !='undefined') {
            return  ioNull.emoji.parse(arg);
        }
        return '';
    }

    function user_name(user_name){
        user_name = user_name || "";
        if(user_name.length> 20){
            return user_name.substring(0,20) +"...";
        }
        return user_name ;
    }

    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return r[2]; return null;
    }

});