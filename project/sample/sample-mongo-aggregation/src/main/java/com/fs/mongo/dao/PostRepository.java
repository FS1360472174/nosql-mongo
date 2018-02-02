package com.fs.mongo.dao;

import com.fs.mongo.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by fangsheng on 2017/7/1.
 *
 * @cnstonefang@gmail.com
 */
@Repository
public interface PostRepository extends CrudRepository<Post,String> {
    Post findUserIdByUserId(Long userId);
    Post findTopByUserId(Long userId);
}
