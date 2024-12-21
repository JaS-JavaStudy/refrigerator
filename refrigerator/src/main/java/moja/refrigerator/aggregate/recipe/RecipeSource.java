package moja.refrigerator.aggregate.recipe;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime recipeSourceCreateTime = LocalDateTime.now();

    @Column(name = "recipe_source_file_name",nullable = false) // 저장 파일 명
    private String recipeSourceFileName;

    @Column(name = "recipe_source_servername") // 서버 저장한 이름 추가.
    private String recipeSourceServername;

    @JoinColumn(name = "recipe_source_type") // 자료타입 동영상 or 사진
    @ManyToOne
    private RecipeSourceType recipeSourceType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="recipe_pk")
    @JsonBackReference
    private Recipe recipe;

}

