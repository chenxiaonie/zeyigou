 //控制层 
app.controller('typeTemplateController' ,function($scope,$controller ,brandService,specificationService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		typeTemplateService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		typeTemplateService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		typeTemplateService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=typeTemplateService.update( $scope.entity ); //修改  
		}else{
			serviceObject=typeTemplateService.add( $scope.entity  );//增加 
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
		typeTemplateService.dele( $scope.selectIds ).success(
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
		typeTemplateService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	//处理显示效果
	$scope.showInfo=(list,key)=>{
		list = JSON.parse(list)	//转化为JSON数组
		let info = ''	//定义最终的连接字符串
		for(let i=0;i < list.length;i++){
			info +=list[i][key]+","
		}
		//处理最后一个逗号
		return info.slice(0,info.length-1)
	}
	$scope.updateUI=(temp)=>{//将内容转换为JSON串
		$scope.entity = temp;
		$scope.entity.brandIds = JSON.parse(temp.brandIds)
		$scope.entity.specIds = JSON.parse(temp.specIds)
		$scope.entity.customAttributeItems = JSON.parse(temp.customAttributeItems)
	}
	$scope.findSpecList=()=>{
		specificationService.findSpecList().success(resp=>{
			$scope.specList = {"data":resp}
		})
	}
	$scope.findBrandList=()=>{
		brandService.findBrandList().success(resp=>{
			$scope.brandList = {"data":resp}
		})
	}
    
});	
