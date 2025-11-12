package project.gasnow.chart.model.service;

import project.gasnow.chart.model.dto.Chart;

import java.util.List;
import java.util.Map;

public interface ChartService_Practice {

    Map<String, Map<String, Object>> getPeriodChartData(String oilCategory, String areaName, String period);

}
