package com.zeyigou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class indexController {

    @RequestMapping("findName")
    public Map<String,String> findName(){
        //获取用户名
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        //定义map返回
        Map<String,String> map = new HashMap<>();
        map.put("name",name);
        return map;
    }
}
