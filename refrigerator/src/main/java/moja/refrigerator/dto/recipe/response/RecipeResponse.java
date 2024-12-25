package moja.refrigerator.dto.recipe.response;

import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeCategory;
import moja.refrigerator.aggregate.recipe.RecipeSource;
import moja.refrigerator.aggregate.recipe.RecipeStep;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.user.response.UserResponse;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RecipeResponse {

    private long recipePk;
    private String recipeName;
    private int recipeCookingTime;
    private int recipeDifficulty;
    private long recipeViews;
    private String recipeContent;

    private LocalDateTime recipeCreateTime;;
    private LocalDateTime recipeUpdateTime;

    private List<RecipeSource> recipeSource;

    private List<RecipeStepResponse> recipeStep;
    private UserResponse user;
    private RecipeCategory recipeCategory;
    private List<RecipeIngredientResponse> recipeIngredients;

}
