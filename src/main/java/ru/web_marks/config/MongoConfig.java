package ru.web_marks.config;

import com.mongodb.MongoClient;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    //Задать имя базы данных
    @Override
    protected String getDatabaseName() {
        return "test";
    }

    //Задать адреса базы данных: IP  и порт
    @Override
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("127.0.0.1", 27017);
    }


}
