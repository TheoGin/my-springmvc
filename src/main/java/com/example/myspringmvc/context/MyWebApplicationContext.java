package com.example.myspringmvc.context;

import com.example.myspringmvc.xml.XMLPaser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class MyWebApplicationContext {
    private ArrayList<String> classFullPathList = new ArrayList<>();
    private ArrayList<MyHandler> handlerMappingUriList = new ArrayList<>();

    /* public MyWebApplicationContext(String basePackage) {
        System.out.println("MyWebApplicationContext init");
        scanPackage(basePackage);
    } */

    public void init() {
        System.out.println("MyWebApplicationContext init");
        String  basePackage = XMLPaser.getBasePackage("myspringmvc.xml");
        // scanPackage(basePackage);

        // com.example.service.MonsterService, com.example.controller.MonsterController
        String[] packs = basePackage.split(",");
        for (String pack : packs) {
            scanPackage(pack);
        }
        System.out.println("classFullPathList = " + classFullPathList);

        MyHandlerMapping myHandleMapping = new MyHandlerMapping();
        myHandleMapping.initHandlerMapping(classFullPathList, handlerMappingUriList);
        System.out.println("handlerMappingUriList = " + handlerMappingUriList);
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
}
