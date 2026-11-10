package com.example.myspringmvc.controller;

import com.example.myspringmvc.annotation.Controller;
import com.example.myspringmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
public class MonsterController {

    @RequestMapping(value = "/listMonsters")
    public void listMonsters(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html;charset=utf-8");
        try {
            PrintWriter writer = resp.getWriter();
            writer.write("妖怪列表");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
