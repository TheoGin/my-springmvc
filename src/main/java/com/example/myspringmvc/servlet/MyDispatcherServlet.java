package com.example.myspringmvc.servlet;

import com.example.myspringmvc.context.MyWebApplicationContext;
import com.example.myspringmvc.xml.XMLPaser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyDispatcherServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {

        MyWebApplicationContext myWebApplicationContext = new MyWebApplicationContext();
        myWebApplicationContext.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("MyDispatcherServlet doGet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("MyDispatcherServlet doPost");
    }
}
