app.service('brandService',function ($http){
        //查询所有品牌列表
        this.findAll=()=>{
                return $http.get("../brand/list.do")
        }
        //条件分页查询
        this.search=(page,size,searchEntity)=>{
                return $http.post("../brand/search.do?page="+page+"&size="+size,searchEntity)
        }
        //修改
        this.update=(entity)=>{
             return  $http.post("../brand/update.do",entity)
        }
        //添加
        this.add=(entity)=>{
                return $http.post("../brand/add.do",entity)
        }
        //删除
        this.del=(selectIds)=>{
               return  $http.post("../brand/delete.do?ids="+selectIds)
        }
        //查询品牌
        this.findBrandList=()=>{
                return $http.post("../brand/findBrandList.do")
        }
})