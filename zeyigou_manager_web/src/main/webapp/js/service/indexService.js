app.service("indexService",function ($http){

    this.findName=()=>{
        return $http.get("../findName.do")
    }
})