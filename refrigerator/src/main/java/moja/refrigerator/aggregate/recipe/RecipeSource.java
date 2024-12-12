package moja.refrigerator.aggregate.recipe;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="tbl_recipe_source")
public class RecipeSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_source_pk")
    private long recipeSourcePk;

    @Column(name = "recipe_source_save")
    private String recipeSourceSave;

    @Column(name = "recipe_source_create_time")
    private String recipeSourceCreateTime = LocalDateTime.now().toString();

    @Column(name = "recipe_source_file_name")
    private String recipeSourceFileName;

    @JoinColumn(name = "recipe_source_type")
    @OneToOne
    private RecipeSourceType recipeSourceType;

}

