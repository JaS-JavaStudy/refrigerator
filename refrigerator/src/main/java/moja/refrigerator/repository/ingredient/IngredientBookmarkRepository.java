package moja.refrigerator.repository.ingredient;

import moja.refrigerator.aggregate.ingredient.IngredientBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientBookmarkRepository extends JpaRepository<IngredientBookmark, Long> {
    List<IngredientBookmark> findAllByUser_UserPk(long userPk);

    // 재료 PK로 북마크 삭제
    void deleteByIngredientMyRefrigerator_IngredientMyRefrigeratorPk(Long ingredientMyRefrigeratorPk);

    // 재료 PK로 북마크 찾기
    IngredientBookmark findByIngredientMyRefrigerator_IngredientMyRefrigeratorPk(Long ingredientMyRefrigeratorPk);
}