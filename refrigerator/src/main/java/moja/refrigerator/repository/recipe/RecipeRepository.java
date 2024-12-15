package moja.refrigerator.repository.recipe;

import moja.refrigerator.aggregate.recipe.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByRecipePk(long recipePk);
}

