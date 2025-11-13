package project.gasnow.chart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.chart.model.service.ChartService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chart")
public class ChartController {

    private final ChartService chartService;

    @GetMapping()
    public Map<String, Map<String, Object>> getData(
            @RequestParam(defaultValue = "B027") String oilCategory,
            @RequestParam(defaultValue = "서울") String areaName,
            @RequestParam(defaultValue = "week") String period
    ) {
        log.info("차트 데이터 요청 - 유종: {}, 지역: {}, 기간: {}", oilCategory, areaName, period);

        Map<String, Map<String, Object>> res = new HashMap<>();

        // 1-week data (일별)
        List<Chart> weekLocal = chartService.getChartData(oilCategory, areaName, "week");
        List<Chart> weekNationwide = chartService.getChartData(oilCategory, "전국", "week");
        log.info("1주 데이터 - 지역: {}, 전국: {}", weekLocal.size(), weekNationwide.size());
        res.put("1주", createPeriodData(weekLocal, weekNationwide, areaName, "day"));

        // 1-month data (일별)
        List<Chart> monthLocal = chartService.getChartData(oilCategory, areaName, "month");
        List<Chart> monthNationwide = chartService.getChartData(oilCategory, "전국", "month");
        log.info("1개월 데이터 - 지역: {}, 전국: {}", monthLocal.size(), monthNationwide.size());
        res.put("1개월", createPeriodData(monthLocal, monthNationwide, areaName, "day"));

        // 1-year data (월별)
        List<Chart> yearLocal = chartService.getChartData(oilCategory, areaName, "year");
        List<Chart> yearNationwide = chartService.getChartData(oilCategory, "전국", "year");
        log.info("1년 데이터 - 지역: {}, 전국: {}", yearLocal.size(), yearNationwide.size());

        // 데이터 확인 (처음 3개만)
        if(!yearLocal.isEmpty()) {
            log.info("1년 지역 데이터 샘플: {}", yearLocal.subList(0, Math.min(3, yearLocal.size())));
        }

        res.put("1년", createPeriodData(yearLocal, yearNationwide, areaName, "month"));

        // all(3-years) data (월별)
        List<Chart> allLocal = chartService.getChartData(oilCategory, areaName, "all");
        List<Chart> allNationwide = chartService.getChartData(oilCategory, "전국", "all");
        log.info("3년 데이터 - 지역: {}, 전국: {}", allLocal.size(), allNationwide.size());
        res.put("3년", createPeriodData(allLocal, allNationwide, areaName, "month"));

        return res;
    }

    private Map<String, Object> createPeriodData(List<Chart> localData, List<Chart> nationwideData, String areaName, String dateFormat) {
        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> localPrices = new ArrayList<>();
        List<Double> nationwidePrices = new ArrayList<>();

        log.info("createPeriodData - 지역: {}, 지역데이터: {}, 전국데이터: {}, 포맷: {}",
                areaName, localData.size(), nationwideData.size(), dateFormat);

        // 전국 데이터 처리
        for(Chart chart : nationwideData) {
            labels.add(formatDateByType(chart.getPriceDate(), dateFormat));
            nationwidePrices.add(chart.getAvgPrice());
        }

        // 지역 데이터 처리
        for(Chart chart : localData) {
            localPrices.add(chart.getAvgPrice());
        }

        log.info("최종 데이터 크기 - labels: {}, 전국: {}, {}: {}",
                labels.size(), nationwidePrices.size(), areaName, localPrices.size());

        result.put("labels", labels);
        result.put("전국", nationwidePrices);
        result.put(areaName, localPrices);

        return result;
    }

    /**
     * 기간별로 다른 날짜 포맷 적용
     * @param priceDate 날짜 문자열
     * @param dateFormat "day" (일별: 10.15) 또는 "month" (월별: 24.10)
     * @return 포맷된 날짜 문자열
     */
    private String formatDateByType(String priceDate, String dateFormat) {
        if(priceDate == null || priceDate.length() < 7) return "";

        try {
            String[] parts = priceDate.split("-");
            if(parts.length >= 2) {
                String year = parts[0];
                String month = parts[1];

                if ("day".equals(dateFormat)) {
                    // 일별: "10.15" 형식
                    if(parts.length >= 3) {
                        String day = parts[2].length() >= 2 ? parts[2].substring(0, 2) : parts[2];
                        return Integer.parseInt(month) + "." + Integer.parseInt(day);
                    }
                } else if ("month".equals(dateFormat)) {
                    // 월별: "24.10" 형식
                    return year.substring(2) + "." + month;
                }
            }
        } catch (Exception e) {
            log.error("날짜 포맷 오류: {}", priceDate, e);
        }

        return priceDate;
    }

    @GetMapping("/yesterday")
    public List<Chart> getYesterdayData() {
        return chartService.getYesterdayData();
    }
}