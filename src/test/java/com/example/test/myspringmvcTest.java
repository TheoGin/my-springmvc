package com.example.test;

import com.example.myspringmvc.xml.XMLPaser;
import org.junit.Test;

public class myspringmvcTest {
    @Test
    public void testGetBasePackage() {
        String basePackage = XMLPaser.getBasePackage("myspringmvc.xml");
        System.out.println(basePackage);
    }
}
