package moja.refrigerator.aggregate.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
import moja.refrigerator.aggregate.ingredient.IngredientMyRefrigerator;

@Data
@Entity
@Table(name = "tbl_recipe_ingredient")
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_ingredient_pk")
    private long recipeIngredientPk ;

    @Column(name = "ingredient_is_necessary")
    private boolean ingredientIsNecessary;

    @JoinColumn(name = "ingredient_management")
    @ManyToOne
    private IngredientManagement ingredientManagement;
//    private IngredientMyRefrigerator ingredientMyRefrigerator ;

    @ManyToOne
    @JoinColumn(name = "recipe")
    @JsonBackReference
    private Recipe recipe;
}
