package project.gasnow.chart.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.chart.model.mapper.ChartMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    private final ChartMapper chartMapper;

    @Override
    public List<Chart> getChartData(String oilCategory, String areaName, String period) {

        /*
        클라이언트가 '전국'을 선택했을 때
        '전국' 데이터가 DB에 AREA_NAME = NULL로 저장되어 있다면,
        컨트롤러에서 "전국"이라는 문자열로 받아왔을 때 null로 바꿔줘야 하지만,
        전국 가격 데이터를 DB에 저장할 때 고정값으로 "99", "전국"으로 설정해서 생략해도 된다.
        String mappedAreaName = areaName;
        if ("전국".equals(areaName)) {
            mappedAreaName = null;
        }
         */



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
}
