package moja.refrigerator.dto.recipe.request;

import lombok.Data;

@Data
public class RecipeIngredientUpdateRequest {
    private boolean ingredientIsNecessary;
    private String ingredientName;
}
