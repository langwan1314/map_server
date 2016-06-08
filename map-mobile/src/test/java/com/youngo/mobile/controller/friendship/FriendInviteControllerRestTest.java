package com.youngo.mobile.controller.friendship;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import com.youngo.core.model.Result;
import com.youngo.mobile.BaseRestTest;
import com.youngo.mobile.model.friendship.FriendInvite;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpRequest;
import org.apache.http.message.BasicHttpRequest;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fuchen on 2015/11/23.
 */
public class FriendInviteControllerRestTest extends BaseRestTest
{
    @Test
    public void testAdd()
    {
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("friendId", "4");
        Result result = doPost("/friendinvite/add", params);
        assertNotNull(result);
        assertThat(result.getCode(), equalTo(200));
        params.put("friendId", "-1");
        result = doPost("/friendinvite/add", params);
        assertNotNull(result);
        assertThat(result.getCode(), equalTo(-2));
    }

    @Test
    public void testAgree()
    {
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("id","1");
        Result result = doPost("/friendinvite/agree", params);
        assertNotNull(result);
        assertThat(result.getCode(), equalTo(200));
    }

    @Test
    public void testRefuse()
    {
        Map<String, String> params = new HashMap<String, String>(1);
        params.put("id","1");
        Result result = doPost("/friendinvite/agree", params);
        assertNotNull(result);
        assertThat(result.getCode(), equalTo(200));
    }

    @Test(timeout = BaseRestTest.defaultTimeout)
    public void testList()
    {
        Result result = doGet("/friendinvite/list");
        assertNotNull(result);
        assertThat(result.getCode(), equalTo(200));
    }
}
