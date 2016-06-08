package com.youngo.admin.controller.version;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.youngo.admin.common.exception.RestException;
import com.youngo.admin.grid.JQGrid;
import com.youngo.admin.grid.SearchFilter;
import com.youngo.admin.grid.SearchRule;
import com.youngo.admin.grid.enumeration.GroupEnum;
import com.youngo.admin.grid.enumeration.OperEnum;
import com.youngo.core.mapper.version.VersionMapper;
import com.youngo.core.model.Result;
import com.youngo.core.model.version.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@ResponseStatus(HttpStatus.OK)
@RequestMapping(value = "/version")
public class AdminVersionController
{
    @Autowired
    private VersionMapper mapper;
    protected static final Logger logger = LoggerFactory.getLogger(AdminVersionController.class);

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model)
    {
        return "version/list";
    }

    @ResponseBody
    @RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces = "application/json")
    public JQGrid<Version> getVersionGrid(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer rows,
                                          @RequestParam(value = "sidx", required = false) String sortBy, @RequestParam(value = "sord", required = false) String order,
                                          @RequestParam(value = "_search", required = false) boolean search, @RequestParam(value = "filters", required = false) String filters)
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
        }

        Map<String, Object> param = new HashMap<>();
        param.put("whereClause", whereClause);
        System.out.println("**************************whereClause=" + whereClause + "******************");

        List<Version> versionList = mapper.list(param);
        JQGrid<Version> versionGrid = new JQGrid<>();
        versionGrid.setData(versionList);
        return versionGrid;
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public String getUpdate(Model model)
    {
        logger.debug("*************** version update begin *******************");
        String clientType1 = "ios";
        String clientType2 = "android";
        List<Version> versions = mapper.get(clientType1);//获取所有版本号

        Map<String, Object> latestVersion = new HashMap<>();
        if (versions != null && versions.size() > 0)
        {
            logger.debug("*****************" + clientType1 + "  latestVersion :" + versions.get(versions.size() - 1).getVersionName() + "**********");
            latestVersion.put(clientType1, versions.get(versions.size() - 1).getVersionName());
        }
        versions = mapper.get(clientType2);
        if (versions != null && versions.size() > 0)
        {
            logger.debug("*****************" + clientType2 + "  latestVersion :" + versions.get(versions.size() - 1).getVersionName() + "**********");
            latestVersion.put(clientType2, versions.get(versions.size() - 1).getVersionName());
        }

        model.addAttribute("latestVersion", latestVersion);
        Version version = new Version();
        model.addAttribute("version", version);
        logger.debug("*************** version update end *******************");
        return "version/update";
    }

    @ResponseBody
    @Transactional(propagation = Propagation.REQUIRED)
    @RequestMapping(method = RequestMethod.POST, value = "/update", produces = "application/json")
    public Result update(@RequestBody Version version)
    {
        logger.debug("*********************begin update version post**********************");
        Result result = new Result();

        String versionNumber = version.getVersionNumber();
        String clientType = version.getClientType();
        String versionName = version.getVersionName();
        boolean forcibly = version.isForcibly();
        String url = version.getUrl();
        String description = version.getDescription();
        logger.debug("******version_number=" + versionNumber + ",forcibly=" + forcibly + ",version_name=" + versionName +
                ",client_type=" + clientType + ",url=" + url + ",description=" + description + "******");
        if (StringUtils.isEmpty(versionNumber) || StringUtils.isEmpty(versionName) || StringUtils.isEmpty(clientType) || StringUtils.isEmpty(url) || StringUtils.isEmpty(description))
        {
            throw new RestException(HttpStatus.BAD_REQUEST.value(), "缺少参数");
        }
        int resultNum = mapper.insert(version);

        result.setCode(200);
        result.setMsg("success");
        result.setData(resultNum);
        logger.debug("*********************end update version post**********************");
        return result;
    }


    private String buildWhereClause(SearchFilter searchFilter)
    {
        String result = "";

        if (searchFilter.getGroupOp().equals(GroupEnum.AND.getDescription()) && searchFilter.getRules().size() > 0)
        {
            for (SearchRule rule : searchFilter.getRules())
            {
                // 商品名称
                if (rule.getField().equals("clientType"))
                {
                    if (OperEnum.getEnum(rule.getOp()) == OperEnum.EQUAL)
                    {
                        result += " and clientType = '" + rule.getData() + "'";
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
