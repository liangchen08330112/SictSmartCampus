package cn.edu.sict.sict_smart_campus.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.edu.sict.sict_smart_campus.mapper")
public class MyConfig {
    //分页插件
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor pi = new PaginationInterceptor();
        return pi;
    }
}
