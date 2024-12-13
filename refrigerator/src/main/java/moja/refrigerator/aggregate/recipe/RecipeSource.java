package moja.refrigerator.aggregate.recipe;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(name = "recipe_source_save") // 저장위치
    private String recipeSourceSave;

    @Column(name = "recipe_source_create_time")
    private String recipeSourceCreateTime = LocalDateTime.now().toString();

    @Column(name = "recipe_source_file_name",nullable = false) // 저장 파일 명
    private String recipeSourceFileName;

    @JoinColumn(name = "recipe_source_type") // 자료타입 동영상 or 사진
    @ManyToOne
    private RecipeSourceType recipeSourceType;

}

