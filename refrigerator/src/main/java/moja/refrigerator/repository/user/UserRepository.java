package moja.refrigerator.repository.user;

import moja.refrigerator.aggregate.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
