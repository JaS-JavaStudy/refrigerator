package moja.refrigerator.dto.ingredient.request;

import lombok.Data;

@Data
public class RequestRegistIngredientBookmark {
    private long userPk;
    private long ingredientMyRefrigeratorPk;
}
