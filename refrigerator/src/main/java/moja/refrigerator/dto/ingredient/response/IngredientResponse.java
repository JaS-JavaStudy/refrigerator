package moja.refrigerator.dto.ingredient.response;

import lombok.Data;

@Data
public class IngredientResponse {
    private long ingredientManagementPk;
    private String ingredientName;
    private String expirationDate;
    private String registrationDate;
    private int seasonDate;
    // 조회 시에는 pk 말고 이름으로 조회 되도록
    private String ingredientCategory;
    private String ingredientStorage;

    // ModelMapper 를 위한 기본 생성자 생성
    public IngredientResponse() {}

}
