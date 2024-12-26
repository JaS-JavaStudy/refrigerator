package moja.refrigerator.aggregate.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeLikeDislike;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "tbl_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pk")
    private long userPk;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_pw", nullable = false)
    private String userPw;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_nickname", nullable = false, unique = true)
    private String userNickname;

    @Column(name = "join_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate joinDate;

    @Column(name = "user_role")
    private String userRole = "ROLE_USER";

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE)
    private List<Follow> following;

    @OneToMany(mappedBy = "following", cascade = CascadeType.REMOVE)
    private List<Follow> followers;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    private List<RecipeLikeDislike> recipeLikeDislike;

}
