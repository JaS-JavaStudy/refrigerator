package moja.refrigerator.repository.recipe;

import moja.refrigerator.aggregate.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}

