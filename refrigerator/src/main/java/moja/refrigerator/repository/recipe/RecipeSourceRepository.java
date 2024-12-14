package moja.refrigerator.repository.recipe;

import moja.refrigerator.aggregate.recipe.RecipeSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeSourceRepository extends JpaRepository<RecipeSource,Long> {
    Optional<RecipeSource> findByRecipeSourceFileName(String recipeSourceFileName);
}
