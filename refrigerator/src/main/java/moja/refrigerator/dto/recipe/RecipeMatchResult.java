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
    private long remainExpirationDays;
    private String urgentIngredientName;
}