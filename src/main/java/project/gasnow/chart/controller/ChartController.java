package project.gasnow.chart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.chart.model.service.ChartService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chart")
public class ChartController {

    private final ChartService chartService;

    @GetMapping("/") // 예시 API 주소
    public List<Chart> getChartData(

            // 기본값으로 'B027', '서울', '1week' 지정한다.
            @RequestParam(defaultValue = "B027")
            String oilCategory,
            @RequestParam(defaultValue = "서울")
            String areaName,
            @RequestParam(defaultValue = "week")
            String period
    ) {
        /*
        ServiceImpl에게는 그냥 받은 값을 그대로 넘겨준다.
        Js에서 선택된 값을 보내면 해당 값을 조회하고, 아니면 기본값을 조회한다.
         */
        return chartService.getChartData(oilCategory, areaName, period);
    }

}
