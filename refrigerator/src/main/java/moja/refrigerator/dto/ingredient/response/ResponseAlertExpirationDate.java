package moja.refrigerator.dto.ingredient.response;

import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;

@Data
public class ResponseAlertExpirationDate {
    private long ingredientMyRefrigeratorPk;
    private IngredientManagement ingredientManagement;
    private float ingredientAmount;
    private String expirationDate;
    private int remainDate;

    public ResponseAlertExpirationDate(long ingredientMyRefrigeratorPk, IngredientManagement ingredientManagement, float ingredientAmount, String expirationDate, int remainDate) {
        this.ingredientMyRefrigeratorPk = ingredientMyRefrigeratorPk;
        this.ingredientManagement = ingredientManagement;
        this.ingredientAmount = ingredientAmount;
        this.expirationDate = expirationDate;
        this.remainDate = remainDate;
    }
}
