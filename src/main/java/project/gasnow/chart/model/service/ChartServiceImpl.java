package project.gasnow.chart.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.chart.model.mapper.ChartMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    private final ChartMapper chartMapper;

    @Override
    public List<Chart> getChartData(String oilCategory, String areaName, String period) {

        // 클라이언트가 선택한 기간에 따라 mapper 메서드 호출한다.
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

    @Override
    public List<Chart> getChartDataAlways(String oilCategory, String period) {

        if ("week".equals(period)) {
            return chartMapper.getOneWeekPrice(oilCategory, "전국");

        } else if ("month".equals(period)) {
            return chartMapper.getOneMonthPrice(oilCategory, "전국");

        } else if ("year".equals(period)) {
            return chartMapper.getOneYearPrice(oilCategory, "전국");

        } else { // "all" 또는 기타
            return chartMapper.getAllPrice(oilCategory, "전국");
        }
    }

    @Override
    public List<Chart> getYesterdayData() {
        return chartMapper.getYesterdayPrice();
    }
}
