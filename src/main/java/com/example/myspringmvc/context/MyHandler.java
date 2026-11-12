package com.example.myspringmvc.context;

import java.lang.reflect.Method;

public class MyHandler {
    private String uri;
    private Object controller;
    private Method method;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "MyHandler{" +
                "uri='" + uri + '\'' +
                ", controller=" + controller +
                ", method=" + method +
                '}';
    }
}
