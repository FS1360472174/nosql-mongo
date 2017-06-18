package com.fs.app;

import com.fs.entity.Person;
import com.fs.service.PersonService;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by SEELE on 2017/6/18.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.fs")
public class SpringBootApp implements CommandLineRunner{

    @Autowired
    private PersonService personService;
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class);
    }

    public void run(String... arg0) {
        personService.savePerson(new Person("10","test"));
    }
}
