package moja.refrigerator.aggregate.recipe;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="tbl_recipe_source")
public class RecipeSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cooking_source_pk")
    private long cookingSourcePk;

    @Column(name = "cooking_source_save")
    private String cookingSourceSave;

    @Column(name = "cooking_source_create_time")
    private String cookingSourceCreateTime;

    @Column(name = "cooking_source_file_name")
    private String cookingSourceFileName;

    @JoinColumn(name = "cooking_source_type")
    @OneToOne
    private RecipeSourceType cookingSourceType;

}

