package io.github.robinringwelski.regsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RegsServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(RegsServiceApplication.class, args);
        /* Might want to change application object initially.
        SpringApplication application = new SpringApplication(MyApplication.class);
        application.run(args);
         */
    }

}
