package moja.refrigerator.repository.ingredient;

import moja.refrigerator.aggregate.ingredient.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Integer> {
}
