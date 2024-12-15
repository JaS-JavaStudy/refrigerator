package moja.refrigerator.controller.user;

import moja.refrigerator.dto.user.request.UserCreateRequest;
import moja.refrigerator.service.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 토큰 검증 로직 확인용
    @GetMapping("/")
    public String getMainPage() {
        return "user Controller";
    }

    // 회원 가입 처리
    @PostMapping("/auth/join")
    public void joinProcess(@RequestBody UserCreateRequest request) {
        userService.createUser(request);
    }
}
