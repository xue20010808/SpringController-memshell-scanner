package controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MappingTest  {


    @ResponseBody
    @RequestMapping(value = "/mappings",method = RequestMethod.GET)
    public  String mapping() throws Exception{

        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes()
                .getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
        RequestMappingHandlerMapping rmhMapping = context.getBean(RequestMappingHandlerMapping.class);
        Field _mappingRegistry = AbstractHandlerMethodMapping.class.getDeclaredField("mappingRegistry");
        _mappingRegistry.setAccessible(true);
        Object mappingRegistry = _mappingRegistry.get(rmhMapping);

        Field _registry = mappingRegistry.getClass().getDeclaredField("registry");
        _registry.setAccessible(true);
        HashMap<Object,Object> registry = (HashMap<Object, Object>)  _registry.get(mappingRegistry);
        Class<?> [] tempArray = AbstractHandlerMethodMapping.class.getDeclaredClasses();
        Class<?> mappingRegistrationClazz = null ;
        for (Class<?> item:tempArray) {
            if(item.getName().equals("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping$MappingRegistration")){
                mappingRegistrationClazz = item;

            }



        }
        StringBuilder sb = new StringBuilder();
        sb.append("<pre>");
        sb.append("| path |").append("\t").append("\t").append("| info |").append("\n");
        for(Map.Entry<Object,Object> entry:registry.entrySet()){
            sb.append("--------------------------------------------");
            sb.append("\n");
            RequestMappingInfo key = (RequestMappingInfo)  entry.getKey();
            List<String> tempList = new ArrayList<>(key.getPatternsCondition().getPatterns());
            sb.append(tempList.get(0)).append("\t").append("-->").append("\t");
            Field _handlerMethod= mappingRegistrationClazz.getDeclaredField("handlerMethod");
            _handlerMethod.setAccessible(true);
            HandlerMethod handlerMethod = (HandlerMethod) _handlerMethod.get(entry.getValue());
            Class<?> beanType = handlerMethod.getBeanType();
            String beanname = beanType.getName();
            Method method = handlerMethod.getMethod();
            String methodname=method.getName();
            sb.append(beanname).append("$").append(methodname);
            sb.append("\n");

        }

        sb.append("</pre>");




    return sb.toString();




    }









}