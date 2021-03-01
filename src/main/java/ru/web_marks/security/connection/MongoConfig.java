package ru.web_marks.security.connection;

import com.mongodb.MongoClient;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

@Configuration("mongo-config")
@PropertySource("classpath:application.properties")
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.host:127.0.0.1}")
    public String u_addr;

    @Value("${spring.data.mongodb.port:27017}")
    public Integer u_port;

    @Value("${spring.data.mongodb.database:test}")
    public String u_dbname;

    //Задать имя базы данных
    @Override
    protected String getDatabaseName() {
        return u_dbname;
    }

    //Задать адреса базы данных: IP  и порт
    @Override
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(u_addr,u_port);
    }

    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }

}
