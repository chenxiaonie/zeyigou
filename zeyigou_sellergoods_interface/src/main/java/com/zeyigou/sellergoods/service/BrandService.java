package com.zeyigou.sellergoods.service;

import com.zeyigou.pojo.PageResult;
import com.zeyigou.pojo.Result;
import com.zeyigou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandService {
    List<TbBrand> findAll();


    PageResult search(int page, int size, TbBrand brand);

    Result add(TbBrand brand);

    Result update(TbBrand brand);

    Result delete(Long[] selectIds);

    List<Map> findBrandList();
}
