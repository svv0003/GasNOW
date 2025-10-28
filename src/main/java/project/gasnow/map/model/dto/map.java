package project.gasnow.map.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class map {
    private String GS_ID;
    private String X_COORD;
    private String Y_COORD;
    private String BRAND_CODE;
    private String STATION_NAME;
    private String ADDRESS;
    private String PHONE;
}
