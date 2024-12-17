package moja.refrigerator.dto.user.request;

import lombok.Data;

@Data
public class PasswordUpdateRequest {
    private String currentPw;
    private String newPw;
}
