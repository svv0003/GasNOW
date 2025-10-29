package project.gasnow.chart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.gasnow.chart.model.service.ChartService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chart")
public class ChartController {

    private final ChartService chartService;

    // @GetMapping("/")

}
