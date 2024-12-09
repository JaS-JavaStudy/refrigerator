package moja.refrigerator.aggregate.user;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk")
    private long userPk;
}
