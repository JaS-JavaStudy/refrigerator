package moja.refrigerator.dto.recipe.request;

import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
import moja.refrigerator.aggregate.recipe.Recipe;

@Data
public class RecipeIngredientCreateRequest {

    private boolean ingredientIsNecessary;
    private String ingredientName;


}
