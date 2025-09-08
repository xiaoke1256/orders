package com.xiaoke1256.orders;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.xiaoke1256.orders.common.mybatis.repository.IRepository;
import com.xiaoke1256.orders.common.mybatis.repository.impl.CrudRepository;

import java.net.URL;
import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {
        System.out.println("System.getProperty(\"user.dir\"):"+System.getProperty("user.dir"));

        ClassLoader classLoader = CodeGenerator.class.getClassLoader();
        URL resource = classLoader.getResource("");
        String modulePath = resource.getPath().substring(0, resource.getPath().indexOf("/target"));
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win") && modulePath.startsWith("/") ) {
            modulePath = modulePath.substring(1);
        }
        String finalModulePath = modulePath;
        System.out.println("modulePath:"+modulePath);

        FastAutoGenerator.create("jdbc:mysql://192.168.249.101:3306/product?characterEncoding=utf-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true", "productUser", "xiaoke_1256")
                .globalConfig(builder -> {
                    builder.author("xiaoke_1256") // 设置作者
                            //.enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(finalModulePath + "/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.xiaoke1256.orders.product") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, finalModulePath + "/src/main/resources/mapper"))// 设置mapperXml文件路径
                            .service("repository").serviceImpl("repository.impl")
                    ;
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok().formatFileName("%sEntity"); // 开启 Lombok 模式
                    builder.mapperBuilder().enableMapperAnnotation(); // 开启 @Mapper 注解
                    //builder.controllerBuilder().enableHyphenStyle(); // 使用连字符命名方式
                    builder.serviceBuilder()
                            .convertServiceFileName(entityName -> "I" + entityName + "Repository")
                            .convertServiceImplFileName(entityName -> entityName + "Repository")
                            .superServiceClass(IRepository.class)
                            .superServiceImplClass(CrudRepository.class)
                    ;
                    builder.addInclude("product");
                })
                .templateEngine(new VelocityTemplateEngine()) // 使用Freemarker引擎模板
                .execute(); // 执行操作
    }
}