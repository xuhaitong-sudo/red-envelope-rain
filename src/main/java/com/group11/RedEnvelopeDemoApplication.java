package com.group11;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.group11.dao")
@SpringBootApplication
public class RedEnvelopeDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedEnvelopeDemoApplication.class, args);
    }

}
