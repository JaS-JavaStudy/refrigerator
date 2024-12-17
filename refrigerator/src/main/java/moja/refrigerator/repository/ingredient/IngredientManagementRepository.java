package moja.refrigerator.repository.ingredient;

import moja.refrigerator.aggregate.ingredient.IngredientManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientManagementRepository extends JpaRepository<IngredientManagement, Long> {

}

