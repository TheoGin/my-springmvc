package com.example.myspringmvc.servlet;

import com.example.myspringmvc.annotation.Controller;
import com.example.myspringmvc.annotation.RequestMapping;
import com.example.myspringmvc.annotation.RequestParam;
import com.example.myspringmvc.context.MyHandler;
import com.example.myspringmvc.context.MyHandlerMapping;
import com.example.myspringmvc.context.MyWebApplicationContext;
import com.example.myspringmvc.xml.XMLPaser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyDispatcherServlet extends HttpServlet {
    private ArrayList<MyHandler> handlerMappingUriList = new ArrayList<>();
    private MyWebApplicationContext myWebApplicationContext = null;

    @Override
    public void init() throws ServletException {

        String contextConfigLocation = getServletConfig().getInitParameter("contextConfigLocation");
        // myWebApplicationContext = new MyWebApplicationContext();
        myWebApplicationContext = new MyWebApplicationContext(contextConfigLocation.split(":")[1]);
        myWebApplicationContext.init();
        // initHandlerMapping();
        MyHandlerMapping myHandleMapping = new MyHandlerMapping();
        myHandleMapping.initHandlerMapping(myWebApplicationContext, handlerMappingUriList);
        System.out.println("handlerMappingUriList = " + handlerMappingUriList);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("MyDispatcherServlet doGet");

        executeDispatch(req, resp);
    }

    private void executeDispatch(HttpServletRequest req, HttpServletResponse resp) {
        // System.out.println("MyDispatcherServlet executeDispatch");

        MyHandler myHandler = getMyHandler(req);
        try {
            if (myHandler == null) {
                resp.getWriter().write("<h1>404 Not Found</h1>");
            } else {
                Object controller = myHandler.getController();
                Method method = myHandler.getMethod();

                Class<?>[] parameterTypes = method.getParameterTypes();

                Object[] args = new Object[parameterTypes.length];

                for (int i = 0; i < parameterTypes.length; i++) {
                    // if ("HttpServletRequest".equals(parameterTypes[i].getSimpleName())) {
                    if (HttpServletRequest.class.isAssignableFrom(parameterTypes[i])) {
                        args[i] = req;
                    // } else if ("HttpServletResponse".equals(parameterTypes[i].getSimpleName())) {
                    } else if (HttpServletResponse.class.isAssignableFrom(parameterTypes[i])) {
                        args[i] = resp;
                    }
                }

                Map<String, String[]> parameterMap = req.getParameterMap();
                for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                    String paramName = stringEntry.getKey();
                    String[] paramValues = stringEntry.getValue();
                    int paramIndex = getParamIndex(method, paramName);
                    if (paramIndex != -1) {
                        args[paramIndex] = paramValues[0];
                    } else {
                        // 处理参数不存在的情况
                        List<String> paramNames = getParamNames(method);

                        for (int i = 0; i < paramNames.size(); i++) {
                            if (paramNames.get(i).equals(paramName)) {
                                args[i] = paramValues[0];
                                break;
                            }
                        }
                    }
                }

                // method.invoke(controller, req, resp);
                method.invoke(controller, args);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getParamNames(Method method) {
        List<String> paramNames = new ArrayList<>();
        Parameter[] methodParameters = method.getParameters();
        for (Parameter methodParameter : methodParameters) {
            paramNames.add(methodParameter.getName());
        }
        System.out.println("paramNames = " + paramNames); // 默认情况 paramNames = [arg0, arg1, arg2]，需要添加插件处理 -》paramNames = [req, resp, name]
        return paramNames;
    }

    private int getParamIndex(Method method, String paramName) {
        Parameter[] methodParameters = method.getParameters();
        for (int i = 0; i < methodParameters.length; i++) {
            Parameter methodParameter = methodParameters[i];
            if (methodParameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParamAnnotation = methodParameter.getAnnotation(RequestParam.class);
                String requestParamValue = requestParamAnnotation.value();
                if (requestParamValue.equals(paramName)) {
                    return i;
                }
            }
        }

        return -1;
    }

    private MyHandler getMyHandler(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        // System.out.println("requestURI = " + requestURI); // /my_springmvc/aaa

        String contextPath = req.getContextPath();
        if (contextPath.equals("/")) {
            contextPath = "";
        }

        requestURI = requestURI.replace(contextPath, "");
        // System.out.println("after requestURI = " + requestURI);

        for (MyHandler myHandler : handlerMappingUriList) {
            if (myHandler.getUri().equals(requestURI)) {
                return myHandler;
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("MyDispatcherServlet doPost");
    }

    /* public void initHandlerMapping() {
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
    } */
}
