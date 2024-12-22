package moja.refrigerator.repository.recipe;

import moja.refrigerator.aggregate.recipe.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

}
