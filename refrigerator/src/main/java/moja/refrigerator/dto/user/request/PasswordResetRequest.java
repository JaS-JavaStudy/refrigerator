package moja.refrigerator.dto.user.request;

import lombok.Data;

@Data
public class PasswordResetRequest {
    private String userEmail;
}
