package project.votebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class VoteBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteBackendApplication.class, args);
    }

}
