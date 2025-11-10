package project.gasnow.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// 설정 클래스 -> Spring 설정을 담고 있어서 Spring이 이 클래스를 읽는다.
@Configuration
public class AppConfig {

    // 이 메서드가 반환하는 객체를 Spring이 관리하는 Spring Bean으로 등록한다.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}