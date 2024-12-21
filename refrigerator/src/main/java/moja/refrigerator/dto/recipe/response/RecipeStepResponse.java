package moja.refrigerator.dto.recipe.response;

import lombok.Data;

@Data
public class RecipeStepResponse {

    private int recipeStepOrder;
    private String recipeStepContent;
    private RecipeStepSourceResponse recipeStepSource;

}
