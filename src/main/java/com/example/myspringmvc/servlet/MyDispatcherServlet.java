package com.example.myspringmvc.servlet;

import com.example.myspringmvc.annotation.Controller;
import com.example.myspringmvc.annotation.RequestMapping;
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
import java.util.ArrayList;
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
                method.invoke(controller, req, resp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
