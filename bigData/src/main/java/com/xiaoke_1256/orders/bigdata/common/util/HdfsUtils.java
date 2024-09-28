package com.xiaoke_1256.orders.bigdata.common.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;

/**
 *
 */
@Component
public class HdfsUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    private static Configuration conf;


    /**
     * 初始化
     */
    private void init(){
        conf = ctx.getBean(Configuration.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
        init();
    }

    /**
     * hdfs上的文件下载到本地(同时合并小文件)
     * @param delSrc 下载成功后如否删除原文件
     * @param hdfsPath
     * @param localPath
     */
    public static void download(boolean delSrc,String hdfsPath,String localPath) {
        try {
            FileSystem fs = FileSystem.get(conf);
            Path srcPath = new Path(hdfsPath);
            Path dstPath = new Path(localPath);

            if (!fs.exists(srcPath)) {
                System.out.println("Source file does not exist");
                return;
            }

            BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(dstPath.toString()));
            // 列出小文件并合并到一个大文件中
            FileStatus[] fileStatuses = fs.listStatus(srcPath);
            for (FileStatus status : fileStatuses) {
                Path smallFilePath = status.getPath();
                // 读取每个小文件的内容并写入到大文件中
                FSDataInputStream inputStream = fs.open(smallFilePath);
                byte[] buffer = new byte[4096];
                int bytesRead = 0;
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    writer.write(buffer, 0, bytesRead);
                }
                inputStream.close();
            }
            writer.close();
            if(delSrc){
                fs.delete(srcPath,true);
            }
            fs.close();

            System.out.println("File downloaded successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 上传至hdfs
     * @param localPath
     * @param hdfsPath
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void upload(String localPath,String hdfsPath) throws IOException, URISyntaxException {
        // 获取FileSystem对象
        FileSystem fs = FileSystem.get(conf);
        // 上传文件
        fs.copyFromLocalFile(new Path(localPath), new Path(hdfsPath));
        System.out.println("File uploaded successfully.");
        fs.close();
    }

    /**
     * 下载一个文件夹
     * @param hdfsFolderPath
     * @param localFolderPath
     * @throws IOException
     */
    public static void downloadFiles(boolean delSrc,String hdfsFolderPath, String localFolderPath) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        Path hdfsPath = new Path(hdfsFolderPath);
        FileStatus[] fileStatusArray = fs.listStatus(hdfsPath);
        for (FileStatus fileStatus : fileStatusArray) {
            Path filePath = fileStatus.getPath();
            fs.copyToLocalFile(filePath, new Path(localFolderPath + "/" + filePath.getName())); // 下载文件到本地
        }
        if(delSrc){
            fs.delete(hdfsPath,true);
        }
        fs.close();
    }

    /**
     * 删除文件或文件夹
     * @param hdfsPath
     */
    public static void delete(String hdfsPath) throws IOException {
        FileSystem fs = FileSystem.get(conf);
        fs.delete(new Path(hdfsPath),true);
        fs.close();
    }


}
