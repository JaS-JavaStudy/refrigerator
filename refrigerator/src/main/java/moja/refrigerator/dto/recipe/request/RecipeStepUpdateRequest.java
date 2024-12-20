package moja.refrigerator.dto.recipe.request;

import lombok.Data;

@Data
public class RecipeStepUpdateRequest {
    private Long recipeStepPk;
    private int recipeStepOrder; // JSON에 있는 필드와 이름 동일해야 함
    private String recipeStepContent; // JSON의 recipeStepContent와 일치
}
