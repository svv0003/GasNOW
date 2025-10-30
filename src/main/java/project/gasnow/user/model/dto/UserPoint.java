package project.gasnow.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPoint {

    private String userId;
    private String lastLogin;
    private int currentPoint;
    private int totalEarned;
    private int totalUsed;
    private String createdAt;
    private String updatedAt;
}
