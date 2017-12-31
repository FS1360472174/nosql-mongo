package com.fs.mongo;

import com.fs.mongo.dao.PostDb;
import com.fs.mongo.model.Post;
import com.fs.mongo.service.PostService;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by fangsheng on 2017/7/1.
 *
 * @cnstonefang@gmail.com
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.fs")

public class SpringBootApp implements CommandLineRunner {
    static {
        Properties props = new Properties();
        try {
            props.load(LogConfiguration.class.getResourceAsStream("/log4j.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PropertyConfigurator.configure(props);
    }
    private static final Logger LOG = LoggerFactory.getLogger(SpringBootApp.class);
    @Autowired
    private PostService postService;
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class);
    }
    public void run(String... strings) throws Exception {
        testAggByCategory();
        testRepository();
    }

    private  void testAggByCategory(){
        List<Post> input = new ArrayList<Post>();
        input.add(postService.getPost(1,"java",new Date()));
        input.add(postService.getPost(1,"java",new Date()));
        input.add(postService.getPost(2,"java",new Date()));
        input.add(postService.getPost(2,"c++",new Date()));
        // postService.savePost(input);
        List<Post> results = postService.getAggregationByCategory(1);
        LOG.info("results:"+results);
    }

    private void testRepository() {
        LOG.info("test repo");
        Post post = postService.getPostByUserId(1);
        LOG.info("repository");
    }
}
