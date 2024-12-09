package moja.refrigerator.aggregate.recipe;

import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;

@Data
@Entity
@Table(name = "tbl_replacable_ingredient")
public class ReplacableIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "replaceable_ingredient_pk")
    private long ReplaceableIngredientPk ;

    @JoinColumn(name = "ingredient_management")
    @ManyToOne
    private IngredientManagement ingredientManagement;

    @JoinColumn(name = "recipe_ingredient")
    @ManyToOne
    private RecipeIngredient RecipeIngredient ;
}
