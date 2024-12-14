package moja.refrigerator.repository.ingredient;

import moja.refrigerator.aggregate.ingredient.IngredientMyRefrigerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientMyRefrigeratorRepository extends JpaRepository<IngredientMyRefrigerator, Long> {
    // 사용자 PK로 해당 사용자의 냉장고 재료 목록 조회
    List<IngredientMyRefrigerator> findByUserUserPk(Long userPk);
}