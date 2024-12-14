package moja.refrigerator.dto.recipe.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecipeResponse {

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

}
