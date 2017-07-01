package com.fs.mongo;

import com.fs.mongo.dao.PostDb;
import com.fs.mongo.model.Post;
import org.junit.Test;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by fangsheng on 2017/7/1.
 *
 * @cnstonefang@gmail.com
 */
public class PostDbTest {

    @Test
    public void testGetPeopleAggregationByCategory(){
        MongoTemplate mockMongoTemplate = mock(MongoTemplate.class);
        PostDb postDb = new PostDb(mockMongoTemplate);

        List<Post> input = new ArrayList<Post>();
        input.add(getPost(1,"java",new Date()));
        input.add(getPost(1,"java",new Date()));
        input.add(getPost(2,"java",new Date()));
        input.add(getPost(2,"c++",new Date()));


    }

    private Post getPost(long userId,String category,Date created) {
        Post post = new Post();
        post.setUserId(userId);
        post.setCategory(category);
        post.setCreated(created);
        return post;
    }
}
