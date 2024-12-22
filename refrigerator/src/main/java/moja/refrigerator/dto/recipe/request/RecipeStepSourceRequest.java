package moja.refrigerator.dto.recipe.request;

import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeSourceType;

import java.time.LocalDateTime;

@Data
public class RecipeStepSourceRequest {
        private long recipeStepSourcePk;
        private String recipeStepSourceSave;
        private LocalDateTime recipeStepSourceCreateTime;
        private String recipeStepSourceFileName;
        private String recipeStepSourceServername;
        private RecipeSourceType recipeStepSourceType;


}
