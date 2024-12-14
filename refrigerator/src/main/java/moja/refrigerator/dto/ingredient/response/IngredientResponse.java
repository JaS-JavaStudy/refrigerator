package moja.refrigerator.dto.ingredient.response;

import lombok.Data;

@Data
public class IngredientResponse {

    private int number;
    private long ingredientMyRefrigeratorPk;
    private String ingredientName;
    private int ingredientAmount;
    private String expirationDate;
    private int seasonDate;
    private String ingredientStorage;

    // ModelMapper 를 위한 기본 생성자 생성
    public IngredientResponse() {}

}
