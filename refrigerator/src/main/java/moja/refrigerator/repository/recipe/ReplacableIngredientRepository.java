package moja.refrigerator.repository.recipe;

import moja.refrigerator.aggregate.recipe.RecipeIngredient;
import moja.refrigerator.aggregate.recipe.ReplacableIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplacableIngredientRepository extends JpaRepository<ReplacableIngredient, Long> {
    List<ReplacableIngredient> findByRecipeIngredient(RecipeIngredient recipeIngredient);
}