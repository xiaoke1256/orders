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
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 */
@Component
public class HdfsUtils implements ApplicationContextAware {

    private static ApplicationContext ctx;

    private static String hdfsUri ;

    /**
     * 初始化
     */
    private void init(){
        hdfsUri = ctx.getEnvironment().getProperty("spark.hdfs.uri");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
        init();
    }

    /**
     * hdfs上的文件下载到本地(同时合并小文件)
     * @param delSrc
     * @param hdfsPath
     * @param localPath
     */
    public static void download(boolean delSrc,String hdfsPath,String localPath) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUri);

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
        // 获取配置对象
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUri);
//        // 指定HDFS的URI
//        URI uri = new URI(hdfsUri);
        // 获取FileSystem对象
        FileSystem fileSystem = FileSystem.get(conf);
        // 上传文件
        fileSystem.copyFromLocalFile(new Path(localPath), new Path(hdfsPath));
        System.out.println("File uploaded successfully.");
    }

    /**
     * 下载一个文件夹
     * @param hdfsFolderPath
     * @param localFolderPath
     * @throws IOException
     */
    public static void downloadFiles(String hdfsFolderPath, String localFolderPath) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUri); // 设置HDFS的URI
        FileSystem fs = FileSystem.get(conf);
        Path hdfsPath = new Path(hdfsFolderPath);
        FileStatus[] fileStatusArray = fs.listStatus(hdfsPath);
        for (FileStatus fileStatus : fileStatusArray) {
            Path filePath = fileStatus.getPath();
            fs.copyToLocalFile(filePath, new Path(localFolderPath + "/" + filePath.getName())); // 下载文件到本地
        }
    }


}
