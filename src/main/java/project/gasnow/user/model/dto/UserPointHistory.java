package project.gasnow.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * point_history 테이블
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPointHistory {
    private int historyId;
    private String userId;
    private int pointChange;
    private String pointType;
    private String description;
    private String createdAt;
}
