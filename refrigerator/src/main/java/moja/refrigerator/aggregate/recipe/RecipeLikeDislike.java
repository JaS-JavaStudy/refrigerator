package moja.refrigerator.aggregate.recipe;

import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.user.User;

@Entity
@Table(name = "tbl_recipe_like_dislike")
@Data
public class RecipeLikeDislike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_dislike_pk")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_pk")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "user_pk")
    private User user;

    @Column(name = "like_status")
    private Boolean likeStatus;
}

