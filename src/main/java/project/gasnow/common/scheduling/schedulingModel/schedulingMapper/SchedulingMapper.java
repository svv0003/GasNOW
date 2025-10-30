package project.gasnow.common.scheduling.schedulingModel.schedulingMapper;

import org.apache.ibatis.annotations.Mapper;
import project.gasnow.chart.model.dto.Chart;

import java.util.List;

@Mapper
public interface SchedulingMapper {

    /**
     * API에서 받아온 차트 데이터 리스트를 DB에 일괄 삽입(또는 업데이트)합니다.
     * @param charts 저장할 차트 데이터 리스트
     * @return 영향을 받은 행의 수
     */
    int saveChartData(List<Chart> charts);

}
