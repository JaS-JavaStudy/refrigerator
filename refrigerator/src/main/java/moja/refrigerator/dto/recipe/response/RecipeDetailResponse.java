package moja.refrigerator.dto.recipe.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeCategory;
import moja.refrigerator.aggregate.recipe.RecipeSource;
import moja.refrigerator.dto.ingredient.response.IngredientResponse;
import moja.refrigerator.dto.user.response.UserResponse;

@Data
public class RecipeDetailResponse {
    private long recipePk;
    private String recipeName;
    private int recipeCookingTime;
    private String recipeContent;
    private int recipeDifficulty;
    private long recipeViews;

    private LocalDateTime recipeCreateTime;;
    private LocalDateTime recipeUpdateTime;

    private List<RecipeSource> recipeSource;
    private UserResponse user;
    private RecipeCategory recipeCategory;

    private List<RecipeIngredientResponse> ingredients;
    private List<RecipeStepResponse> recipeStep;
}
