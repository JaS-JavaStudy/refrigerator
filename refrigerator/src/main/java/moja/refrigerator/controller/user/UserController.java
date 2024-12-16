package moja.refrigerator.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import moja.refrigerator.dto.user.request.UserCreateRequest;
import moja.refrigerator.dto.user.request.UserUpdateRequest;
import moja.refrigerator.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> join(@RequestBody UserCreateRequest request) {
        userService.createUser(request);
        return ResponseEntity.ok().body("회원 가입이 완료되었습니다.");
    }

    // 회원 정보 수정
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserUpdateRequest request) {
        userService.updateUser(request);
        return ResponseEntity.ok().body("회원 정보가 수정되었습니다.");
    }
}
