package project.gasnow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GasNowApplication {

    public static void main(String[] args) {
        SpringApplication.run(GasNowApplication.class, args);
    }

}
