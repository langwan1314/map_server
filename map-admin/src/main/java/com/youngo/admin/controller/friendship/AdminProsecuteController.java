package com.youngo.admin.controller.friendship;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngo.admin.common.exception.RestException;
import com.youngo.admin.controller.chat.MessageService;
import com.youngo.admin.controller.push.ClientKickUserHandler;
import com.youngo.admin.controller.push.ClientUserWarningHandler;
import com.youngo.admin.grid.JQGrid;
import com.youngo.admin.grid.SearchFilter;
import com.youngo.admin.grid.SearchRule;
import com.youngo.admin.grid.enumeration.GroupEnum;
import com.youngo.admin.grid.enumeration.OperEnum;
import com.youngo.admin.mapper.chat.AdminChatMapper;
import com.youngo.admin.mapper.friendship.AdminProsecuteMapper;
import com.youngo.admin.model.Paginator;
import com.youngo.admin.model.chat.AdminChatInfo;
import com.youngo.admin.model.chat.AdminChatUser;
import com.youngo.admin.model.friendship.AdminProseCute;
import com.youngo.core.mapper.user.UserMapper;
import com.youngo.core.model.Result;
import com.youngo.core.model.msg.MessageEntity;
import com.youngo.core.model.user.User;
import com.youngo.ssdb.core.SsdbConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by 浮沉 on 2016/3/15.
 * 投诉管理
 */
@Controller
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/prosecute")
public class AdminProsecuteController
{
    @Autowired
    private AdminProsecuteMapper adminProsecuteMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AdminChatMapper adminChatMapper;
    @Autowired
    private UserMapper userMapper;


