package moja.refrigerator.aggregate.recipe;


import jakarta.persistence.*;
import lombok.Data;

@Table(name = "tbl_recipe_source_type")
@Entity
@Data
public class RecipeSourceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cooking_source_type_pk")
    private int cookingSourceTypePk;

    @Column(name = "cooking_source_type",length = 50)
    private String cookingSourceType;
}
