package com.zeyigou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.zeyigou.group.Specification;
import com.zeyigou.mapper.TbSpecificationMapper;
import com.zeyigou.mapper.TbSpecificationOptionMapper;
import com.zeyigou.pojo.*;
import com.zeyigou.sellergoods.service.SpecificationService;
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
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	@Autowired
	private TbSpecificationOptionMapper optionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		//添加规格
		specificationMapper.insert(specification.getSpec());
		//添加规格信息
		for (TbSpecificationOption option : specification.getSpecificationOptionList()) {
			//设置规格外键关联的id
			option.setSpecId(specification.getSpec().getId());
			//插入表中
			optionMapper.insert(option);
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		//修改规格表
		specificationMapper.updateByPrimaryKey(specification.getSpec());

		//修改规格品牌表
		//首先根据外键删除规格品牌的数据
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		//获取条件对象
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		//设置条件
		criteria.andSpecIdEqualTo(specification.getSpec().getId());
		optionMapper.deleteByExample(example);
		//进行添加
		for (TbSpecificationOption op : specification.getSpecificationOptionList()) {
			//绑定外键
			op.setSpecId(specification.getSpec().getId());
			//进行添加
			optionMapper.insert(op);
		}
	}
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		//根据id查出规格对象
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);

		//2.根据id查询规格选项
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		//获取条件对象
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		//设置条件
		criteria.andSpecIdEqualTo(id);
		//进行查询
		List<TbSpecificationOption> tbSpecificationOptions = optionMapper.selectByExample(example);
		System.out.println("tbSpecificationOptions = " + tbSpecificationOptions);
		//返回
		return new Specification(tbSpecification,tbSpecificationOptions);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			specificationMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		TbSpecificationExample.Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> findSpecList() {
		return specificationMapper.findSpecList();
	}

}
