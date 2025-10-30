package project.gasnow.chart.model.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChartServiceImpl implements ChartService {

    /*
    private final ChartMapper chartMapper; // MyBatis나 JPA 저장용

    public ChartService(ChartMapper chartMapper) {
        this.chartMapper = chartMapper;
    }

    public void fetchAndSaveCharts() {
        String url = "https://example.com/api/chart.xml";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ChartWrapper> response = restTemplate.getForEntity(url, ChartWrapper.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            List<Chart> charts = response.getBody().getOils();

            for (Chart chart : charts) {
                chart.setCreatedAt(LocalDateTime.now().toString());
                chartMapper.insertChart(chart);
            }
        }
    }
     */
}
