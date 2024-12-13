package moja.refrigerator.aggregate.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_pk")
    private long recipePk;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(name = "recipe_cooking_time")
    private int recipeCookingTime;

    @Column(name = "recipe_content",columnDefinition = "TEXT") // 길이 제한을 해제하기 위해 text로 설정
    private String recipeContent;

    @CreationTimestamp
    @Column(name = "recipe_create_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime  recipeCreateTime;

    @UpdateTimestamp
    @Column(name = "recipe_update_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
    private LocalDateTime  recipeUpdateTime;

    @Column(name = "recipe_difficulty")
    private int recipeDifficulty;

    @Column(name = "recipe_views")
    private long recipeViews = 0;

    @JoinColumn(name = "user")
    @ManyToOne
    private User user;

    @OneToMany (mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)// 1개의 레시피에 여러 이미지가 들어갈 수 있으니까 수정
    private List<RecipeSource> recipeSource = new ArrayList<>() ; // 여러 Source가 들어갈 수 있으니까 list로 수정

    public void addRecipeSource(RecipeSource recipeSource) {
        this.recipeSource.add(recipeSource);
        if(recipeSource.getRecipe() != this) {
            recipeSource.setRecipe(this);
        }
    }
    @JoinColumn(name = "recipe_category")
    @ManyToOne
    private RecipeCategory recipeCategory;

}