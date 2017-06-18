package com.fs.service;

import com.fs.entity.Person;
import com.fs.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by SEELE on 2017/6/18.
 */
@Service
public class PersonService {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PersonRepository personRepository;

    public void savePerson(Person person) {
        mongoTemplate.save(person);
    }

    public void savePersonWithRepo(Person person) {
        personRepository.insert(person);
    }

}
