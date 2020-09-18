app.controller("baseController",function ($scope){

//重新加载列表 数据
    $scope.reloadList=function(){
        //切换页码
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }
    //定义分页配置
    $scope.paginationConf = {
        currentPage : 1,					//当前页
        totalItems : 10,					//总记录数
        itemsPerPage : 5,					//每页的记录数
        perPageOptions: [10, 20, 30, 40, 50],  //分页选项
        onChange : function(){				//改变页码时触发此事件
            //findByPage();					//分页查询
            // $scope.search();						//条件查询带分页
        }
    }
    //定义用户选择的id数组
    $scope.selectIds = [];
    //对id的操作
    $scope.updateSelection=(e,id)=>{
        if (e.target.checked){//如果备选就添加id
            $scope.selectIds.push(id)
        }else {		//如果不背选就删除id
            let index = $scope.selectIds.indexOf(id)
            $scope.selectIds.splice(index,1)
        }
    }
})