package com.xiaoke1256.thirdpay;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;


import java.net.URL;
import java.util.Collections;

public class CodeGenerator {
    public static void main(String[] args) {

        ClassLoader classLoader = CodeGenerator.class.getClassLoader();
        URL resource = classLoader.getResource("");
        String modulePath = resource.getPath().substring(0, resource.getPath().indexOf("/target"));
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win") && modulePath.startsWith("/") ) {
            modulePath = modulePath.substring(1);
        }
        System.out.println("modulePath:"+modulePath);
        final String finalModulePath = modulePath;

        FastAutoGenerator.create("jdbc:mysql://192.168.249.101:3306/thirdpay?characterEncoding=utf-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true",
                "thirdpayUser", "xiaoke_1256").globalConfig(builder -> {

                    builder.outputDir( finalModulePath + "/src/main/java")
                            .fileOverride()
                            .author("xiaoke_1256")
                            .dateType(DateType.TIME_PACK)
                            .commentDate("yyyy-MM-dd");
        }).packageConfig(builder -> {
            builder.parent("com.xiaoke1256.thirdpay.payplatform")
                    .entity("bo")
                    .mapper("mapper")
                    .xml("mapper")
                    .service("dao")
                    .serviceImpl("dao.impl")
                    .controller("controller")
                    .pathInfo(Collections.singletonMap(OutputFile.xml, finalModulePath + "/src/main/resources/mapper"));
        }).strategyConfig(builder -> {
            builder.addInclude("third_pay_order") // 设置需要生成的表名
                    .addTablePrefix("t_", "sys_") // 设置过滤表前缀
                    // 实体策略配置
                    .entityBuilder()
                    .enableLombok() // 开启 Lombok
                    .naming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
                    .columnNaming(NamingStrategy.underline_to_camel) // 数据库表字段映射到实体的命名策略
                    .enableTableFieldAnnotation() // 开启字段注解
                    // Controller策略配置
                    .controllerBuilder()
                    .enableRestStyle() // 开启生成@RestController 控制器
                    .enableHyphenStyle() // 开启驼峰转连字符
                    // Service 策略配置
                    .serviceBuilder()
                    .formatServiceFileName("%sDao") // 格式化 service 接口文件名称
                    .formatServiceImplFileName("%sDaoImpl") // 格式化 service 实现类文件名称
                    // Mapper 策略配置
                    .mapperBuilder()
                    .enableBaseResultMap() // 启用 BaseResultMap 生成
                    .enableBaseColumnList(); // 启用 BaseColumnList 生成
        }).templateEngine(new VelocityTemplateEngine())
        .execute();

        System.out.println("代码生成完成！");

    }
}
