package com.fs.mongo.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;
import java.util.Date;
import java.util.List;

/**
 * Created by fangsheng on 2017/7/1.
 */
@Document(collection = "post")
@Data
public class Post {
    @Id
    private ObjectId postId;
    private Long userId;
    private String title;
    private String category;
    @Indexed
    private List<String> tags;
    private String contents;
    private Date created;
    private long total;
}
