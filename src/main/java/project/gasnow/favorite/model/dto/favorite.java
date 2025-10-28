package project.gasnow.favorite.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class favorite {
    private String USER_ID;
    private String GS_ID;
    private String CREATED_AT;
}
