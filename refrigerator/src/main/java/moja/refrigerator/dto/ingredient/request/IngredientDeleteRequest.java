package moja.refrigerator.dto.ingredient.request;

import lombok.Data;

@Data
public class IngredientDeleteRequest {

    private Long ingredientMyRefrigeratorPk;
    private float deleteAmount;

}
