package config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: CodeGenerator
 * Description:  mybatis-plus 代码生成器
 * Date:      2019-07-31 20:11
 * author     admin
 * version    V1.0
 */

public class CodeGenerator {

    private  static final String basePackage = "com";
    private static final String basePath = System.getProperty("user.dir");

     public static void main(String[] args) {
         List<String> list =new ArrayList();
         list.add("mmall_user");
         list.add("mmall_cart");
         list.add("mmall_category");
         list.add("mmall_order");
         list.add("mmall_order_item");
         list.add("mmall_pay_info");
         list.add("mmall_product");
         list.add("mmall_shipping");
         for (int i = 0; i < list.size(); i++) {
            run(list.get(i),"mmall_");
         }
    }

    public static void run(String tableName,String prefix) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(new File(basePath+ "/src/main/java").getAbsolutePath());
        gc.setAuthor("zhangbin");
        //覆盖写入
        gc.setFileOverride(true);
        gc.setActiveRecord(false);
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setIdType(IdType.INPUT);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/mmall?useUnicode=true&useSSL=false&characterEncoding=utf8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        mpg.setDataSource(dsc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
            }
        };

        // 自定义生成
        List<FileOutConfig> focList = new ArrayList<>();

        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String childPath = "/src/main/resources/mapper/" + tableInfo.getMapperName() + ".xml";
                return new File(basePath, childPath).getAbsolutePath();
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(basePackage);
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();

        strategy.setEntityColumnConstant(true); //生成字段常量
        strategy.entityTableFieldAnnotationEnable(true); //生成注解
        strategy.setEntityLombokModel(true); //lombok
        strategy.setTablePrefix(new String[] { prefix });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[] { tableName }); // 需要生成的表

        mpg.setStrategy(strategy);


        // 关闭默认 xml 生成，调整生成 至 根目录
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        mpg.setTemplate(tc);

        mpg.execute();
    }

}
