package moja.refrigerator.service.ingredient;

import moja.refrigerator.dto.ingredient.request.IngredientCreateRequest;
import moja.refrigerator.dto.ingredient.request.RequestRegistIngredientBookmark;
import moja.refrigerator.dto.ingredient.response.ResponseRegistIngredientBookmark;

public interface IngredientService {
    void createIngredient(IngredientCreateRequest request);

    ResponseRegistIngredientBookmark createIngredientBookmark(RequestRegistIngredientBookmark requestBookmark);
}
