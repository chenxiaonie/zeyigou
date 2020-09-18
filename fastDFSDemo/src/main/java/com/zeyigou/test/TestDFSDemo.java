package com.zeyigou.test;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class TestDFSDemo {
    public static void main(String[] args) throws Exception {
        //初始化dfs环境
        ClientGlobal.init("E:\\java\\project2\\zeyigou\\fastDFSDemo\\src\\main\\resources\\fds.conf");
        //获取trackClient对象
        TrackerClient trackerClient = new TrackerClient();
        //获取trackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取storageClient
        StorageClient storageClient = new StorageClient(trackerServer,null);
        //进行文件上传
        String[] strings = storageClient.upload_appender_file("C:\\Users\\Administrator\\Desktop\\面试专题\\Redis+CAS+线程池\\Redis\\薪火相传.png","png",null);
        for (String string : strings) {
            System.out.println(string);
        }
    }
}
