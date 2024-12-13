package moja.refrigerator.service.ingredient;

import moja.refrigerator.dto.ingredient.request.*;
import moja.refrigerator.dto.ingredient.response.*;

import java.util.List;

public interface IngredientService {
//     void createIngredient(IngredientCreateRequest request);

    ResponseRegistIngredientBookmark createIngredientBookmark(RequestRegistIngredientBookmark requestBookmark);

    void createIngredient(IngredientCreateRequest request); // 재료 등록 메서드
  
    List<IngredientResponse> getIngredient(); // 재료 조회 메서드
    void updateIngredient(IngredientUpdateRequest request);
    void deleteIngredient(long ingredientManagementPk);

    List<ResponseUsersIngredientBookmarkLists> getUsersIngredientBookmarkLists(
            RequestIngredientBookmarkLists requestBookmarkLists);

    ResponseDeleteIngredientBookmark deleteIngredientBookmark(RequestDeleteIngredientBookmark requestDeleteBookmark);
}
