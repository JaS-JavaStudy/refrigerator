package moja.refrigerator.dto.user.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String userEmail;
    private String userNickname;
}
