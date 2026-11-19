package com.example.myspringmvc.controller;

import com.example.myspringmvc.annotation.*;
import com.example.myspringmvc.entity.Monster;
import com.example.myspringmvc.service.MonsterService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

// @Controller(value = "monster")
@Controller
public class MonsterController {

    // @AutoWired(value = "monsterServ")
    @AutoWired
    private MonsterService monsterService;


    @RequestMapping(value = "/list/monsters/json")
    @ResponseBody
    public List<Monster> listMonstersJson(HttpServletRequest req, HttpServletResponse resp) {

        List<Monster> monsters = monsterService.listMonsters();

        return monsters;
    }

    @RequestMapping(value = "/list/monsters")
    public void listMonsters(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html;charset=utf-8");

        List<Monster> monsters = monsterService.listMonsters();
        try {
            PrintWriter writer = resp.getWriter();
            writer.write("<h1>妖怪列表</h1>");
            // 表格拼接
            writer.write("<table width='500px' border='1' style='border-collapse: collapse' border='1px'>");
            for (Monster monster : monsters) {
                writer.write("<tr>");
                writer.write("<td>" + monster.getId() + "</td>");
                writer.write("<td>" + monster.getName() + "</td>");
                writer.write("<td>" + monster.getSkill() + "</td>");
                writer.write("<td>" + monster.getAge() + "</td>");
                writer.write("</tr>");
            }
            writer.write("</table>");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/list/find")
    // public void findMonstersByName(HttpServletRequest req, HttpServletResponse resp, @RequestParam(value = "name") String name) {
    public void findMonstersByName(HttpServletRequest req, HttpServletResponse resp, String name) {
        resp.setContentType("text/html;charset=utf-8");
        System.out.println("name = " + name);

        List<Monster> monsters = monsterService.findMonstersByName(name);
        try {
            PrintWriter writer = resp.getWriter();
            writer.write("<h1>通过名称查询妖怪列表</h1>");
            // 表格拼接
            writer.write("<table width='500px' border='1' style='border-collapse: collapse' border='1px'>");
            for (Monster monster : monsters) {
                // 可以用 StringBuffer 来拼接
                writer.write("<tr>");
                writer.write("<td>" + monster.getId() + "</td>");
                writer.write("<td>" + monster.getName() + "</td>");
                writer.write("<td>" + monster.getSkill() + "</td>");
                writer.write("<td>" + monster.getAge() + "</td>");
                writer.write("</tr>");
            }
            writer.write("</table>");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/monster/login")
    public String login(HttpServletRequest req, HttpServletResponse resp, String name) {
        boolean logined = monsterService.login(name);
        req.setAttribute("monsterName", name);
        System.out.println("name = " + name);
        if (logined) {
            // return "redirect:/login_ok.jsp";
            // return "forward:/login_ok.jsp";
            return "/login_ok.jsp";
        } else {
            return "forward:/login_error.jsp";
        }
    }
}
