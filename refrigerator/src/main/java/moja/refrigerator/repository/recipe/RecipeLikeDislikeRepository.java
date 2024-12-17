package moja.refrigerator.repository.recipe;

import moja.refrigerator.aggregate.recipe.Recipe;
import moja.refrigerator.aggregate.recipe.RecipeLikeDislike;
import moja.refrigerator.aggregate.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RecipeLikeDislikeRepository extends JpaRepository<RecipeLikeDislike, Long> {
    Optional<RecipeLikeDislike> findByRecipeAndUser(Recipe recipe, User user);
    long countByRecipeAndLikeStatus(Recipe recipe, Boolean likeStatus);
}
