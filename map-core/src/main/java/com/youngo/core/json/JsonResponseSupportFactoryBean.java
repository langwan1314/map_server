package com.youngo.core.json;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

/**
 * Modified Spring internal Return value handlers, and wires up a decorator to
 * add support for @JsonResponseView
 * 
 * @author Jack Matthews
 * 
 */
final class JsonResponseSupportFactoryBean implements InitializingBean {

//    Logger log = LoggerFactory.getLogger(JsonResponseSupportFactoryBean.class);

    @Autowired
    private RequestMappingHandlerAdapter adapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        // Spring 4 way
    	List<HandlerMethodReturnValueHandler> handlers = this.adapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> copy = new ArrayList<HandlerMethodReturnValueHandler>(handlers);

        decorateHandlers(copy);
        this.adapter.setReturnValueHandlers(copy);
    }

    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                JsonResponseInjectingReturnValueHandler decorator = new JsonResponseInjectingReturnValueHandler(handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
//                this.log.info("JsonResponse decorator support wired up");
                break;
            }
        }
    }

}