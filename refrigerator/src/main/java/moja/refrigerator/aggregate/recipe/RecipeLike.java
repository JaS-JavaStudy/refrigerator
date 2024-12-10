package moja.refrigerator.aggregate.recipe;


import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.user.User;

@Data
@Entity
@Table(name="tbl_recipe_like")
public class RecipeLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_pk")
    private long likePk;

    @JoinColumn(name = "user")
    @ManyToOne
    private User user;

    @JoinColumn(name = "recipe")
    @ManyToOne
    private Recipe recipe;
}
