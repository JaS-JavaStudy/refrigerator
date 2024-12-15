package moja.refrigerator.aggregate.ingredient;

import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.user.User;

@Table(name = "tbl_ingredient_bookmark")
@Entity
@Data
public class IngredientBookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_bookmark_pk")
    private long ingredientBookmarkPk;

    @ManyToOne
    @JoinColumn(name = "user_pk")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ingredient_my_refrigerator_pk")
    private IngredientMyRefrigerator ingredientMyRefrigerator;
}
