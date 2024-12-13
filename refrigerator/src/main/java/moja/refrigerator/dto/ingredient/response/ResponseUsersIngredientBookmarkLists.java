package moja.refrigerator.dto.ingredient.response;

import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;

@Data
public class ResponseUsersIngredientBookmarkLists {
    private IngredientManagement ingredientManagement;
}
