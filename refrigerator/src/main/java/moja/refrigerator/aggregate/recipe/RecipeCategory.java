package moja.refrigerator.aggregate.recipe;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="tbl_recipe_category")
public class RecipeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_category_pk")
    private int RecipeCategoryPK;

    @Column(name = "recipe_category")
    private String recipeCategory;
}
