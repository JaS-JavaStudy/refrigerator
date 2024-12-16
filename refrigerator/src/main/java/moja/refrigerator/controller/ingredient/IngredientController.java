package moja.refrigerator.controller.ingredient;

import jakarta.persistence.EntityNotFoundException;
import moja.refrigerator.dto.ingredient.request.*;
import moja.refrigerator.dto.ingredient.response.*;
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
    public ResponseEntity<String> deleteIngredient(@RequestBody IngredientDeleteRequest request) {
        try {
            ingredientService.deleteIngredient(request);
            return ResponseEntity.ok("재료가 성공적으로 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/bookmark/regist")
    public ResponseEntity<ResponseRegistIngredientBookmark> createIngredientBookmark(
            @RequestBody RequestRegistIngredientBookmark requestBookmark
            ) {
        ResponseRegistIngredientBookmark responseBookmark =
                ingredientService.createIngredientBookmark(requestBookmark);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseBookmark);
    }

    @GetMapping("/bookmark")
    public ResponseEntity<List<ResponseUsersIngredientBookmarkLists>> getUsersIngredientBookmarkList(
            @RequestBody RequestIngredientBookmarkLists requestBookmarkLists
    ) {
        List<ResponseUsersIngredientBookmarkLists> responseBookmarkLists =
                ingredientService.getUsersIngredientBookmarkLists(requestBookmarkLists);

        return ResponseEntity.status(HttpStatus.OK).body(responseBookmarkLists);
    }

    @DeleteMapping("/bookmark/delete")
    public ResponseEntity<ResponseDeleteIngredientBookmark> deleteIngredientBookmark(
            @RequestBody RequestDeleteIngredientBookmark requestDeleteBookmark
    ) {

        ResponseDeleteIngredientBookmark responseDeleteIngredientBookmark =
                ingredientService.deleteIngredientBookmark(requestDeleteBookmark);

        System.out.println(responseDeleteIngredientBookmark.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(responseDeleteIngredientBookmark);
    }

    @GetMapping("/alert")
    public ResponseEntity<ResponseAlertExpirationDate> alertExpirationDate(
            @RequestBody RequestAlertExpirationDate requestAlertExpirationDate
    ) {
        ResponseAlertExpirationDate responseAlertExpirationDate =
                ingredientService.alertExpirationDate(requestAlertExpirationDate);

        return ResponseEntity.status(HttpStatus.OK).body(responseAlertExpirationDate);
    }

}
