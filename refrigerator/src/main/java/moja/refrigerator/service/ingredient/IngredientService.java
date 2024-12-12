package moja.refrigerator.service.ingredient;

import moja.refrigerator.dto.ingredient.request.IngredientCreateRequest;
import moja.refrigerator.dto.ingredient.request.IngredientUpdateRequest;
import moja.refrigerator.dto.ingredient.response.IngredientResponse;

import java.util.List;

public interface IngredientService {
    void createIngredient(IngredientCreateRequest request); // 재료 등록 메서드
    List<IngredientResponse> getIngredient(); // 재료 조회 메서드
    void updateIngredient(IngredientUpdateRequest request);
    void deleteIngredient(long ingredientManagementPK);
}
