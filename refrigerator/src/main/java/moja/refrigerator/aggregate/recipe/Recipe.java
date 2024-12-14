package moja.refrigerator.aggregate.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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

    @JoinColumn(name = "recipe_source")
    @ManyToOne
    private RecipeSource recipeSource;

    @JoinColumn(name = "recipe_category")
    @ManyToOne
    private RecipeCategory recipeCategory;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredient> recipeIngredients;
}