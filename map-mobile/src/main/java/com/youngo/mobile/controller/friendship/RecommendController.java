package com.youngo.mobile.controller.friendship;

import com.sun.istack.internal.NotNull;
import com.youngo.core.common.CommonStaticConstant;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.ForceLogin;
import com.youngo.core.model.Login;
import com.youngo.core.model.Pager;
import com.youngo.core.model.user.User;
import com.youngo.core.model.user.UserBrief;
import com.youngo.mobile.controller.location.GoogleLocationService;
import com.youngo.mobile.mapper.friendship.RecommendMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fuchen on 2015/12/11.
 * 语伴推荐，预计这块的算法会越来越复杂，所以提前抽取成一个单独的类
 */
@RestController
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/recommend")
public class RecommendController
{
    @Autowired
    private RecommendMapper recommendMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GoogleLocationService locationService;

    private final Logger logger = LoggerFactory.getLogger(RecommendController.class);

    /**
     * 系统推荐好友<br>
     * 1、已经推荐过了的好友不再重复推荐<br>
     * 2、已经是好友了的用户不再重复推荐<br>
     * 3、已经发起过好友申请的人不再重复推荐<br>
     * 4、曾经是好友，后面被添加了黑名单或者被删除了的用户不再重复推荐<br>
     * 5、尽可能语言匹配<br>
     * 6、用户的好友数目大于一定阀值之后(交友兴趣饱和)，随着好友数目增加，被推荐的权重降低<br>
     * 7、异性推荐权重稍微提升<br>
     * 8、记录推荐情况，后续做数据分析，根据准确率和召回率持续优化推荐算法<br>
     * 9、最近注册的人优先推荐
     *
     * @param page  页码，下标从1开始
     * @param glans 对方擅长的语言,example：en，example：en,ch；多种语言以英文逗号隔开
     * @return 返回第page页的数据
     */
    @Login(forceLogin = ForceLogin.no)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Pager recommend(@RequestParam int page, @RequestParam(required = false) String glans,
                           @RequestParam(required = false) Boolean nsort,
                           @RequestParam(required = false) Boolean isort,
                           @RequestParam(required = false) Boolean gsort,
                           @RequestParam(required = false) String local,
                           HttpServletRequest request)
    {
        Pager pager = new Pager();
        pager.setPage(page);
        int limit = pager.getLimit();
        int offset = pager.getOffset();
        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("offset", offset);
        if (!StringUtils.isEmpty(glans))
        {
            String goodAtLanguages = parseLanguages(glans);
            params.put("glans", goodAtLanguages);
        }
        List<UserBrief> users ;
        String userId = (String) request.getAttribute(CommonStaticConstant.USER_ID);
        if (!StringUtils.isEmpty(userId))
        {
            params.put("userId", userId);
            if(nsort==Boolean.TRUE)
            {
                params.put("limit", limit);
                params.put("offset", offset);
                users = recommendMapper.listNearBy(params);
            }else if(isort==Boolean.TRUE)
            {
                users = recommendMapper.listSameInterest(params);
            }else if(gsort==Boolean.TRUE)
            {
                users = recommendMapper.listOppositeSex(params);
            }else
            {
                users = recommendMapper.list(params);
            }
            User byId = userMapper.getById(userId);
        }else
        {
            users = recommendMapper.list(params);
        }
        if(!StringUtils.isEmpty(local))
        {
            locationService.getLocalName(users,local);
        }
        int count = recommendMapper.count(params);
        pager.setList(users);
        pager.setCount(count);
        return pager;
    }

    /**
     * 用户注册时，推荐三个好友给这个用户
     *
     * @param userId
     */
    public List<UserBrief> recommendForRegeist(String userId)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return recommendMapper.listIntroduction(params);
    }

    /**
     * @param glans 语言列表
     * @return 语言解析，转换成数据库sql in语句可以用到的字符串
     */
    private String parseLanguages(@NotNull String glans)
    {
        glans = glans.replace("'","");
        if (glans.contains(","))
        {
            StringBuilder builder = new StringBuilder();
            String[] split = glans.split(",");
            for (int i = 0; i < split.length; i++)
            {
                if (i != 0)
                {
                    builder.append(",");
                }
                builder.append("'");
                builder.append(split[i]);
                builder.append("'");
            }
            return builder.toString();
        } else
        {
            return "'" + glans + "'";
        }
    }
}
