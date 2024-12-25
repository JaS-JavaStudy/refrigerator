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

    // 전체 재료 조회 API 추가 (userPk 파라미터를 선택적으로 만듦)
    @GetMapping
    public List<IngredientResponse> getIngredient(@RequestParam(name="userPk",required = false) Long userPk) {
        if (userPk != null) {
            // 기존 로직: 특정 사용자의 냉장고 재료 조회
            return ingredientService.getIngredient(userPk);
        } else {
            // 새로운 로직: 전체 재료 목록 조회
            return ingredientService.getAllIngredients();
        }
    }

    // 재료 등록
    @PostMapping
    public void createIngredient(
            @RequestBody IngredientCreateRequest request,
            @RequestParam("userPk") Long userPk,
            @RequestParam("ingredientManagementPk") Long ingredientManagementPk) {
        ingredientService.createIngredient(request, userPk, ingredientManagementPk);
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

    @PostMapping("/alert")
    public ResponseEntity<List<ResponseAlertExpirationDate>> alertExpirationDate(
            @RequestBody RequestAlertExpirationDate requestAlertExpirationDate
    ) {
        List<ResponseAlertExpirationDate> responseAlertExpirationDate =
                ingredientService.alertExpirationDate(requestAlertExpirationDate);

        return ResponseEntity.status(HttpStatus.OK).body(responseAlertExpirationDate);
    }

}
