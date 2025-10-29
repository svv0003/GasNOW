package project.gasnow.jido.model.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gasnow.jido.model.service.jidoService;

@RestController
@RequestMapping("/jido")
@RequiredArgsConstructor
@Slf4j
public class jidoController {
    private final jidoService jidoService;
    /**
    * /jido 페이지 요청 및 지도 데이터 요청을 처리할 메서드
    * @return
    * */
    @GetMapping
    public String jidoPage() {
        log.info("/jido GET 요청 수신");
        return "jido";
    }
}
