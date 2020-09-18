package com.zeyigou.sellergoods.service.impl;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.zeyigou.group.Goods;
import com.zeyigou.mapper.*;
import com.zeyigou.pojo.*;
import com.zeyigou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbItemCatMapper tbItemCatMapper;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbGoodsDescMapper tbGoodsDescMapper;
	@Autowired
	private TbBrandMapper tbBrandMapper;
	@Autowired
	private TbSellerMapper sellerMapper;

	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {
		//设置商品状态并添加商品
		goods.getGoods().setAuditStatus("0");
		goodsMapper.insert(goods.getGoods());

		//添加商品信息
//		设置商品信息的id
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
		//添加商品
		tbGoodsDescMapper.insert(goods.getGoodsDesc());


		//添加sku列表
		addSku(goods);
	}

	private void addSku(Goods goods) {
		//获取items
		List<TbItem> items = goods.getItems();
		//遍历添加
		for (TbItem item : items) {
			//设置标题
			item.setTitle(goods.getGoods().getGoodsName());
			//设置分类id
			item.setCategoryid(goods.getGoods().getCategory3Id());
			//设置时间
			item.setCreateTime(new Date());
			item.setUpdateTime(new Date());
			//设置商家id
			item.setSellerId(goods.getGoods().getSellerId());
			item.setGoodsId(goods.getGoods().getId());
			//设置状态值
			item.setStatus("1");
			//设置品牌名称
			Long brandId = goods.getGoods().getBrandId();
			TbBrand tbBrand = tbBrandMapper.selectByPrimaryKey(brandId);
			item.setBrand(tbBrand.getName());

			//设置分类名称
			String name = tbItemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id()).getName();
			item.setCategory(name);

			//获取并设置商家名称
			String name1 = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId()).getName();
			item.setSeller(name1);

			//获取上传图片的列表
			String itemImages = goods.getGoodsDesc().getItemImages();
			List<Map> maps = JSON.parseArray(itemImages, Map.class);
			if (maps != null && maps.size() > 0) {
				String str = maps.get(0).get("url")+"";
				System.out.println("str = " + str);
				item.setImage(str);
			}
			//插入
			itemMapper.insert(item);
		}
	}


	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){
		goodsMapper.updateByPrimaryKey(goods.getGoods());
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id){
		return goodsMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			goodsMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		TbGoodsExample.Criteria criteria = example.createCriteria();
		
		if(goods!=null){			
						if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+goods.getSellerId()+"%");
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void updateStatus(String status, Long[] ids) {
		//循环审核
		for (Long id : ids) {
			//根据逐渐查找id
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			//设置状态
			tbGoods.setAuditStatus(status);
			//修改
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}

}
