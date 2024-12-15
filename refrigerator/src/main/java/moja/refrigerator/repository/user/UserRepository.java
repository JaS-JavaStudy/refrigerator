package moja.refrigerator.repository.user;

import moja.refrigerator.aggregate.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);
    boolean existsByUserEmail(String userEmail);
    boolean existsByUserNickname(String userNickname);
    Optional<User> findByUserPk(long userPk);
    Optional<User> findByUserId(String userId);
}
