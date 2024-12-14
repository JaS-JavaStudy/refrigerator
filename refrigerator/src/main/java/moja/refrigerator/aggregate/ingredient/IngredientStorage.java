package moja.refrigerator.aggregate.ingredient;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="tbl_ingredient_storage")
public class IngredientStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_storage_pk")
    private int ingredientStoragePk;

    @Column(name = "ingredient_storage")
    private String ingredientStorage;
}
