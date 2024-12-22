package moja.refrigerator.aggregate.recipe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_recipe_step")
public class RecipeStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_step_pk")
    private long recipeStepPk;

    @Column(name = "recipe_step_order")
    private int recipeStepOrder;

    @Column(name = "recipe_step_content")
    private String recipeStepContent;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "recipe_step_source_pk")
    private RecipeStepSource recipeStepSource;

    @ManyToOne
    @JoinColumn(name = "recipe_pk")
    @JsonBackReference
    private Recipe recipe;

}
