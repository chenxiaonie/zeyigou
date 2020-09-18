 //控制层 
app.controller('itemCatController' ,function($scope,$controller,itemCatService){
	
	$controller('baseController',{$scope:$scope});//继承

	//查找级别列表
	$scope.findByParentId=(parentId)=>{
		itemCatService.findByParentId(parentId).success(response=>{
			$scope.list = response
		})
	}

	//设置默认属性
	$scope.parentId = -1;
	$scope.grade = 1;
	$scope.setGrade=(grade)=>{
		console.log("AAA"+grade)
		$scope.grade = grade;
	}
//g根据分类的级别对其设置值
	$scope.showLevel=(entity)=>{
		console.log(entity)
		switch ($scope.grade){
			case 1:
				$scope.entity_1 = entity
				$scope.entity_2 = null
				$scope.entity_3 = null
				break;
			case 2:
				$scope.entity_2 = entity
				$scope.entity_3 = null
				break;
			case 3:
				$scope.entity_3 = entity
				break;
		}
		$scope.parentId = entity.id;
		//根据父id查询子分类列表
		$scope.findByParentId(entity.id);
	}

    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
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
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}


});	
