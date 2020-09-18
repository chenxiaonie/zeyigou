package com.zeyigou.sellergoods.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sun.tools.javac.util.StringUtils;
import com.zeyigou.mapper.TbBrandMapper;
import com.zeyigou.pojo.PageResult;
import com.zeyigou.pojo.Result;
import com.zeyigou.pojo.TbBrand;
import com.zeyigou.pojo.TbBrandExample;
import com.zeyigou.sellergoods.service.BrandService;
import org.apache.solr.common.util.StrUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper tbBrandMapper;
    @Override
    public List<TbBrand> findAll() {
        return tbBrandMapper.selectByExample(null);
    }
    //分页查询
    @Override
    public PageResult search(int page, int size, TbBrand brand) {
        //开始分页
        PageHelper.startPage(page,size);
        //开始查询
        TbBrandExample example = new TbBrandExample();
        //获取查询条件
        TbBrandExample.Criteria criteria = example.createCriteria();
        //设置条件
        if (brand != null){
            if (StrUtil.isNotBlank(brand.getName())) {
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (StrUtil.isNotBlank(brand.getFirstChar())) {
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }

        List<TbBrand> tbBrands = tbBrandMapper.selectByExample(example);
        //转换为Page对象
        Page<TbBrand> tbBrandPage = (Page<TbBrand>) tbBrands;
        //返回
        return new PageResult(tbBrandPage.getTotal(),tbBrandPage.getResult());
    }
    //添加品牌
    @Override
    public Result add(TbBrand brand) {
        try{
            tbBrandMapper.insert(brand);
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false,"添加失败");
        }
        return  new Result(true,"添加成功");
    }

    //x更新品牌
    @Override
    public Result update(TbBrand brand) {
        try{
            tbBrandMapper.updateByPrimaryKey(brand);
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false,"更新失败");
        }
        return  new Result(true,"更新成功");
    }
//删除
    @Override
    public Result delete(Long[] selectIds) {
        try{
            for (Long id : selectIds) {
                tbBrandMapper.deleteByPrimaryKey(id);
            }
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false,"删除失败");
        }
        return  new Result(true,"删除成功");

    }

    @Override
    public List<Map> findBrandList() {
        return tbBrandMapper.findBrandList();
    }
}
