package moja.refrigerator.aggregate.recipe;

import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;

@Data
@Entity
@Table(name = "tbl_recipe_ingredient")
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_ingredient_pk")
    private long recipeIngredientPk ;

    @Column(name = "ingredient_is_necessary")
    private boolean ingredientIsNecessary ;

    @JoinColumn(name = "ingredient_management")
    @ManyToOne
    private IngredientManagement ingredientManagement;

    @JoinColumn(name = "recipe")
    @ManyToOne
    private Recipe recipe;
}