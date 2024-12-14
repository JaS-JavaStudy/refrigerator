package moja.refrigerator.dto.recipe.response;

import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeCategory;
import moja.refrigerator.aggregate.recipe.RecipeSource;
import moja.refrigerator.aggregate.user.User;

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
    private User user;
    private RecipeCategory recipeCategory;

}
