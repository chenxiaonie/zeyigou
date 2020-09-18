package com.zeyigou.shop.controller;

import com.zeyigou.pojo.Result;
import com.zeyigou.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UpLoadController {
    //获取属性
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;
    @RequestMapping("upload")
    public Result upload(MultipartFile file) throws Exception {
        System.out.println("file = " + file);
        try {
            //获取上传工具类对象
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:properties/fds.conf");
            //处理文件后缀名
            String suffix = file.getName().substring(file.getName().indexOf(".") + 1);
            System.out.println("suffix = " + suffix);
            //上传文件
            byte[] bytes = file.getBytes();
            String s = fastDFSClient.uploadFile(bytes, suffix);
            //获取最终的图片路径
            String url = FILE_SERVER_URL + s;
            System.out.println("url = " + url);
            //返回结果
            return new Result(true,url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }

    }
}
