package com.group11;

import com.group11.common.config.DiyConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class RedEnvelopeDemoApplicationTests {

    @Autowired
    DataSource dataSource;
    @Autowired
    DiyConfig config;

    @Test
    void contextLoads() throws SQLException {
        System.out.println("数据源: " + dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println("连接: " + connection);
        System.out.println("连接地址: " + connection.getMetaData().getURL());
        connection.close();
    }

    @Test
    void test() {
        System.out.println("红包雨动态配置: " + config);
    }

}
