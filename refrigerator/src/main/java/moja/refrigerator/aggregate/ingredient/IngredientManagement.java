package moja.refrigerator.aggregate.ingredient;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_ingredient_management")
public class IngredientManagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_management_pk")
    private long ingredientManagementPk;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @Column(name = "season_date")
    private int seasonDate;

    @JoinColumn(name = "ingredient_category")
    @ManyToOne
    private IngredientCategory ingredientCategory;

    @JoinColumn(name = "ingredient_storage")
    @ManyToOne
    private IngredientStorage ingredientStorage;
}
