package moja.refrigerator.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import moja.refrigerator.aggregate.user.TokenBlacklist;
import moja.refrigerator.repository.user.TokenBlacklistRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class LogoutFilter implements LogoutHandler {
    private final TokenBlacklistRepository tokenBlacklistRepository;

    public LogoutFilter(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("토큰이 유효하지 않습니다.");
        }

        String token = authorization.split(" ")[1];

        // 토큰을 블랙리스트에 추가
        TokenBlacklist blacklist = new TokenBlacklist();
        blacklist.setBlacklistToken(token);
        tokenBlacklistRepository.save(blacklist);
    }
}
