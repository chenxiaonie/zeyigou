 //控制层 
app.controller('goodsController' ,function($scope,$controller ,itemCatService,typeTemplateService,goodsService){
	
	$controller('baseController',{$scope:$scope});//继承
	//0.初始化组合对象
	$scope.entity = {goods:{},goodsDesc:{itemImages:[],specificationItems:[]},items:[]}
	//上传文件
	$scope.updateFile=()=>{
		goodsService.updateFile().success(response=>{
			if (response.success){
				$scope.imageEntity.url = response.message
			}else {
				alert(response.message)
			}
		})
	}
	$scope.addImage=()=>{
			$scope.entity.goodsDesc.itemImages.push($scope.imageEntity)
	}
	//1.根据父id查询子分类列表
	$scope.findByParentId=(parentId)=>{
		itemCatService.findByParentId(parentId).success(response=>{
			$scope.category1List = response;
		})
	}
	//2根据一级分类id查二级分类id:
	$scope.$watch("entity.goods.category1Id",(newValue,oldValue)=>{
		//查询二级分类id
		itemCatService.findByParentId(newValue).success(response=>{
			$scope.category2List = response
		})
	})
//3根据二级级分类id查三级分类id:
	$scope.$watch("entity.goods.category2Id",(newValue,oldValue)=>{
		//查询二级分类id
		itemCatService.findByParentId(newValue).success(response=>{
			$scope.category3List = response
		})
	})
	//4根据三级id查询模板id
	$scope.$watch("entity.goods.category3Id",(newValue,oldValue)=>{
		itemCatService.findOne(newValue).success(response=>{
			$scope.entity.goods.typeTemplateId = response.typeId
		})
	})
//5.视频模板id的变化会得到不同的品牌列表
	$scope.$watch("entity.goods.typeTemplateId",(newValue,oldValue)=>{
		typeTemplateService.findOne(newValue).success(response=>{
			//根据末班id查询品牌列表
			$scope.brandList =JSON.parse(response.brandIds)
			//查询自定义扩展属性
			$scope.entity.goodsDesc.customAttributeItems = JSON.parse(response.customAttributeItems)
			//根据模板对象查询规格列表
			typeTemplateService.findSpecList(newValue).success(response=>{
				$scope.specList = response
			})
		})
	})
	//6.用户选择某个规格选项会进行数据的变化
	$scope.updateSpec=(event,name,value)=>{
		//在goodDesc表中的specificationItems查询是否有指定的对象
		let obj = searchByKey($scope.entity.goodsDesc.specificationItems,"attributeName",name)
		//如果obj没有值,表示某个规格机器选项是第一次添加$scope,entity.specificationItems
		if (!obj){
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]})
		}else {
			if (event.target.checked){//如果被选中
				obj.attributeValue.push(value)
			}else {
				//当前未被复选时,从中删除此元素
				let index = obj.attributeValue.indexOf(value)
				//删除选项
				obj.attributeValue.splice(index,1)
				//判断此元素的个数是否为0,若为0 ,则表示一个元素也没有,此时就要当前的attributeValue
				if (obj.attributeValue.length == 0){
					let index2 = $scope.entity.goodsDesc.specificationItems.indexOf(obj)
					//删除
					$scope.entity.goodsDesc.specificationItems.splice(index2,1)
				}
			}
		}
		createSkuList();
	}

	//7.通过数组的key和名查询对象
		searchByKey=(list,key,value)=>{
			for (let  i=0,len = list.length;i < len; i++){
				if (list[i][key] == value){
					return list[i]
				}
			}
			return null
		}
	//8.生成sku列表
	createSkuList=()=>{
		//定义默认值
		$scope.entity.items = [{spec:{},price:0,num:8888,status:'0',isDefault:'0'}]
		//获取用户的选项数据
		let  items = $scope.entity.goodsDesc.specificationItems;
		//遍历此数据,向$scope.entity.items添加数据
		for (let i = 0,len = items.length;i < len;i++){
				$scope.entity.items = addData($scope.entity.items,items[i].attributeName,items[i].attributeValue)
		}
	}
	//定义添加数据方法
	addData=(list,name,value)=>{
		// 定义存放数据的数组
		let itemList = [];
		//遍历list集合将值传入其中
		for (let i = 0,len = list.length;i < len;i++){
			//获取原始数组
			let oldRow = list[i]
			for (let j = 0,len2 = value.length; j < len2;j++){
				let newRow = JSON.parse(JSON.stringify(oldRow))
				//添加规格数据
				newRow.spec[name] = value[j]
				//向新行中添加数据
				itemList.push(newRow)

			}
		}
		//返回结果
		return itemList;
	}


    //读取列表数据绑定到表单中
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    

	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){

		$scope.entity.goodsDesc.introduction = editor.html()
		console.log($scope.entity)
		var serviceObject;//服务层对象
		if($scope.entity.goods.id){//如果有ID
			serviceObject=goodsService.update( $scope.entity); //修改
		}else{
			serviceObject=goodsService.add( $scope.entity );//增加
		}
		serviceObject.success(
			function(response){
				if(response.success){
					$scope.entity = {}
					editor.html('');
				}else{
					alert(response.message);
				}
			}
		);
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
});	
