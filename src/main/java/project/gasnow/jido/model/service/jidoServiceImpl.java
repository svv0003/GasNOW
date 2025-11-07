package project.gasnow.jido.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.gasnow.jido.model.dto.GasStation;
import project.gasnow.jido.model.mapper.GasStationMapper;

@Service
@RequiredArgsConstructor
public class jidoServiceImpl implements jidoService {

    private final GasStationMapper gasStationMapper;

    @Override
    public void saveGasStation(GasStation gasStation) {
        gasStationMapper.upsertGasStation(gasStation);
    }
}