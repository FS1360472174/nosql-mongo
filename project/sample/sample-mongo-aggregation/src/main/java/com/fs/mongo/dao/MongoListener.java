package com.fs.mongo.dao;

import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

/**
 * @author cnstonefang@gmail.com
 */
@Repository
public class MongoListener extends AbstractMongoEventListener<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(MongoListener.class);
    @Override
    public void onBeforeSave(BeforeSaveEvent<Object> event) {
        LOG.info("listen on before save");
    }
}
