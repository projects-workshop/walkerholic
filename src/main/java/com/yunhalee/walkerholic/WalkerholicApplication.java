package com.yunhalee.walkerholic;

import com.yunhalee.walkerholic.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class WalkerholicApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalkerholicApplication.class, args);
    }
}
