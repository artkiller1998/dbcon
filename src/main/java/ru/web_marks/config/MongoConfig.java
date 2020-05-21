package ru.web_marks.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import org.springframework.data.mongodb.core.mapping.Document;

@Configuration("mongo-config")
@PropertySource(name = "ConfigurationFromFile", value = "application.properties")
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongodb.connection.addr:127.0.0.1}")
    public String u_addr;

    @Value("${mongodb.connection.port:27017}")
    public Integer u_port;

    @Value("${mongodb.connection.dbname:test}")
    public String u_dbname;

//    @Value("${mongodb.connection.collection}")
//    private static String u_collection;

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



}
