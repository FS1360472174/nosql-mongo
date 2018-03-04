package com.fs.mongo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.fs.mongo.model.MongoId.COLLECTION_NAME;

/**
 * @author cnstonefang@gmail.com
 */
@Document(collection = COLLECTION_NAME)
@Data
public class MongoId {
    public static final String COLLECTION_NAME = "system-id";
    public static final String FIELD_COLLNAME = "collName";
    public static final String FIELD_SEQID = "seqId";

    @Id
    private String id;
    private String collName;
    private Long seqId;
}
