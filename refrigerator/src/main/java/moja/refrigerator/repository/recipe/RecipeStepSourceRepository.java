package moja.refrigerator.repository.recipe;

import moja.refrigerator.aggregate.recipe.RecipeIngredient;
import moja.refrigerator.aggregate.recipe.RecipeStepSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepSourceRepository extends JpaRepository<RecipeStepSource, Long> {
}
