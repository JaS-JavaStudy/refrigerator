package moja.refrigerator.aggregate.recipe;

import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.user.User;

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

    @Column(name = "recipe_create_time")
    private String recipeCreateTime;

    @Column(name = "recipe_update_time")
    private String recipeUpdateTime;

    @Column(name = "recipe_difficulty")
    private int recipeDifficulty;

    @Column(name = "recipe_views")
    private long recipeViews;

    @JoinColumn(name = "user")
    @ManyToOne
    private User user;

    @JoinColumn(name = "cooking_source")
    @ManyToOne
    private RecipeSource recipeSource;

    @JoinColumn(name = "recipe_category")
    @ManyToOne
    private RecipeCategory recipeCategory;
}