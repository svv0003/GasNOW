package project.gasnow.chart.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.chart.model.mapper.ChartMapper;
import project.gasnow.chart.model.mapper.ChartMapper_Practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChartServiceImpl_Practice implements ChartService_Practice {

    private final ChartMapper_Practice chartMapper;

    @Override
    public Map<String, Map<String, Object>> getPeriodChartData(String oilCategory, String areaName, String period) {

        Map<String, Map<String, Object>> res = new HashMap<>();

        List<Chart> weekLocal = getChartData(oilCategory, areaName, period);
        List<Chart> weekNationwide = getChartData(oilCategory, "전국", period);
        res.put(period, createPeriodData(weekLocal,weekNationwide,areaName));

        return res;
    }

    public List<Chart> getChartData(String oilCategory, String areaName, String period) {
        if ("week".equals(period)) {
            return chartMapper.getOneWeekPrice(oilCategory, areaName);
        } else if ("month".equals(period)) {
            return chartMapper.getOneMonthPrice(oilCategory, areaName);
        } else if ("year".equals(period)) {
            return chartMapper.getOneYearPrice(oilCategory, areaName);
        } else { // "all" 또는 기타
            return chartMapper.getAllPrice(oilCategory, areaName);
        }
    }

    private Map<String, Object> createPeriodData(List<Chart> localData, List<Chart> nationwideData, String areaName) {
        Map<String, Object> result = new HashMap<>();
        List<String> labels = new ArrayList<>();
        List<Double> localPrices = new ArrayList<>();
        List<Double> nationwidePrices = new ArrayList<>();

        for(Chart chart : nationwideData) {
            labels.add(formatDate(chart.getPriceDate()));
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

    private String formatDate(String priceDate) {
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
}

