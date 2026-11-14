package com.example.myspringmvc.context;

import com.example.myspringmvc.annotation.Controller;
import com.example.myspringmvc.annotation.RequestMapping;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyHandlerMapping {
    public void initHandlerMapping(MyWebApplicationContext myWebApplicationContext, ArrayList<MyHandler> handlerMappingUriList) {
        ConcurrentHashMap<String, Object> ioc = myWebApplicationContext.getIoc();
        if (ioc.isEmpty()) {
            throw new RuntimeException("spring ioc 容器为空");
        }
        try {
            for (Map.Entry<String, Object> entry : ioc.entrySet()) {
                Class<?> clazz = entry.getValue().getClass();
                if (clazz.isAnnotationPresent(Controller.class)) {
                    for (Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMappingAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
                            String methodUri = requestMappingAnnotation.value();
                            MyHandler myHandler = new MyHandler();
                            myHandler.setUri(methodUri);
                            myHandler.setController(entry.getValue());
                            myHandler.setMethod(method);
                            handlerMappingUriList.add(myHandler);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
