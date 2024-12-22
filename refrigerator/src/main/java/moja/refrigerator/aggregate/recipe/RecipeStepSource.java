package moja.refrigerator.aggregate.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="tbl_recipe_step_source")
public class RecipeStepSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_step_source_pk")
    private long recipeStepSourcePk;

    @Column(name = "recipe_step_source_save") // 저장위치
    private String recipeStepSourceSave;

    @Column(name = "recipe_step_source_create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime recipeStepSourceCreateTime = LocalDateTime.now();

    @Column(name = "recipe_step_source_file_name",nullable = false) // 저장 파일 명
    private String recipeStepSourceFileName;

    @Column(name = "recipe_step_source_servername") // 서버 저장한 이름 추가.
    private String recipeStepSourceServername;

    @JoinColumn(name = "recipe_step_source_type") // 자료타입 동영상 or 사진
    @ManyToOne
    private RecipeSourceType recipeStepSourceType;

    @JsonBackReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="recipe_step_pk")
    private RecipeStep recipeStep;
}
