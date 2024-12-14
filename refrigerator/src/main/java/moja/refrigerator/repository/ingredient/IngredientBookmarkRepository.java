package moja.refrigerator.repository.ingredient;

import moja.refrigerator.aggregate.ingredient.IngredientBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientBookmarkRepository extends JpaRepository<IngredientBookmark, Long> {
    List<IngredientBookmark> findAllByUser_UserPk(long userPk);
}