    protected static final Logger logger = LoggerFactory.getLogger(AdminProsecuteController.class);

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model)
    {
        return "prosecute/list";
    }

    @ResponseBody
    @RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces = "application/json")
    public JQGrid<AdminProseCute> getGrid(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer rows,
                                          @RequestParam(value = "sidx", required = false) String sortBy, @RequestParam(value = "sord", required = false) String order,
                                          @RequestParam(value = "_search", required = false) boolean search, @RequestParam(value = "filters", required = false) String filters)
    {
        String whereClause = buildWhereCause(filters,search);

        Map<String, Object> param = new HashMap<>();
        param.put("whereClause", whereClause);

        int totalRecords = adminProsecuteMapper.count(param);

        // 获取列表
        Paginator paginator = new Paginator(page, rows, totalRecords);
        param.put("rowNum", paginator.getOffset());
        param.put("rowCount", paginator.getLimit());
        if(StringUtils.isEmpty(sortBy))
        {
            param.put("sortBy", "prosecute.createtime");
            param.put("order", "DESC");
        }else
        {
            param.put("sortBy", sortBy);
            param.put("order", order);
        }

        List<AdminProseCute> versionList = adminProsecuteMapper.list(param);
        JQGrid<AdminProseCute> versionGrid = new JQGrid<>();
        versionGrid.setData(versionList);
        return versionGrid;
    }

    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public String getAudit(Model model)
    {
        return "prosecute/audit";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/update", produces = "application/json")
    public Result update(@RequestBody Map<String, String> params) {
        Result result = new Result();

        String status = params.get("status");
        String id = params.get("id");
        if(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(status) && !"0".equals(status))
        {
            AdminProseCute proseCute = adminProsecuteMapper.get(id);
            if(proseCute!=null && "0".equals(proseCute.getStatus()))
            {
                adminProsecuteMapper.update(params);//TODO 需要把用户设置为不可用
                if("3".equals(status))
                {
                    String accusedId = proseCute.getAccusedId();
                    User user = new User();
                    user.setStatus("disable");
                    user.setId(accusedId);
                    userMapper.updateByUserId(user);
                    ClientKickUserHandler.fireUseerDisable(accusedId);
                }else if("2".equals(status))
                {
                    ClientUserWarningHandler.fireUserWarning(proseCute.getAccusedId());
                }
            }
        }
        result.setCode(200);
        result.setMsg("success");
        result.setData("success");
        return result;
    }


    /**
     * @param id 会话ID
     * @return 根据会话ID获取最新的消息
     */
    @ResponseBody
    @RequestMapping(value = "/getbs", method = RequestMethod.GET)
    public Result getMessageBySession(@RequestParam String id)
    {
        Result rs = new Result();
        rs.setCode(200);
        rs.setMsg("success");

        AdminProseCute proseCute = adminProsecuteMapper.get(id);
        if(proseCute!=null)
        {
            String prosecutorId = proseCute.getProsecutorId();//举报人
            String accusedId = proseCute.getAccusedId();//被举报人
            String sessionId = SsdbConstants.Chat.chatSessionPrefix + accusedId + ":" + prosecutorId;
            List<MessageEntity> messages = messageService.getMessagesBySessionId(sessionId);
            List<AdminChatInfo> translate = translate(messages,accusedId);
            rs.setData(translate);
        }else
        {
            rs.setData(new ArrayList<>());
        }
        return rs;
    }

    /**
     * @param id 会话ID
     * @return 根据会话ID获取最新的消息
     */
    @ResponseBody
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public Result getAllMessages(@RequestParam String id)
    {
        Result rs = new Result();
        rs.setCode(200);
        rs.setMsg("success");

        AdminProseCute proseCute = adminProsecuteMapper.get(id);
        if(proseCute!=null)
        {
            String accusedId = proseCute.getAccusedId();
            List<MessageEntity> messages = messageService.getMessages(accusedId);
            List<AdminChatInfo> translate = translate(messages,accusedId);
            rs.setData(translate);
        }else
        {
            rs.setData(new ArrayList<>());
        }
        return rs;
    }

    private List<AdminChatInfo> translate(List<MessageEntity> messageEntities,String sessionId)
    {
        Map<String,AdminChatUser> usercache = new HashMap<>();
        List<AdminChatInfo> chatInfos = new LinkedList<>();
        if(messageEntities!=null && !messageEntities.isEmpty())
        {
            for(int i=messageEntities.size()-1;i>=0;i--)
            {
                MessageEntity entity = messageEntities.get(i);
                AdminChatInfo chatInfo = new AdminChatInfo();
                String fromId = String.valueOf(entity.getFromId());
                AdminChatUser chatUser = usercache.get(fromId);
                if(chatUser==null)
                {
                    chatUser = adminChatMapper.getChatUser(fromId);
                    usercache.put(fromId,chatUser);
                }
                chatInfo.setId(String.valueOf(entity.getId()));
                chatInfo.setContent(entity.getContent());
                chatInfo.setCreatetime(new Date(entity.getCreated()*1000));
                chatInfo.setSender(chatUser);
                chatInfo.setSeq(String.valueOf(entity.getId()));
                chatInfo.setSession_id(sessionId);
                chatInfo.setType(entity.getMsgType());
                chatInfos.add(chatInfo);
            }
        }
        return chatInfos;
    }

    private String buildWhereCause(String filters,boolean search)
    {
        String whereClause = "";
        if (search && !StringUtils.isEmpty(filters))
        {
            ObjectMapper mapper = new ObjectMapper();
            try
            {
                SearchFilter searchFilter = mapper.readValue(filters, SearchFilter.class);
                if (searchFilter != null && searchFilter.getGroupOp() != null && searchFilter.getRules() != null)
                {
                    whereClause = buildWhereClause(searchFilter);
                }
            } catch (Exception e)
            {
                logger.debug(e.getMessage());
                throw new RestException(HttpStatus.BAD_REQUEST.value(), "无法解析搜索参数");
            }
        }else
        {
            whereClause = "WHERE prosecute.status = 0";
        }
        return whereClause;
    }

    private String buildWhereClause(SearchFilter searchFilter)
    {
        String result = "";

        if (searchFilter.getGroupOp().equals(GroupEnum.AND.getDescription()) && searchFilter.getRules().size() > 0)
        {
            for (SearchRule rule : searchFilter.getRules())
            {
                if (rule.getField().equals("status"))
                {
                    if (OperEnum.getEnum(rule.getOp()) == OperEnum.EQUAL)
                    {
                        result += " and prosecute.status = '" + rule.getData() + "'";
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(result))
        {
            result = result.replaceFirst("and", "where");
        }
        return result;
    }

}
