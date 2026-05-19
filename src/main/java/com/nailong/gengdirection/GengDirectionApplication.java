package com.nailong.gengdirection;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 应用启动类。
 *
 * @MapperScan 告诉 MyBatis 去哪里扫描 Mapper 接口；
 * 这里用 ** 通配，意思是 com.nailong.gengdirection 下任何子包里的 mapper 包都会被扫到，
 * 例如：post.mapper、user.mapper、comment.mapper ...
 *
 * 这样一来，每个 Mapper 接口就算不写 @Mapper 也能被注册成 Bean。
 */
@SpringBootApplication
@EnableScheduling
@MapperScan("com.nailong.gengdirection.**.mapper")
public class GengDirectionApplication {

    public static void main(String[] args) {
        SpringApplication.run(GengDirectionApplication.class, args);
    }

}
