package moja.refrigerator.dto.recipe.response;

import lombok.Data;

@Data
public class RecipeRecommendResponse {
    private long recipePk;
    private String recipeName;
    private String recipeContent;
    private int recipeCookingTime;
    private double matchRate;
    private long remainExpirationDays;
    private String urgentIngredientName;
}