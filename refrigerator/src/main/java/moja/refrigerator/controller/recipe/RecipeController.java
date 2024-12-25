package moja.refrigerator.controller.recipe;

import moja.refrigerator.aggregate.recipe.Recipe;
import moja.refrigerator.aggregate.recipe.RecipeCategory;
import moja.refrigerator.dto.recipe.request.RecipeCreateRequest;
import moja.refrigerator.dto.recipe.request.RecipeLikeRequest;
import moja.refrigerator.dto.recipe.request.RecipeUpdateRequest;
import moja.refrigerator.dto.recipe.response.*;
import moja.refrigerator.service.recipe.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/recipe")
public class RecipeController {
    private RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public Recipe createRecipe(
            @RequestPart (name="request") RecipeCreateRequest request
            ,@RequestPart (name="recipeSources",required =false) List<MultipartFile> recipeSources
            ,@RequestPart (name="recipeStepSources",required =false) List<MultipartFile> recipeStepSources
    ){

        return recipeService.createRecipe(request
                , recipeSources
                , recipeStepSources
        );
    }

    @GetMapping
    public List<RecipeResponse> getAllRecipes(){
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public RecipeDetailResponse getRecipe(@PathVariable("id") long id){ return recipeService.getRecipe(id);}

    @GetMapping("/category")
    public List<RecipeCategoryResponse> getRecipeCategory(){
        return recipeService.getRecipeCategory();
    }

    @DeleteMapping
    public void deleteRecipe(@RequestParam("recipePk") long recipePk){
        recipeService.deleteRecipe(recipePk);
    }

    @PutMapping
    public Recipe updateRecipe(
            @RequestPart RecipeUpdateRequest request
            ,@RequestPart (name="recipeSources",required =false) List<MultipartFile> recipeSources
            ,@RequestPart (name="recipeStepSources",required =false) List<MultipartFile> recipeStepSources
    ){
        return recipeService.updateRecipe(request,recipeSources,recipeStepSources);
    }

    @GetMapping("/recommend")
    public List<RecipeRecommendResponse> getRecommendedRecipes(@RequestParam("userPk") Long userPk) {
        return recipeService.getRecommendedRecipes(userPk);
    }

    @GetMapping("/random")
    public ResponseEntity<RecipeRecommendResponse> getRandomRecipe() {
        try {
            RecipeRecommendResponse randomRecipe = recipeService.getRandomRecipe();
            return ResponseEntity.ok(randomRecipe);
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/reaction")
    public ResponseEntity<RecipeLikeResponse> toggleLikeDislike(
            @RequestBody RecipeLikeRequest request
    ) {
        return ResponseEntity.ok(recipeService.toggleLikeDislike(request));
    }
    @GetMapping("/status")
    public boolean getLikeStatus(
            @RequestParam("userPk") Long userPk, @RequestParam("recipePk") Long recipePk
    ) {
        return recipeService.getLikeStatus(userPk,recipePk);
    }
    @GetMapping("/reaction")
    public ResponseEntity<RecipeLikeResponse> getLikeDislike(
            @RequestBody RecipeLikeRequest request
    ) {
        return ResponseEntity.ok(recipeService.getLikeDislike(request));
    }

    @GetMapping("/liked")
    public List<RecipeResponse> getLikedRecipes(@RequestParam("userPk") Long userPk) {
        return recipeService.getRecipeLikedByUserPk(userPk);
    }

}
