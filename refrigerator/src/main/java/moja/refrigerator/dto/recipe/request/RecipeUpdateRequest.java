package moja.refrigerator.dto.recipe.request;


import lombok.Data;

import java.util.List;

@Data
public class RecipeUpdateRequest {
    private long recipePk;
    private String recipeName;
    private int recipeCookingTime;
    private int recipeDifficulty;

    private String recipeSource;
    private int recipeCategoryPk;
    private String userPk;
    private List<RecipeStepUpdateRequest> recipeSteps;
    private List<RecipeIngredientUpdateRequest> recipeIngredients;

}
