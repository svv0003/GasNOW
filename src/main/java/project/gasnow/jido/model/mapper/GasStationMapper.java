package project.gasnow.jido.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.gasnow.jido.model.dto.GasStation;

@Mapper
public interface GasStationMapper {
    void upsertGasStation(GasStation gasStation);
}
