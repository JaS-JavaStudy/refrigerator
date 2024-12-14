package moja.refrigerator.aggregate.ingredient;

import jakarta.persistence.*;
import lombok.Data;
import moja.refrigerator.aggregate.user.User;

@Data
@Entity
@Table(name = "tbl_ingredient_my_refrigerator")
public class IngredientMyRefrigerator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_my_refrigerator_pk")
    private long ingredientMyRefrigeratorPk;

    @ManyToOne
    @JoinColumn(name = "user_pk")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ingredient_management_pk")
    private IngredientManagement ingredientManagement;

    @Column(name = "expiration_date")
    private String expirationDate;

    @Column(name = "registration_date")
    private String registrationDate;

    @Column(name = "ingredient_amount")
    private float ingredientAmount;

}
