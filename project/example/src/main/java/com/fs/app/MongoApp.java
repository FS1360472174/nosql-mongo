package com.fs.app;

import com.fs.entity.Person;
import com.fs.service.PersonService;
import com.mongodb.Mongo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.net.UnknownHostException;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by SEELE on 2017/6/18.
 */
@ComponentScan(basePackages = "com.fs")
public class MongoApp {
    private static final Log log = LogFactory.getLog(MongoApp.class);
    @Autowired
    private PersonService personService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public static void main(String [] args) {
        //ApplicationContext context = new AnnotationConfigApplicationContext(MongoConfig.class);
        //MongoTemplate template = (MongoTemplate) context.getBean("mongoTemplate");
        //template.insert(new Person("3","mike2"));
        //PersonService service = (PersonService) context.getBean("personService");
       // service.savePerson(new Person("5","mike2"));
        //upsertWithTemplate();

        // this is canot be injected,as app instance is not managed by spring
       // MongoApp app = new MongoApp();
        // app.upsert();
    }

    private static void upsertWithOpertion() {
        MongoOperations operation = null;
        try {
            operation = new MongoTemplate(new Mongo(),"test");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        operation.insert(new Person("1","mark"));
        log.info(operation.findOne(new Query(where("name").is("mark")),Person.class));
    }

    private void upsert() {
        Person person = new Person("2","mike");
        personService.savePerson(person);
       // log.info(mongoTemplate.findOne(new Query(where("name").is("mark")),Person.class));
    }

}
