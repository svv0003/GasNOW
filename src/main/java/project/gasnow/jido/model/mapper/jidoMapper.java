package project.gasnow.jido.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import project.gasnow.jido.model.dto.jido;

import java.util.List;

@Mapper
public interface jidoMapper {
    int insertStationList(List<jido> stationList); // 파라미터 타입 List<jido>
    int insertOrUpdateStation(jido station);      // 파라미터 타입 jido
    List<jido> selectAllStations();               // 반환 타입 List<jido>
    jido selectStationById(String gsId);          // 파라미터 String, 반환 타입 jido
}
