package moja.refrigerator.service.ingredient;

import moja.refrigerator.dto.ingredient.request.IngredientCreateRequest;

public interface IngredientService {
    void createIngredient(IngredientCreateRequest request);
}
