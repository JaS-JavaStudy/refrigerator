package moja.refrigerator.dto.recipe.request;

import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.recipe.RecipeSourceType;

@Data
public class RecipeSourceCreateRequest {
    private String recipeSourceFileName;
    private String recipeSourceSave;
    private RecipeSourceType recipeSourceType;

//    private long recipeSourcePk;
//    private String recipeSourceCreateTime;
}
