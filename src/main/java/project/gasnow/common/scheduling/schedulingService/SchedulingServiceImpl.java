package project.gasnow.common.scheduling.schedulingService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.gasnow.common.scheduling.schedulingMapper.SchedulingMapper;

@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService{

    private final SchedulingMapper schedulingMapper;

    @Override
    public int insertOilPrice() {
        return schedulingMapper.insertOilPrice();
    }

}
