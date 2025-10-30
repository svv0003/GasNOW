package project.gasnow.common.scheduling.schedulingModel.schedulingDTO;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import project.gasnow.chart.model.dto.Chart;
import java.util.List;

// XML 태그 이름은 대문자 그대로 사용해야 한다. (@XmlElement(name="DATE") 처럼)
@XmlRootElement(name = "RESULT")
public class ChartWrapper {

    // <OIL> 태그 여러 개 → List로 받음
    private List<Chart> oils;

    @XmlElement(name = "OIL")
    public List<Chart> getOils() {
        return oils;
    }

    public void setOils(List<Chart> oils) {
        this.oils = oils;
    }

}
