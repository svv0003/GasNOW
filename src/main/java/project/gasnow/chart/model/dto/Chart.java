package project.gasnow.chart.model.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "OIL")
public class Chart {

    @XmlElement(name = "DATE")
    private String priceDate; // XML의 DATE → DB PRICE_DATE

    @XmlElement(name = "PRODCD")
    private String oilCategory; // XML의 PRODCD → DB OIL_CATEGORY

    @XmlElement(name = "PRICE")
    private double avgPrice; // XML의 PRICE → DB AVG_PRICE

    private String createdAt; // XML에는 없음 → 현재 시각으로 세팅

}
