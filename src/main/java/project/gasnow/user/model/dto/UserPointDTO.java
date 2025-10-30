package project.gasnow.user.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPointDTO {
    private String userId;
    private String lastLogin;
    private int currentPoint;
    private int totalEarned;
    private int totalUsed;
    private String createdAt;
    private String updatedAt;
}
