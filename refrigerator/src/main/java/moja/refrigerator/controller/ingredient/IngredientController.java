package moja.refrigerator.controller.ingredient;

import moja.refrigerator.dto.ingredient.request.IngredientCreateRequest;
import moja.refrigerator.service.ingredient.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public void createIngredient(@RequestBody IngredientCreateRequest request) {
        ingredientService.createIngredient(request);
    }
}
