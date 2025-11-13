package project.gasnow.common.scheduling.schedulingModel.schedulingService;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestTemplate;
import project.gasnow.chart.model.dto.Chart;
import project.gasnow.common.scheduling.schedulingModel.schedulingDTO.ChartWrapper;
import project.gasnow.common.scheduling.schedulingModel.schedulingMapper.SchedulingMapper;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulingServiceImpl implements SchedulingService{

    private final SchedulingMapper schedulingMapper;
    private final RestTemplate restTemplate;

    @Value("${opinet.api.key}")
    private String API_Key;

    final String API_URL = "https://www.opinet.co.kr/api/";

    @Override
    @Transactional
    public void saveChartData() {

        final String NATION_API_URL = "https://www.opinet.co.kr/api/avgRecentPrice.do?out=xml&code=" + API_Key;
        log.info(NATION_API_URL);

        try {
            log.info("====전국 데이터 조회 시작====");
            // 1. API 호출
            log.info("API 호출: {}", NATION_API_URL);
            String xmlResponse = restTemplate.getForObject(NATION_API_URL, String.class);
            /*
            String fakeData = """
                    <RESULT>
                       <OIL>
                          <DATE>20251113</DATE>
                          <PRODCD>B027</PRODCD>
                          <PRICE>1662.63</PRICE>
                       </OIL>
                       <OIL>
                          <DATE>20251113</DATE>
                          <PRODCD>B034</PRODCD>
                          <PRICE>1911.79</PRICE>
                       </OIL>
                       <OIL>
                          <DATE>20251113</DATE>
                          <PRODCD>C004</PRODCD>
                          <PRICE>1294.96</PRICE>
                       </OIL>
                       <OIL>
                          <DATE>20251113</DATE>
                          <PRODCD>D047</PRODCD>
                          <PRICE>1535.68</PRICE>
                       </OIL>
                       <OIL>
                          <DATE>20251113</DATE>
                          <PRODCD>K015</PRODCD>
                          <PRICE>999.49</PRICE>
                       </OIL>
                    </RESULT>""";
            String xmlResponse = fakeData;
             */

            log.info("전국 XML 원본 데이터" + xmlResponse);

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
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error("작업 중 오류 발생", e);
        }
    }

    /**
     * 지역별 평균 유가 데이터 저장
     * - 17개 지역의 데이터를 병렬로 수집
     */
    @Override
    @Transactional
    public void saveAreaChartData() {
        List<String> areaCodes = generateAreaCodes();
        log.info("지역별 평균가 수집 시작. 대상 지역: {}개", areaCodes.size());

        try {
            // 병렬로 API 호출 및 데이터 수집
            List<Chart> allAreaCharts = areaCodes.parallelStream()
                    .map(this::fetchAreaData)
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            if (allAreaCharts.isEmpty()) {
                log.warn("저장할 지역 데이터가 없습니다.");
                return;
            }

            int affectedRows = schedulingMapper.saveAreaChartData(allAreaCharts);
            log.info("지역별 평균가 DB 저장 완료. 총 {}건 중 {}건 업데이트",
                    allAreaCharts.size(), affectedRows);

        } catch (Exception e) {
            log.error("지역별 평균가 저장 중 오류 발생", e);
            throw new RuntimeException("지역별 평균가 저장 실패", e);
        }
    }

    /**
     * 지역 코드 리스트 생성
     * @return 1~11, 14~19 지역 코드 리스트
     */
    private List<String> generateAreaCodes() {
        List<String> areaCodes = new ArrayList<>();

        for (int i = 1; i <= 11; i++) {
            areaCodes.add(String.format("%02d", i));
        }

        for (int i = 14; i <= 19; i++) {
            areaCodes.add(String.format("%02d", i));
        }

        return areaCodes;
    }

    /**
     * 특정 지역의 유가 데이터 조회
     * @param areaCode 지역 코드
     * @return 파싱된 Chart 리스트
     */
    private List<Chart> fetchAreaData(String areaCode) {
        String apiUrl = String.format("%s/areaAvgRecentPrice.do?out=xml&code=%s&area=%s",
                API_URL, API_Key, areaCode);

        try {
            log.debug("지역 API 호출: 지역코드={}", areaCode);

            String xmlResponse = restTemplate.getForObject(apiUrl, String.class);
            log.info("지역 XML 원본 데이터" + xmlResponse);
            if (xmlResponse == null || xmlResponse.isEmpty()) {
                log.warn("API 응답이 비어있습니다. 지역코드: {}", areaCode);
                return new ArrayList<>();
            }

            return parseXmlResponse(xmlResponse);

        } catch (Exception e) {
            log.error("지역 데이터 조회 실패. 지역코드: {}", areaCode, e);
            return new ArrayList<>();
        }
    }

    /**
     * XML 응답을 Chart 객체 리스트로 파싱
     * @param xmlResponse XML 문자열
     * @return 파싱된 Chart 리스트
     */
    private List<Chart> parseXmlResponse(String xmlResponse) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ChartWrapper.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        try (StringReader reader = new StringReader(xmlResponse)) {
            ChartWrapper chartWrapper = (ChartWrapper) unmarshaller.unmarshal(reader);

            return Optional.ofNullable(chartWrapper)
                    .map(ChartWrapper::getOils)
                    .filter(oils -> !oils.isEmpty())
                    .orElse(new ArrayList<>());
        }
    }
}
