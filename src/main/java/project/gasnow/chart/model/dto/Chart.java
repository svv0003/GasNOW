package project.gasnow.chart.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chart {

    private String priceDate;
    private String oilCategory;
    private int avgPrice;
    private String createdAt;

}
