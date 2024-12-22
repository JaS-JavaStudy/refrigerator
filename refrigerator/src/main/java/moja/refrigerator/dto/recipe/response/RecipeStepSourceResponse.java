package moja.refrigerator.dto.recipe.response;

import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeSourceType;

import java.time.LocalDateTime;

@Data
public class RecipeStepSourceResponse {
    private long recipeStepSourcePk;
    private String recipeStepSourceSave;
    private LocalDateTime recipeStepSourceCreateTime;
    private String recipeStepSourceFileName;
    private String recipeStepSourceServername;
    private RecipeSourceType recipeStepSourceType;
}
