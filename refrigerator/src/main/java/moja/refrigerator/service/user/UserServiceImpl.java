package moja.refrigerator.service.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.user.request.PasswordResetRequest;
import moja.refrigerator.dto.user.request.PasswordUpdateRequest;
import moja.refrigerator.dto.user.request.UserCreateRequest;
import moja.refrigerator.dto.user.request.UserUpdateRequest;
import moja.refrigerator.exception.user.DuplicateUserException;
import moja.refrigerator.jwt.LogoutFilter;
import moja.refrigerator.repository.user.UserRepository;
import moja.refrigerator.service.email.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final LogoutFilter logoutFilter;

    @Override
    @Transactional
    public void createUser(UserCreateRequest request) {
        // 중복 검사 로직
        checkDuplicateUser(request);

        // dto -> 엔티티 변환
        User user = modelMapper.map(request, User.class);

        // 비밀번호 암호화
        user.setUserPw(passwordEncoder.encode(request.getUserPw()));
        user.setUserRole("ROLE_USER");

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateRequest request) {
        // 현재 로그인한 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 이메일 변경 요청이 있는 경우
        if (request.getUserEmail() != null) {
            if (!request.getUserEmail().equals(user.getUserEmail())
                    && userRepository.existsByUserEmail(request.getUserEmail())) {
                throw new DuplicateUserException("이미 사용 중인 이메일입니다.");
            }
            user.setUserEmail(request.getUserEmail());
        }

        // 닉네임 변경 요청이 있는 경우
        if (request.getUserNickname() != null) {
            if (!request.getUserNickname().equals(user.getUserNickname())
                    && userRepository.existsByUserNickname(request.getUserNickname())) {
                throw new DuplicateUserException("이미 사용 중인 닉네임입니다.");
            }
            user.setUserNickname(request.getUserNickname());
        }
    }

    @Override
    @Transactional
    public void resetPassword(PasswordResetRequest request) {
        // 이메일로 사용자 찾기
        User user = userRepository.findByUserEmail(request.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 가입된 계정이 없습니다."));

        // 임시 비밀번호 생성
        String tempPassword = UUID.randomUUID().toString().substring(0, 12);

        try {
            // 이메일 발송
            emailService.sendTempPassword(user.getUserNickname(), user.getUserEmail(), tempPassword);
            // 임시 비밀번호로 업데이트
            user.setUserPw(passwordEncoder.encode(tempPassword));
        } catch (Exception e) {
            System.out.println("Detailed error: " + e.getMessage());
            throw new RuntimeException("이메일 발송에 실패했습니다.");
        }

    }

    @Override
    @Transactional
    public void updatePassword(PasswordUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 기존 비밀번호 검증
        if (!passwordEncoder.matches(request.getCurrentPw(), user.getUserPw())) {
            throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 암호화 후 저장
        user.setUserPw(passwordEncoder.encode(request.getNewPw()));
    }

    @Override
    @Transactional
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 유저 삭제
        userRepository.delete(user);

        // 로그아웃 처리 (토큰 블랙리스트에 추가)
        logoutFilter.logout(request, response, authentication);
    }

    private void checkDuplicateUser(UserCreateRequest request) {
        List<String> errors = new ArrayList<>();
        if (userRepository.existsByUserId(request.getUserId())) {
            errors.add("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByUserEmail(request.getUserEmail())) {
            errors.add("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByUserNickname(request.getUserNickname())) {
            errors.add("이미 사용 중인 닉네임입니다.");
        }

        if (!errors.isEmpty()) {
            throw new DuplicateUserException(String.join(", ", errors));
        }
    }
}