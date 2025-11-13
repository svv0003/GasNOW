package project.gasnow.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class configController {

    @Value("${opinet.api.key}")
    private String opinetApiKey;

    // 프론트에서 요청할 때 API 키를 돌려주는 엔드포인트
    @GetMapping("/key")
    public Map<String, String> getApiKey() {
        return Map.of("apiKey", opinetApiKey);
    }
}

