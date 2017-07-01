package com.fs.mongo.dao;

import com.fs.mongo.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by fangsheng on 2017/7/1.
 *
 * @cnstonefang@gmail.com
 */
@Repository
public interface PostRepository extends MongoRepository<Post,String> {

}
