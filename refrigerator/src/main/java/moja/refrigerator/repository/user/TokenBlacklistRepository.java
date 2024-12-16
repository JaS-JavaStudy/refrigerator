package moja.refrigerator.repository.user;

import moja.refrigerator.aggregate.user.TokenBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlacklistRepository extends JpaRepository<TokenBlacklist, Long> {
    boolean existsByBlacklistToken(String blacklistToken);
}
