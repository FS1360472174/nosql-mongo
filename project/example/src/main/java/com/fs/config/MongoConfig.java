package com.fs.config;

import com.mongodb.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by SEELE on 2017/6/18.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.fs.repository")
public class MongoConfig extends AbstractMongoConfiguration{
    @Override
    public String getDatabaseName() {
        return "test";
    }
    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception{
        MongoClientOptions options = new MongoClientOptions.Builder().connectionsPerHost(8).build();
        return new SimpleMongoDbFactory(new MongoClient("localhost",options ),"test");
    }
    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

    public @Bean Mongo mongo() throws Exception {
        // MongoClient 是原生的java driver
        return new MongoClient();
    }
}
