package com.youngo.admin.controller;

import com.youngo.core.model.Login;
import com.youngo.core.model.msg.UnreadEntity;
import com.youngo.ssdb.core.DefaultHashMapOperations;
import com.youngo.ssdb.core.HashMapOperations;
import com.youngo.ssdb.core.SsdbConstants;
import org.nutz.ssdb4j.spi.SSDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Created by FC on 2016/2/18.
 * 对消息服务器的消息的管理
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/ssdb")
public class SSDBController
{
    private HashMapOperations<String, String, UnreadEntity> unReadOperations;
    @Autowired
    public void setSsdb(SSDB ssdb)
    {
        this.unReadOperations = new DefaultHashMapOperations<>(ssdb,String.class,String.class,UnreadEntity.class);
    }

    /**
     * 读取未读消息
     * 清除所有未读消息，慎用
     */
    @Login
    @RequestMapping(method = RequestMethod.GET, value = "clear")
    public String remove(HttpServletRequest request)
    {
        Set<String> names = unReadOperations.getNames(SsdbConstants.Chat.chatUnReadPrefix, "chat:unreae", 10000);
        for(String name:names)
        {
            unReadOperations.clear(name);
        }
        return "success";
    }

}
