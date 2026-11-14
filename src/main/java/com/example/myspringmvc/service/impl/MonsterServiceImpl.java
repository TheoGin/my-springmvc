package com.example.myspringmvc.service.impl;

import com.example.myspringmvc.annotation.Service;
import com.example.myspringmvc.entity.Monster;
import com.example.myspringmvc.service.MonsterService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// @Service("monsterServ")
@Service
public class MonsterServiceImpl implements MonsterService {
    @Override
    public List<Monster> listMonsters() {
        ArrayList<Monster> monsters = new ArrayList<>();
        monsters.add(new Monster(100, "牛魔王", "芭蕉扇", 500));
        monsters.add(new Monster(200, "蜘蛛精", "吐口水", 200));

        return monsters;
    }
}
