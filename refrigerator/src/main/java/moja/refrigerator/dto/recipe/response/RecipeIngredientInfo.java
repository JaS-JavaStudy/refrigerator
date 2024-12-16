package moja.refrigerator.dto.recipe.response;

import lombok.Data;

@Data
public class RecipeIngredientInfo {
    private String ingredientName;
    private boolean isNecessary;
}