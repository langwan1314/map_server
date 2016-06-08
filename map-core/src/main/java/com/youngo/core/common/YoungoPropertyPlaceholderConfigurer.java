package com.youngo.core.common;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.ServletContextResource;

import java.lang.reflect.Field;

/**
 * 有些参数，如数据库连接，图片服务器地址等，需要随环境的不同而不同<br>
 * Dtarget.env属性的作用为用来识别环境的类型（开发环境、测试环境、正式环境)<br>
 * 如果jvm参数中没有Dtarget.env属性，则默认Dtarget.env属性值为dev<br>
 * 否则，Dtarget.env以jvm参数中的值为准<br>
 */
public class YoungoPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer
{
    @Override
    public void setLocations(Resource... locations)
    {
        String property = System.getProperty("target.env");
        if (StringUtils.isEmpty(property))
        {
            for (int i = 0; i < locations.length; i++)
            {
                Resource location = locations[i];
                if(location instanceof ServletContextResource)
                {
                    ServletContextResource resource = (ServletContextResource)location;
                    String path = resource.getPath();
                    path = path.replace("${target.env}", "dev");
                    locations[i] = new ServletContextResource(resource.getServletContext(), path);
                }else if(location instanceof ClassPathResource)
                {
                    ClassPathResource resource = (ClassPathResource)location;
                    String path = resource.getPath();
                    path = path.replace("${target.env}", "dev");
                    locations[i] = new ClassPathResource(path,resource.getClassLoader());
                }
            }
        }
        super.setLocations(locations);
    }

}
