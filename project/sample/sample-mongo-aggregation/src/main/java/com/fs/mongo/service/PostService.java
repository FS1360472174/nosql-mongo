package com.fs.mongo.service;

import com.fs.mongo.dao.PostDb;
import com.fs.mongo.dao.PostRepository;
import com.fs.mongo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fangsheng on 2017/7/1.
 *
 * @cnstonefang@gmail.com
 */
@Service
public class PostService
{
    @Autowired
    private PostDb postDb;

    @Autowired
    private PostRepository postRepository;
    public void savePost(List<Post> listPost) {
        postRepository.save(listPost);
    }
    public List<Post> getAggregationByCategory(long userId) {
        return postDb.getPeopleAggregationByCategory(userId);
    }

    public Post getPost(long userId, String category, Date created) {
        Post post = new Post();
        post.setUserId(userId);
        post.setCategory(category);
        post.setCreated(created);
        return post;
    }
}
