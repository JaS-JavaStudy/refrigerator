package moja.refrigerator.aggregate.ingredient;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_ingredient_category")
@Data
public class IngredientCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_category_pk")
    private int ingredientCategoryPk;

    @Column(name = "ingredient_category")
    private String ingredientCategory;
}
