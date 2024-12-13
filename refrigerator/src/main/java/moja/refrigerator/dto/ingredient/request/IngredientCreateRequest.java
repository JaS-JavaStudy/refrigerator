package moja.refrigerator.dto.ingredient.request;

import lombok.Data;

@Data
public class IngredientCreateRequest {
    private String ingredientName;
    private String expirationDate;
    private String registrationDate;
    private int seasonDate;

    private int ingredientCategoryPk;
    private int ingredientStoragePk;
}
