package project.gasnow.common.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.gasnow.common.scheduling.schedulingModel.schedulingService.SchedulingService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChartScheduling {

    private final SchedulingService schedulingService;

    @Scheduled(cron = "0 0 3 * * *")
    public void insertOilPrice() {

        String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("==== 일일 평균가 업데이트 시작 [{}] ====", startTime);

        try {
            schedulingService.saveChartData();
            schedulingService.saveAreaChartData();
        } catch (Exception e) {
            log.error("오류 발생 : ", e);
        }

        String endTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        log.info("==== 일일 평균가 업데이트 종료 [{}] ====", endTime);
    }
}
