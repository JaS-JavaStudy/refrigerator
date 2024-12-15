package moja.refrigerator.dto.recipe.request;

import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeCategory;
import moja.refrigerator.aggregate.recipe.RecipeSource;
import moja.refrigerator.aggregate.user.User;

@Data
public class RecipeCreateRequest {
    private String recipeName;
    private int recipeCookingTime;
    private int recipeDifficulty;
    private String recipeContent;
//    private long recipeSource;
    private int recipeCategoryPk;
    private long userPk;

//    private long recipePk; // 자동 추가
//    private String recipeCreateTime; //자동 추가
//    private String recipeUpdateTime; //자동 추가
//    private long recipeViews; // 조회 시 올리는 것으로
}
