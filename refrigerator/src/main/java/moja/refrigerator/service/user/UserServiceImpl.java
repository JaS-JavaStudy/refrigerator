package moja.refrigerator.service.user;

import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.user.request.UserCreateRequest;
import moja.refrigerator.dto.user.request.UserUpdateRequest;
import moja.refrigerator.exception.user.DuplicateUserException;
import moja.refrigerator.repository.user.TokenBlacklistRepository;
import moja.refrigerator.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

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