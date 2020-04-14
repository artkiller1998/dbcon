package ru.web_marks.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

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

    //Задать название пакета
    //@Override
    //protected String getMappingBasePackage() {
    //    return "ru.web_marks";
    //}
}
