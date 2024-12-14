package moja.refrigerator.dto.ingredient.request;

import lombok.Data;

@Data
public class IngredientCreateRequest {

    private float ingredientAmount;
    private String expirationDate;
    private String registrationDate;

}
