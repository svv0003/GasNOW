package project.gasnow.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewItemDTO {
    private String createdAt;
    private String stationName;
    private Double ratingScore;
}
