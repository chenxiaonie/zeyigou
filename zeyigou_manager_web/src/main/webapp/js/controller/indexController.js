app.controller("indexController",function ($scope,indexService){
    $scope.findName=()=>{
        indexService.findName().success(response=>{
            $scope.name = response.name
        })
    }
})