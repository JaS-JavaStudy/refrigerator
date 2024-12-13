package moja.refrigerator.repository.ingredient;

import moja.refrigerator.aggregate.ingredient.IngredientBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientBookmarkRepository extends JpaRepository<IngredientBookmark, Long> {
}
