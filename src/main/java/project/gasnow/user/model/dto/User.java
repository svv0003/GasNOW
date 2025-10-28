package project.gasnow.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userPhone;
    private String status;
    private String userCreatedAt;
    private String userUpdatedAt;
}
