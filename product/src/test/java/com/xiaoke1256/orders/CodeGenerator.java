package com.xiaoke1256.orders;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.net.URL;

public class CodeGenerator {
    public static void main(String[] args) {
        AutoGenerator generator = new AutoGenerator();

        System.out.println("System.getProperty(\"user.dir\"):"+System.getProperty("user.dir"));

        ClassLoader classLoader = CodeGenerator.class.getClassLoader();
        URL resource = classLoader.getResource("");
        String modulePath = resource.getPath().substring(0, resource.getPath().indexOf("/target"));
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win") && modulePath.startsWith("/") ) {
            modulePath = modulePath.substring(1);
        }
        System.out.println("modulePath:"+modulePath);

        // 全局配置
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(modulePath + "/src/main/java");
        globalConfig.setAuthor("xiaoke_1256");
        globalConfig.setOpen(false);
        generator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://192.168.249.101:3306/product?characterEncoding=utf-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("productUser");
        dataSourceConfig.setPassword("xiaoke_1256");
        generator.setDataSource(dataSourceConfig);

        // 包配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.xiaoke1256.orders.product");
        packageConfig.setEntity("entity");
        packageConfig.setMapper("mapper");
        packageConfig.setXml("mapper");
        generator.setPackageInfo(packageConfig);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setInclude("product"); // 表名
        generator.setStrategy(strategy);

        generator.execute(); // 执行生成
    }
}