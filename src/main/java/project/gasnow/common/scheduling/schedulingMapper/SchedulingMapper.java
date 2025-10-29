package project.gasnow.common.scheduling.schedulingMapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SchedulingMapper {

    int insertOilPrice();
}
