package moja.refrigerator.repository.recipe;

import moja.refrigerator.aggregate.recipe.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeCategoryRepositoy extends JpaRepository<RecipeCategory,Integer> {

}
