package moja.refrigerator.dto.ingredient.response;

import lombok.Data;

@Data
public class IngredientResponse {
    private long ingredientManagementPk;
    private String ingredientName;
    private String expirationDate;
    private String registrationDate;
    private int seasonDate;

    // ModelMapper를 위한 기본 생성자 생성
    public IngredientResponse() {}

    // 생성자
    public IngredientResponse(long ingredientManagementPk, String ingredientName, String expirationDate, String registrationDate, int seasonDate) {
        this.ingredientManagementPk = ingredientManagementPk;
        this.ingredientName = ingredientName;
        this.expirationDate = expirationDate;
        this.registrationDate = registrationDate;
        this.seasonDate = seasonDate;
    }
}
