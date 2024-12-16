package moja.refrigerator.aggregate.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_token_blacklist")
@Data
public class TokenBlacklist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blacklistPk;

    @Column(nullable = false)
    private String blacklistToken;
}
