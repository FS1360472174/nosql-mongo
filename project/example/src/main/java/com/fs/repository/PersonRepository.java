package com.fs.repository;

import com.fs.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by SEELE on 2017/6/18.
 */
@Component
public interface PersonRepository extends MongoRepository<Person,String>{

}
