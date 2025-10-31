package project.gasnow.common.scheduling.schedulingModel.schedulingService;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.common.scheduling.schedulingModel.schedulingDTO.ChartWrapper;
import project.gasnow.common.scheduling.schedulingModel.schedulingMapper.SchedulingMapper;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService{

    private final SchedulingMapper schedulingMapper;
    private final RestTemplate restTemplate;

    @Value("${opinet.api.key}")
    private String API_Key;

    @Override
    public void saveChartData() {

        final String API_URL = "http://www.opinet.co.kr/api/avgRecentPrice.do?out=xml&code=" + API_Key;

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

    @Override
    public void saveAreaChartData() {

        final String API_URL_AREA = "https://www.opinet.co.kr/api/areaAvgRecentPrice.do?out=xml&code=" + API_Key + "&area=01&date=";

        // 지역 코드 리스트
        List<String> areaCodes = new ArrayList<>();
        // 1 ~ 11
        for (int i = 1; i <= 11; i++) {
            areaCodes.add(String.format("%02d", i)); // "1" -> "01"
        }
        // 14 ~ 19
        for (int i = 14; i <= 19; i++) {
            areaCodes.add(String.format("%02d", i));
        }
        // 17개 지역의 데이터를 누적할 리스트
        List<Chart> allAreaCharts = new ArrayList<>();

        try {

            /*
            // [수정 3] 17개 지역 코드 순회
            for (String areaCode : AREA_CODES) {
                // [수정 2] API URL을 루프 내에서 동적으로 생성
                final String API_URL_AREA = String.format(
                    "https://www.opinet.co.kr/api/areaAvgRecentPrice.do?out=xml&code=%s&area=%s",
                    API_Key, areaCode
                );

                log.info("API 호출 (지역): {}", API_URL_AREA);
                String xmlResponse = restTemplate.getForObject(API_URL_AREA, String.class);

                if (xmlResponse == null || xmlResponse.isEmpty()) {
                    log.warn("API 응답이 비어있습니다 (지역 코드: {}).", areaCode);
                    continue; // 다음 지역 코드로 넘어감
                }

                // 2. XML 파싱
                StringReader reader = new StringReader(xmlResponse);
                ChartWrapper chartWrapper = (ChartWrapper) unmarshaller.unmarshal(reader);
                reader.close(); // 리소스 정리

                if (chartWrapper != null && chartWrapper.getOils() != null && !chartWrapper.getOils().isEmpty()) {
                    // [수정 3] 파싱된 데이터를 전체 리스트에 추가
                    allAreaCharts.addAll(chartWrapper.getOils());
                } else {
                    log.warn("파싱 데이터가 없습니다 (지역 코드: {}).", areaCode);
                }

                // (참고) API 부하를 줄이기 위해 짧은 대기 시간 추가 가능
                // Thread.sleep(100);

            } // end for loop

            // 3. MyBatis Mapper로 DB에 저장 (17개 지역 데이터를 한 번에)
            if (allAreaCharts.isEmpty()) {
                log.warn("저장할 지역 데이터가 없습니다.");
                return;
            }

            // [수정 4] 올바른 매퍼 메소드 호출
            int affectedRows = schedulingMapper.saveAreaChartData(allAreaCharts);
            log.info("DB 저장 완료 (지역). 총 {}건의 데이터 중 {}건 업데이트 완료.", allAreaCharts.size(), affectedRows);

             */



            // 1. API 호출
            log.info("API 호출: {}", API_URL_AREA);
            String xmlResponse = restTemplate.getForObject(API_URL_AREA, String.class);

            if (xmlResponse == null || xmlResponse.isEmpty()) {
                log.warn("API 응답이 비어있습니다.");
                return;
            }

            // 2. XML 파싱
            // JAXB 기능을 사용하기 위한 진입점     ChartWrapper 클래스를 기준으로 변역 작업할 것이라고 선언한다.
            JAXBContext jaxbContext = JAXBContext.newInstance(ChartWrapper.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xmlResponse);
            ChartWrapper chartWrapper = (ChartWrapper) unmarshaller.unmarshal(reader);

            if (chartWrapper == null || chartWrapper.getOils() == null || chartWrapper.getOils().isEmpty()) {
                log.warn("파싱 데이터가 없습니다.");
                return;
            }

            // 3. MyBatis Mapper로 DB에 저장
            int affectedRows = schedulingMapper.saveAreaChartData(chartWrapper.getOils());
            log.info("DB 저장 완료. 총 {}건의 데이터 중 {}건 업데이트 완료.", chartWrapper.getOils().size(), affectedRows);

        } catch (Exception e) {
            log.error("작업 중 오류 발생", e);
        }
    }

}
