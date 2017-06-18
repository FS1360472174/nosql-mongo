package com.fs.config;

import com.mongodb.Mongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by SEELE on 2017/6/18.
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.fs.repository")
public class MongoConfig {

    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception{
        return new SimpleMongoDbFactory(new Mongo(),"test");
    }
    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}
