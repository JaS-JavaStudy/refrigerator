package moja.refrigerator.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moja.refrigerator.dto.user.request.PasswordResetRequest;
import moja.refrigerator.dto.user.request.PasswordUpdateRequest;
import moja.refrigerator.dto.user.request.UserCreateRequest;
import moja.refrigerator.dto.user.request.UserUpdateRequest;
import moja.refrigerator.service.user.FollowService;
import moja.refrigerator.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    private final FollowService followService;

    public UserController(UserService userService, FollowService followService) {
        this.userService = userService;
        this.followService = followService;
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
        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입이 완료되었습니다.");
    }

    // 회원 정보 수정
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UserUpdateRequest request) {
        userService.updateUser(request);
        return ResponseEntity.ok().body("회원 정보가 수정되었습니다.");
    }

    // 비밀번호 재발급
    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.ok().body("임시 비밀번호가 이메일로 발송되었습니다.");
    }

    // 비밀번호 재설정
    @PutMapping("/password/update")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(request);
        return ResponseEntity.ok().body("비밀번호가 변경되었습니다.");
    }

    // 팔로우 및 언팔로우
    @PostMapping("/follow/{userPk}")
    public ResponseEntity<?> toggleFollow(@PathVariable Long userPk) {
        followService.toggleFollow(userPk);
        return ResponseEntity.ok().body("팔로우 상태가 변경되었습니다.");
    }

    // 회원 탈퇴
    @PostMapping("/delete")
    public ResponseEntity<?> delete(HttpServletRequest request, HttpServletResponse response) {
        userService.deleteUser(request, response);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
