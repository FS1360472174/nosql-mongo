package com.fs.mongo.dao;

import com.fs.mongo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fangsheng on 2017/7/1.
 *
 * @cnstonefang@gmail.com
 */
@Component
public class PostDb {
    @Autowired
    private MongoTemplate mongoTemplate;

    public PostDb(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    public List<Post> getPeopleAggregationByCategory(long userId) {
        MatchOperation match = Aggregation.match(new Criteria("userId").is(userId));
        GroupOperation group  = Aggregation.group("category").count().as("total");
        ProjectionOperation project =  Aggregation.project("total").and("_id").as("category")
                .andExclude("_id");
        Aggregation aggregation = Aggregation.newAggregation(match,group,project);
        AggregationResults<Post> results = mongoTemplate.aggregate(aggregation,"post",Post.class);
        return results.getMappedResults();
    }
}
