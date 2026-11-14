package com.example.myspringmvc.context;

import com.example.myspringmvc.annotation.Controller;
import com.example.myspringmvc.xml.XMLPaser;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MyWebApplicationContext {
    private ArrayList<String> classFullPathList = new ArrayList<>();
    private ConcurrentHashMap<String, Object> ioc = new ConcurrentHashMap<>();
    private String springMvcXmlFileName;

    /* public MyWebApplicationContext(String basePackage) {
        System.out.println("MyWebApplicationContext init");
        scanPackage(basePackage);
    } */

    public MyWebApplicationContext() {
    }

    public MyWebApplicationContext(String springMvcXmlFileName) {
        this.springMvcXmlFileName = springMvcXmlFileName;
    }

    public void init() {
        System.out.println("MyWebApplicationContext init");
        // String  basePackage = XMLPaser.getBasePackage("myspringmvc.xml");
        String  basePackage = XMLPaser.getBasePackage(springMvcXmlFileName);
        // scanPackage(basePackage);

        // com.example.service.MonsterService, com.example.controller.MonsterController
        String[] packs = basePackage.split(",");
        for (String pack : packs) {
            scanPackage(pack);
        }
        System.out.println("classFullPathList = " + classFullPathList);

        executeInstaneToInitIoc();
        System.out.println("ioc = " + ioc);

    }

    private void executeInstaneToInitIoc() {
        for (String classFullPath : classFullPathList) {
            try {
                Class<?> clazz = Class.forName(classFullPath);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String className = StringUtils.uncapitalize(clazz.getSimpleName());
                    Controller controllerAnnotation = clazz.getDeclaredAnnotation(Controller.class);
                    if (!controllerAnnotation.value().isEmpty()) {
                        className = controllerAnnotation.value();
                    }
                    ioc.put(className, clazz.newInstance());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void scanPackage(String pack) {
        URL url = MyWebApplicationContext.class.getResource("/" + pack.replace('.', '/'));
        if (url == null) {
            throw new RuntimeException("package not found: " + pack);
        }
        File file = new File(url.getFile());
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                if (f.isDirectory()) {
                    scanPackage(pack + "." + f.getName());
                } else {
                    classFullPathList.add(pack + "." + f.getName().replace(".class", ""));
                }
            }
        }
    }

    public ConcurrentHashMap<String, Object> getIoc() {
        return ioc;
    }
}
