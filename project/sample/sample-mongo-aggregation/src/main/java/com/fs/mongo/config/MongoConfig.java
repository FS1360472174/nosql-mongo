package com.fs.mongo.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.net.UnknownHostException;

/**
 * Created by fangsheng on 2017/7/1.
 *
 * @cnstonefang@gmail.com
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.fs.mongo.dao")
public class MongoConfig extends AbstractMongoConfiguration {

    @Bean
    public MongoDbFactory mongoDbFactory() throws UnknownHostException {
        MongoClientOptions options = new MongoClientOptions.Builder().connectionsPerHost(8).build();
        return new SimpleMongoDbFactory(new MongoClient("localhost",options ),"test");
    }
    // 默认数据库会生成_class字段，需要更改mappingMongoConverter的typeMappper属性值
    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory(),mappingMongoConverter());
    }

    protected String getDatabaseName() {
        return "test";
    }

    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient();
    }

    @Bean
    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        MappingMongoConverter converter = new MappingMongoConverter(mongoDbFactory(),this.mongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }
}
