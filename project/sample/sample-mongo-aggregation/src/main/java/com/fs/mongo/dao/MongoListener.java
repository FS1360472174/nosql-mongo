package com.fs.mongo.dao;

import com.fs.mongo.annotation.MongoAutoId;
import com.fs.mongo.model.MongoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

/**
 * @author cnstonefang@gmail.com
 */
@Repository
public class MongoListener extends AbstractMongoEventListener<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(MongoListener.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Object> event) {
        final Object source = event.getSource();
        if (source != null) {
            ReflectionUtils.doWithFields(source.getClass(), new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(final java.lang.reflect.Field field)
                        throws IllegalArgumentException, IllegalAccessException {
                    ReflectionUtils.makeAccessible(field);
                    if (field.isAnnotationPresent(MongoAutoId.class) && field.get(source) == null) {
                        field.set(source, getId(event.getCollectionName()));
                    }
                }
            });
        }
    }
    /**
     * 获取自增id
     * 这边是利用mongo的findAndModify的原子性实现的
     * 也可以使用redis来实现
     */
    private Long getId(final String collName) {
        final Query query = new Query().addCriteria(
                new Criteria(MongoId.FIELD_COLLNAME).is(collName));
        final Update update = new Update();
        update.inc(MongoId.FIELD_SEQID, 1);
        final FindAndModifyOptions options = new FindAndModifyOptions().upsert(true).returnNew
                (true);
        final MongoId sequence = mongoTemplate.findAndModify(query, update, options,
                MongoId.class);
        return sequence.getSeqId();
    }
}
