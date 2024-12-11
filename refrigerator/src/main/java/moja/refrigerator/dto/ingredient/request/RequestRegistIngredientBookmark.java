package moja.refrigerator.dto.ingredient.request;

import lombok.Data;

@Data
public class RequestRegistIngredientBookmark {
    private int userPk;
    private int ingredientPk;
}
