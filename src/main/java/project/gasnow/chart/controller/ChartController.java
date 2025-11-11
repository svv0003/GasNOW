package project.gasnow.chart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.chart.model.dto.ChartResponse;
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

    /**
     * 차트 데이터 조회 - 모든 기간 데이터를 한 번에 반환
     * @param oilCategory 유종코드 (기본값 :B027, 휘발유)
     * @param areaName    지역명   (기본값: 서울)
     * @param period      요청 기간(week, month, year, all)
     * @return            모든 기간별 차트 데이터
     */
    @GetMapping()
    public Map<String, Map<String, Object>> getData(
            // 기본값으로 'B027', '서울', 'week' 지정한다.
            @RequestParam(defaultValue = "B027")
            String oilCategory,
            @RequestParam(defaultValue = "서울")
            String areaName,
            @RequestParam(defaultValue = "week")
            String period
    ) {
        Map<String, Map<String, Object>> res = new HashMap<>();

        // 1-week data
        // 서비스에서 지별역 1주 데이터를 조회해서 변수에 저
        List<Chart> weekLocal = chartService.getChartData(oilCategory, areaName, "week");
        // 서비스에서 전국 1주 데이터를 조회하여 변수에 저장
        List<Chart> weekNationwide = chartService.getChartData(oilCategory, "전국", "week");
        // 1주 데이터를 차트 형식으로 변환해해서 결과 맵에 1주 키로 저장
        res.put("1주", createPeriodData(weekLocal,weekNationwide,areaName));


        // 1-month data (
        List<Chart> monthLocal = chartService.getChartData(oilCategory, areaName, "month");
        List<Chart> monthNationwide = chartService.getChartData(oilCategory, "전국", "month");
        res.put("1개월", createPeriodData(monthLocal,monthNationwide,areaName));


        // 1-year data
        List<Chart> yearLocal = chartService.getChartData(oilCategory, areaName, "year");
        List<Chart> yearNationwide = chartService.getChartData(oilCategory, "전국", "year");
        res.put("1년", createPeriodData(yearLocal,yearNationwide,areaName));

        // all(3-years) data
        List<Chart> allLocal = chartService.getChartData(oilCategory, areaName, "all");
        List<Chart> allNationwide = chartService.getChartData(oilCategory, "전국", "all");
        res.put("3년", createPeriodData(allLocal,allNationwide,areaName));
        /*
        ServiceImpl에게는 그냥 받은 값을 그대로 넘겨준다.
        Js에서 선택된 값을 보내면 해당 값을 조회하고, 아니면 기본값을 조회한다.

        // 서비스 호출 결과를 변수에 담는다.
        List<Chart> local = chartService.getChartData(oilCategory, areaName, period);
        List<Chart> nationwide = chartService.getChartDataAlways(oilCategory, period);

        log.info(nationwide.toString());
        // DTO 객체를 생성하여 반환한다.
        return new ChartResponse(local, nationwide);
         */
        return res;// 모든 데이터가 담긴 맵을 반
    }
    /**
     * 전국 데이터 조회 - 모든 기간 데이터를 한 번에 반환
     * @param oilCategory 유종코드 (기본값 :B027, 휘발유)
     * @return            모든 기간별 차트 데이터
     */
    @GetMapping("/always")
    public Map<String, Map<String, Object>> getChartDataAlways(
            // 기본값으로 'B027', 'week' 지정한다.
            @RequestParam(defaultValue = "B027")
            String oilCategory

    ) {
        Map<String, Map<String, Object>> res = new HashMap<>();

        // 1-week data
        // 모든 데이터가 1주 키에 저장되게 설정
        //전국 1주 데이터 조회
        List<Chart> weekdata = chartService.getChartDataAlways(oilCategory,  "week");
        res.put("1", createNationwideData(weekdata));


        // 1-month data (
        // 전국 데이터만 조회!
        List<Chart> monthdata = chartService.getChartDataAlways(oilCategory,  "month");
        res.put("1", createNationwideData(monthdata));


        // 1-year data
        List<Chart> yeardata = chartService.getChartDataAlways(oilCategory,  "year");
        res.put("1", createNationwideData(yeardata));

        // all(3-years) data
        List<Chart> alldata = chartService.getChartDataAlways(oilCategory,  "all");
        res.put("1", createNationwideData(alldata));
        /*
        ServiceImpl에게는 그냥 받은 값을 그대로 넘겨준다.
        Js에서 선택된 값을 보내면 해당 값을 조회하고, 아니면 기본값을 조회한다.

        // 서비스 호출 결과를 변수에 담는다.
        List<Chart> local = chartService.getChartData(oilCategory, areaName, period);
        List<Chart> nationwide = chartService.getChartDataAlways(oilCategory, period);

        log.info(nationwide.toString());
        // DTO 객체를 생성하여 반환한다.
        return new ChartResponse(local, nationwide);
         */
        return res;
    }
    /**
     * Convert only the national data into a chart format
     * @param localData
     * @param nationwideData
     * @param areaName
     * @return
     */
    private Map<String, Object> createPeriodData(List<Chart> localData, List<Chart> nationwideData, String areaName) {
        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> localPrices = new ArrayList<>();
        List<Double> nationwidePrices = new ArrayList<>();

        for(Chart chart : nationwideData) {
            labels.add(formateDate(chart.getPriceDate()));
            nationwidePrices.add(chart.getAvgPrice());
        }

        for(Chart chart : localData) {
            localPrices.add(chart.getAvgPrice());
        }

        result.put("labels", labels);
        result.put("전국", nationwidePrices);
        result.put(areaName, localPrices);

        return result;
    }

    /**
     * convert only the national data into a chart format.
     * @param nationwideData
     * @return
     */
    private Map<String, Object> createNationwideData(List<Chart> nationwideData){
        Map<String, Object> result = new HashMap<>();

        List<String> labels = new ArrayList<>();
        List<Double> nationwidePrices = new ArrayList<>();

        for(Chart chart : nationwideData ){
            labels.add(formateDate(chart.getPriceDate()));
            nationwidePrices.add(chart.getAvgPrice());
        }
        result.put("labels", labels);
        result.put("전국",nationwidePrices);

        return result;
    }

    /**
     * date formatting
     * @param priceDate Formate the date appropriately based on the priceDate format
     * @return
     */
    private String formateDate(String priceDate) {
        if(priceDate == null || priceDate.length() < 10) return "";

        // "2024-10-01" 형식 처리
        String[] parts = priceDate.split("-");
        if(parts.length == 3) {
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);
            return month + "." + day;
        }

        return priceDate;
    }
    @GetMapping("/yesterday")
    public List<Chart> getYesterdayData() {
        return chartService.getYesterdayData();
    }
}
