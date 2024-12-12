package moja.refrigerator.dto.ingredient.response;

import lombok.Data;
import moja.refrigerator.aggregate.ingredient.IngredientBookmark;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
import moja.refrigerator.aggregate.user.User;

@Data
public class ResponseRegistIngredientBookmark {
    private long ingredientBookmarkPk;
    private User user;
    private IngredientManagement ingredientManagement;
}
