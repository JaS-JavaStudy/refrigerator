package moja.refrigerator.dto.ingredient.request;

import lombok.Data;

@Data
public class IngredientUpdateRequest {

    private long ingredientMyRefrigeratorPk;
    private float ingredientAmount;
    private String expirationDate;
    private String registrationDate;

}
