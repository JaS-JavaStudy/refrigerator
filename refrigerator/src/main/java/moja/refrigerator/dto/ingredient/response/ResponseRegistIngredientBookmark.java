package moja.refrigerator.dto.ingredient.response;

import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientBookmark;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
import moja.refrigerator.aggregate.ingredient.IngredientMyRefrigerator;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.user.response.UserResponse;

@Data
public class ResponseRegistIngredientBookmark {
    private long ingredientBookmarkPk;
    private UserResponse user;
    private IngredientMyRefrigerator ingredientMyRefrigerator;
}
