package com.zeyigou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zeyigou.pojo.PageResult;
import com.zeyigou.pojo.Result;
import com.zeyigou.pojo.TbBrand;
import com.zeyigou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("brand")
public class BrandController {
    @Reference
    private BrandService brandService;

    @RequestMapping("list")
    public List<TbBrand> list(){
        System.out.println("\"list\" = " + "list");
        return brandService.findAll();
    }

    //条件查询带分页
    @RequestMapping("search")
    public PageResult search(int page, int size, @RequestBody(required = false)TbBrand brand ){
        return  brandService.search(page, size, brand);
    }

    //添加
    @RequestMapping("add")
    public Result add(@RequestBody TbBrand brand){
        return brandService.add(brand);
    }
    //添加
    @RequestMapping("update")
    public Result update(@RequestBody TbBrand brand){
        return brandService.update(brand);
    }

    //删除
    @RequestMapping("delete")
    public Result delete(Long[] ids){
        return brandService.delete(ids);
    }
    //查询品牌
    @RequestMapping("findBrandList")
    public List<Map> findBrandList(){
        return brandService.findBrandList();
    }
}

