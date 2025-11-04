package project.gasnow.chart.model.service;

import project.gasnow.chart.model.dto.Chart;

import java.util.List;

public interface ChartService {

    /**
     * 클라이언트의 선택(유종, 지역, 기간)에 맞는 차트 데이터를 조회한다.
     * @param oilCategory 예: "B034"
     * @param areaName 예: "서울", "부산", "전국"
     * @param period 예: "week", "month", "year", "all"
     * @return 차트 데이터 리스트
     */
    List<Chart> getChartData(String oilCategory, String areaName, String period);
    List<Chart> getChartDataAlways(String oilCategory, String period);
}
