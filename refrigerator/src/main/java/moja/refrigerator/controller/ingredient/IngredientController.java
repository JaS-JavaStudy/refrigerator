package moja.refrigerator.controller.ingredient;

import moja.refrigerator.dto.ingredient.request.IngredientCreateRequest;
import moja.refrigerator.dto.ingredient.request.IngredientUpdateRequest;
import moja.refrigerator.dto.ingredient.request.RequestRegistIngredientBookmark;
import moja.refrigerator.dto.ingredient.response.IngredientResponse;
import moja.refrigerator.dto.ingredient.response.ResponseRegistIngredientBookmark;
import moja.refrigerator.service.ingredient.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    // 재료 등록
    @PostMapping
    public void createIngredient(
            @RequestBody IngredientCreateRequest request,
            @RequestParam Long userPk,
            @RequestParam Long ingredientManagementPk) {
        ingredientService.createIngredient(request, userPk, ingredientManagementPk);
    }

    // 재료 조회
    @GetMapping
    public List<IngredientResponse> getIngredient(@RequestParam Long userPk) {
        return ingredientService.getIngredient(userPk);
    }

    // 재료 정보 수정
    @PutMapping
    public void updateIngredient(@RequestBody IngredientUpdateRequest request) {
        ingredientService.updateIngredient(request);
    }

    // 재료 삭제
    @DeleteMapping
    public void deleteIngredient(@RequestParam Long ingredientMyRefrigeratorPk) {
        ingredientService.deleteIngredient(ingredientMyRefrigeratorPk);
    }

    @PostMapping("/bookmark/regist")
    public ResponseEntity<ResponseRegistIngredientBookmark> createIngredientBookmark(
            @RequestBody RequestRegistIngredientBookmark requestBookmark
            ) {
        ResponseRegistIngredientBookmark responseBookmark =
                ingredientService.createIngredientBookmark(requestBookmark);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBookmark);
    }

}
