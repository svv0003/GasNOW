package project.gasnow.chart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.chart.model.service.ChartService;
import project.gasnow.chart.model.service.ChartService_Practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chart")
public class ChartController_Practice {

    private final ChartService_Practice chartService;

    @GetMapping("/data")
    public Map<String, Map<String, Object>> getChartData(
            @RequestParam(defaultValue = "B027")
            String oilCategory,
            @RequestParam(defaultValue = "전국")
            String areaName,
            @RequestParam(defaultValue = "week")
            String period
    ) {
        return chartService.getPeriodChartData(oilCategory, areaName, period);
    }
}
