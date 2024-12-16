package moja.refrigerator.service.user;

import jakarta.servlet.http.HttpServletRequest;
import moja.refrigerator.aggregate.user.TokenBlacklist;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.user.request.UserCreateRequest;
import moja.refrigerator.exception.user.DuplicateUserException;
import moja.refrigerator.repository.user.TokenBlacklistRepository;
import moja.refrigerator.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
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
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper modelMapper, TokenBlacklistRepository tokenBlacklistRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
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