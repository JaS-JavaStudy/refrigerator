package moja.refrigerator.dto.recipe.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
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

    private String recipeSource;
    private String userPk;
    private String recipeCategory;

    private List<IngredientResponse> ingredients;
}
