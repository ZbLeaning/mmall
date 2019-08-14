package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: APP
 * Description:  启动类
 * Date:      2019-07-25 21:11
 * author     admin
 * version    V1.0
 */
@SpringBootApplication
@MapperScan("com.mmall.mapper")
public class App {
    public static void main( String[] args )

    {
        System.out.println( "Hello World!" );
        SpringApplication.run(App.class,args);
    }
}
