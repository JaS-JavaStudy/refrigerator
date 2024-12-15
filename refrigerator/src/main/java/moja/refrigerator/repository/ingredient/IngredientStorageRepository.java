package moja.refrigerator.repository.ingredient;

import moja.refrigerator.aggregate.ingredient.IngredientStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientStorageRepository extends JpaRepository<IngredientStorage, Integer> {
}
