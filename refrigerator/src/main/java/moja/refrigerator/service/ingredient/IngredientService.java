package moja.refrigerator.service.ingredient;

import moja.refrigerator.dto.ingredient.request.*;
import moja.refrigerator.dto.ingredient.response.*;

import java.util.List;

public interface IngredientService {

    ResponseRegistIngredientBookmark createIngredientBookmark(RequestRegistIngredientBookmark requestBookmark);

    void createIngredient(IngredientCreateRequest request, Long userPk, Long ingredientManagementPk); // 재료 등록 메서드
  
    List<IngredientResponse> getIngredient(Long userPk); // 재료 조회 메서드
    void updateIngredient(IngredientUpdateRequest request);

    void deleteIngredient(IngredientDeleteRequest request);

    List<ResponseUsersIngredientBookmarkLists> getUsersIngredientBookmarkLists(
            RequestIngredientBookmarkLists requestBookmarkLists);

    ResponseDeleteIngredientBookmark deleteIngredientBookmark(RequestDeleteIngredientBookmark requestDeleteBookmark);

    List<ResponseAlertExpirationDate> alertExpirationDate(RequestAlertExpirationDate requestAlertExpirationDate);

    List<IngredientResponse> getAllIngredients();
}
