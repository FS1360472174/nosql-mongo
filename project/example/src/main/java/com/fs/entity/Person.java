package com.fs.entity;

/**
 * Created by SEELE on 2017/6/18.
 */
public class Person {
    private String id;
    private String name;
    public Person(String id, String name) {
        this.id = id;
        this.name = name;

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
