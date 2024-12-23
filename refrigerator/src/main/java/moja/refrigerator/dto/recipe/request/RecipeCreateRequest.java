package moja.refrigerator.dto.recipe.request;

import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeIngredient;
import moja.refrigerator.aggregate.recipe.RecipeStep;

import java.util.List;

@Data
public class RecipeCreateRequest {
    private String recipeName;
    private int recipeCookingTime;
    private int recipeDifficulty;
    private String recipeContent;
    private int recipeCategoryPk;
    private List<RecipeStepRequest> recipeSteps;
    private long userPk;
    private List<RecipeIngredientCreateRequest> recipeIngredients;

}
