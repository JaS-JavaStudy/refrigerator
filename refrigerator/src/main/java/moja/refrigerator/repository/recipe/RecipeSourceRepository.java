package moja.refrigerator.repository.recipe;

import moja.refrigerator.aggregate.recipe.RecipeSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeSourceRepository extends JpaRepository<RecipeSource,Long> {
}
