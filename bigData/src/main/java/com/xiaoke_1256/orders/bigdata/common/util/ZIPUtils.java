package com.xiaoke_1256.orders.bigdata.common.util;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * zip 打包
 */
public class ZIPUtils {
    /**
     * 创建zip压缩包
     * @param zipFilePath 压缩包文件路径
     * @param resourcePath 进行压缩的文件路径
     */
    public static File createZip(String zipFilePath, String resourcePath) throws IOException {
        //首先获取文件路径下的所有文件的list（包含子文件夹下的）
        List<File> files = getFileList(new File(resourcePath));

        //判断压缩包路径中有无同名压缩包，有则删除
        File zip = new File(zipFilePath);
        if (zip.exists()) {
            zip.delete();
        }

        //创建压缩包文件
        zip.createNewFile();

        FileOutputStream output = new FileOutputStream(zip);
        ZipOutputStream zipOut = new ZipOutputStream(output);
        //遍历文件List
        for (File file : files) {
            FileInputStream input = new FileInputStream(file);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
            //从file的路径中截取掉resourcePath，只留下相对的地址，是为了让生成的压缩包的文件层级与resourcePath内一致
            ZipEntry zipEntry = new ZipEntry(file.getPath().substring(resourcePath.length()));
            zipOut.putNextEntry(zipEntry);
            int num;
            byte[] buffer = new byte[512];
            while ((num = bufferedInputStream.read(buffer)) != -1) {
                zipOut.write(buffer, 0, num);
            }
            bufferedInputStream.close();
            input.close();
        }
        zipOut.close();
        output.close();
        return zip;
    }

    /**
     * 获取文件夹下的所有文件列表
     * @param file 文件夹的File
     */
    public static List<File> getFileList(File file){
        List<File> fileList = new LinkedList<>();
        File[] files = file.listFiles();
        for (File f : files) {
            //如果是目录，则递归调用本方法
            if (f.isDirectory()){
                fileList.addAll(getFileList(f));
            } else {
                fileList.add(f);
            }
        }

        return fileList;
    }

    public static void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        // 创建输出目录如果它不存在
        if (!dir.exists()) {
            dir.mkdirs();
        }

        byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {
            String fileName = ze.getName();
            File newFile = new File(destDir + File.separator + fileName);

            // 创建所有非存在的父目录
            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            // 关闭当前ZipEntry并移至下一个
            zis.closeEntry();
            ze = zis.getNextEntry();
        }

        // 关闭最后一个ZipEntry
        zis.closeEntry();
        zis.close();
        fis.close();
    }

}
