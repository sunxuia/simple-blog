package net.sunxu.demo.sb;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.social.facebook.api.Account;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.*;


@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories("net.sunxu.demo.sb.repository")
@EnableElasticsearchRepositories("net.sunxu.demo.sb.repository.es")
@EnableRedisRepositories("net.sunxu.demo.sb.repository.redis")
public class SimpleBlogApplication {

    public static void main(String[] args) {
        // 避免 elastic search 的一个相关错误
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(SimpleBlogApplication.class, args);
    }
}



