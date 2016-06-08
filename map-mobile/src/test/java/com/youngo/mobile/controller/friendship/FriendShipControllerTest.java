package com.youngo.mobile.controller.friendship;

import com.youngo.core.model.Result;
import com.youngo.mobile.BaseRestTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by fuchen on 2015/11/23.
 */
public class FriendShipControllerTest extends BaseRestTest
{
    @Test
    public void testList()
    {
        Result result = doGet("/friendship/list");
        assertNotNull(result);
        assertThat(result.getCode(), equalTo(200));
    }
    @Test
    public void testRecommend()
    {
        Result result = doGet("/friendship/recommend?page=1&glans=english,chinese");
        assertNotNull(result);
        assertThat(result.getCode(), equalTo(200));
    }
    @Test
    public void tesRemove()
    {
        Map<String ,String > params = new HashMap<>(1);
        params.put("friendId","1");
        Result result = doPost("/friendship/remove", params);
        assertNotNull(result);
        assertThat(result.getCode(),equalTo(200));
    }
}
