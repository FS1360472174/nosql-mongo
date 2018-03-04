package com.fs.mongo.dao;

import com.fs.mongo.model.Post;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * @author fangzhang
 *
 */
@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {
    Mono<Post> findUserIdByUserId(Long userId);
}
