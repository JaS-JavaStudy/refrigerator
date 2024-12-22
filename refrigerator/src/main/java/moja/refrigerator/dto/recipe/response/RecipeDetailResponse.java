package moja.refrigerator.dto.recipe.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeSource;
import moja.refrigerator.dto.ingredient.response.IngredientResponse;

@Data
public class RecipeDetailResponse {
    private long recipePk;
    private String recipeName;
    private int recipeCookingTime;
    private int recipeDifficulty;
    private long recipeViews;

    private LocalDateTime recipeCreateTime;;
    private LocalDateTime recipeUpdateTime;

    private List<RecipeSource> recipeSource;
    private String userPk;
    private String recipeCategory;

    private List<IngredientResponse> ingredients;
    private List<RecipeStepResponse> recipeStep;
}
