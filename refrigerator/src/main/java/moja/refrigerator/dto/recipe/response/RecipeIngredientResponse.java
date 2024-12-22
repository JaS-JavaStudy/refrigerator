package moja.refrigerator.dto.recipe.response;

import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;

@Data
public class RecipeIngredientResponse {
    private IngredientManagement ingredientManagement;
    private boolean ingredientIsNecessary;
}
