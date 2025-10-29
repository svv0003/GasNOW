package project.gasnow.rating.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    private String gsId;
    private String userId;
    private double rating;
    private String createdAt;
}
