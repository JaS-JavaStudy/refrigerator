package moja.refrigerator.dto.user.request;

import lombok.Data;

@Data
public class UserCreateRequest {
    private String userId;
    private String userPw;
    private String userEmail;
    private String userNickname;
}
