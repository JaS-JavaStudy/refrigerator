package moja.refrigerator.service.user;

import moja.refrigerator.dto.user.request.UserCreateRequest;

public interface UserService {
    void createUser(UserCreateRequest request);
}
