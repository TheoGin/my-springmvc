package com.example.myspringmvc.context;

import com.example.myspringmvc.annotation.Controller;
import com.example.myspringmvc.annotation.RequestMapping;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;

public class MyHandlerMapping {
    public void initHandlerMapping(ArrayList<String> classFullPathList, ArrayList<MyHandler> handlerMappingUriList) {
        for (String classFullPath : classFullPathList) {
            try {
                Class<?> clazz = Class.forName(classFullPath);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    MyHandler myHandler = new MyHandler();
                    /* String className = StringUtils.uncapitalize(clazz.getSimpleName());
                    Controller controllerAnnotation = clazz.getDeclaredAnnotation(Controller.class);
                    if (!controllerAnnotation.value().isEmpty()) {
                        className = controllerAnnotation.value();
                    }
                    myHandler.setUri("/" + className); */
                    myHandler.setController(clazz.newInstance());

                    for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping requestMappingAnnotation = method.getDeclaredAnnotation(RequestMapping.class);
                            String methodUri = requestMappingAnnotation.value();
                            myHandler.setUri(methodUri);
                            myHandler.setMethod(method);
                        }
                    }

                    handlerMappingUriList.add(myHandler);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
