package project.gasnow.rating.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class rating {
    private String GS_ID;
    private String USER_ID;
    private double RATING;
    private String REVIEW_TEXT;
    private String CREATED_AT;
}
