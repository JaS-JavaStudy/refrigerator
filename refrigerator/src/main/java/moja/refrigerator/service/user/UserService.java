package moja.refrigerator.service.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moja.refrigerator.dto.user.request.PasswordResetRequest;
import moja.refrigerator.dto.user.request.PasswordUpdateRequest;
import moja.refrigerator.dto.user.request.UserCreateRequest;
import moja.refrigerator.dto.user.request.UserUpdateRequest;

public interface UserService {
    void createUser(UserCreateRequest request);
    void updateUser(UserUpdateRequest request);
    void resetPassword(PasswordResetRequest request);
    void updatePassword(PasswordUpdateRequest request);
    void deleteUser(HttpServletRequest request, HttpServletResponse response);
}
