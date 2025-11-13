package project.gasnow.chart.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.chart.model.mapper.ChartMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    private final ChartMapper chartMapper;

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
        List<Double> nationwidePrices = new ArrayList<>();
        List<Double> localPrices = new ArrayList<>();

        // 1. 빠른 조회를 위해 두 리스트 모두 Map으로 변환
        Map<String, Double> localMap = new HashMap<>();
        for (Chart c : localData) localMap.put(c.getPriceDate(), c.getAvgPrice());

        Map<String, Double> nationMap = new HashMap<>();
        for (Chart c : nationwideData) nationMap.put(c.getPriceDate(), c.getAvgPrice());

        // 2. [핵심] 모든 날짜 키를 수집하여 중복 제거 (Set 사용)
        // 전국에만 있는 날짜 + 지역에만 있는 날짜 + 둘 다 있는 날짜 모두 포함됨
        java.util.Set<String> allDates = new java.util.HashSet<>();
        allDates.addAll(localMap.keySet());
        allDates.addAll(nationMap.keySet());

        // 3. 날짜순으로 정렬 (String 날짜 포맷이 "YYYY-MM-DD" 형식이므로 알파벳순 정렬하면 시간순 정렬됨)
        List<String> sortedDates = new ArrayList<>(allDates);
        java.util.Collections.sort(sortedDates);

        // 4. 정렬된 통합 타임라인을 순회하며 데이터 채우기
        for (String dateKey : sortedDates) {
            // 라벨 추가
            labels.add(formatDate(dateKey));

            // 전국 데이터 채우기 (데이터 없으면 null 들어감 -> 차트 끊김)
            nationwidePrices.add(nationMap.get(dateKey));

            // 지역 데이터 채우기 (데이터 없으면 null 들어감 -> 차트 끊김)
            localPrices.add(localMap.get(dateKey));
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

    @Override
    public List<Chart> getYesterdayData() {
        return chartMapper.getYesterdayPrice();
    }
}

