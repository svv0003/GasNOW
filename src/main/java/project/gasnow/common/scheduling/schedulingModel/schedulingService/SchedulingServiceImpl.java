package project.gasnow.common.scheduling.schedulingModel.schedulingService;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.gasnow.common.scheduling.schedulingModel.schedulingDTO.ChartWrapper;
import project.gasnow.common.scheduling.schedulingModel.schedulingMapper.SchedulingMapper;

import java.io.StringReader;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService{

    private final SchedulingMapper schedulingMapper;
    private final RestTemplate restTemplate;

    @Value("${opinet.api.key}")
    private static String API_Key;
    private static final String API_URL = "http://www.opinet.co.kr/api/avgRecentPrice.do?out=xml&code=" + API_Key;

    @Override
    public void saveChartData() {
        try {
            // 1. API 호출
            log.info("API 호출: {}", API_URL);
            String xmlResponse = restTemplate.getForObject(API_URL, String.class);

            if (xmlResponse == null || xmlResponse.isEmpty()) {
                log.warn("API 응답이 비어있습니다.");
                return;
            }

            // 2. XML 파싱
            // JAXB 기능을 사용하기 위한 진입점     ChartWrapper 클래스를 기준으로 변역 작업할 것이라고 선언한다.
            JAXBContext jaxbContext = JAXBContext.newInstance(ChartWrapper.class);

            // JAXBContext의 번역 기능을
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xmlResponse);
            ChartWrapper chartWrapper = (ChartWrapper) unmarshaller.unmarshal(reader);

            if (chartWrapper == null || chartWrapper.getOils() == null || chartWrapper.getOils().isEmpty()) {
                log.warn("파싱 데이터가 없습니다.");
                return;
            }

            // 3. MyBatis Mapper로 DB에 저장
            int affectedRows = schedulingMapper.saveChartData(chartWrapper.getOils());
            log.info("DB 저장 완료. 총 {}건의 데이터 중 {}건 업데이트 완료.", chartWrapper.getOils().size(), affectedRows);

        } catch (Exception e) {
            log.error("작업 중 오류 발생", e);
        }
    }

}
