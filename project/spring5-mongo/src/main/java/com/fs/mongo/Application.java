package com.fs.mongo;

import com.fs.mongo.dao.PostRepository;
import com.fs.mongo.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;

/**
 * @author fangzhang
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.fs")
public class Application implements CommandLineRunner {

    @Autowired
    private PostRepository mPostResitory;

    public static void main(final String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(final String... strings) throws Exception {
        final Post post1 = new Post();
        post1.setUserId(1L);
        mPostResitory.saveAll(Flux.just(post1)).subscribe();
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
        mPostResitory.findUserIdByUserId(1L).log().subscribe(System.out::println);
        System.out.print("end");
    }
}
