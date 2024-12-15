package moja.refrigerator.dto.recipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import moja.refrigerator.aggregate.recipe.Recipe;

@Data
@AllArgsConstructor
public class RecipeMatchResult {
    private Recipe recipe;
    private boolean matched;
    private double matchRate;
    private String urgentIngredient;        // 가장 시급한 재료명
    private String urgentExpirationDate;    // 해당 재료의 유통기한
}