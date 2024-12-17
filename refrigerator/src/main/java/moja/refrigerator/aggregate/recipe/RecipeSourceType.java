package moja.refrigerator.aggregate.recipe;


import jakarta.persistence.*;
import lombok.Data;

@Table(name = "tbl_recipe_source_type")
@Entity
@Data
public class RecipeSourceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_source_type_pk")
    private int recipeSourceTypePk;

    @Column(name = "recipe_source_type",length = 50)
    private String recipeSourceType;
}
