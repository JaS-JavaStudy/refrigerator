package moja.refrigerator.dto.ingredient.response;

import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
import moja.refrigerator.aggregate.ingredient.IngredientMyRefrigerator;

@Data
public class ResponseUsersIngredientBookmarkLists {
    private IngredientMyRefrigerator ingredientMyRefrigerator;
}
