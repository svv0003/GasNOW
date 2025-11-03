package project.gasnow.chart.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import project.gasnow.chart.model.dto.Chart;

import java.util.List;

@Mapper
public interface ChartMapper {

    // XML의 #{oilCategory}와 #{areaName}에 값을 전달하기 위해 @Param을 사용한다.
    List<Chart> getAllPrice(@Param("oilCategory") String oilCategory,
                            @Param("areaName") String areaName);
    List<Chart> getOneYearPrice(@Param("oilCategory") String oilCategory,
                            @Param("areaName") String areaName);
    List<Chart> getOneMonthPrice(@Param("oilCategory") String oilCategory,
                            @Param("areaName") String areaName);
    List<Chart> getOneWeekPrice(@Param("oilCategory") String oilCategory,
                            @Param("areaName") String areaName);

}
