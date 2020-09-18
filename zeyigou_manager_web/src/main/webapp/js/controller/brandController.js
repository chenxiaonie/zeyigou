app.controller("brandController",function ($scope,$controller,brandService){
    //继承baseCOntroller的$scope
    $controller("baseController", {$scope: $scope});
    //
    $scope.search=()=>{
        brandService.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage,$scope.searchEntity).success(resp=>{
            //赋值
            $scope.brandList = resp.rows
            //更新记录数
            $scope.paginationConf.totalItems = resp.total
        })
    }
    //查询所有品牌
    $scope.findAll=()=>{
        brandService.findAll().success(resp=>{
            $scope.brandList = resp
        })
    }
    // 添加或修改
    $scope.save=()=>{
        var serviceObject;//服务层对象
        let id = $scope.entity.id;
        if (id){
        let  url =
            serviceObject=brandService.update($scope.entity)//进行修改
        }else {
            serviceObject=brandService.add($scope.entity)//进行添加
        }
       serviceObject.success(resp=>{
            if (resp.success){
                $scope.search()
            }else {
                alert(resp.message)
            }
        })
    }
    //更新模态框内容
    $scope.updateUI=(brand)=>{
        $scope.entity = brand;
    }

    //删除
    $scope.del=()=>{
        brandService.del($scope.selectIds).success(resp=>{
            if (resp.success){
                $scope.search()
                $scope.selectIds=[]
            }else {
                alert(resp.message)
            }
        })
    }
})