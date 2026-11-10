package com.example.myspringmvc.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.net.URL;

public class XMLPaser {
    public static String getBasePackage() {

        try {
            InputStream resourceAsStream = XMLPaser.class.getClassLoader().getResourceAsStream("myspringmvc.xml");
            // System.out.println("resourceAsStream = " + resourceAsStream);
            // URL url = XMLPaser.class.getResource("/myspringmvc.xml");
            // System.out.println(url);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(resourceAsStream);
            // Document document = saxReader.read(url.getFile());
            Element rootElement = document.getRootElement();
            Element componentScanElement = rootElement.element("component-scan");
            String basePackageValue = componentScanElement.attributeValue("base-package");
            return basePackageValue;
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
}
