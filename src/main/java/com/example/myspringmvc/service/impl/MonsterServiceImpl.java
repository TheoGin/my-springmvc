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

    @Override
    public List<Monster> findMonstersByName(String name) {

        ArrayList<Monster> monsters = new ArrayList<>();
        monsters.add(new Monster(100, "牛魔王", "芭蕉扇", 500));
        monsters.add(new Monster(200, "蜘蛛精", "吐口水", 200));
        monsters.add(new Monster(300, "白骨精", "咬人", 300));
        monsters.add(new Monster(400, "小红帽", "跑", 200));
        monsters.add(new Monster(500, "狼人", "咬人", 400));

        ArrayList<Monster> resultMonsters = new ArrayList<>();

        for (Monster monster : monsters) {
            if (monster.getName().contains(name)) {
                resultMonsters.add(monster);
            }
        }

        return resultMonsters;
    }
}
