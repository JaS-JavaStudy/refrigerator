package moja.refrigerator.controller.recipe;

import moja.refrigerator.aggregate.recipe.Recipe;
import moja.refrigerator.dto.recipe.request.RecipeCreateRequest;
import moja.refrigerator.dto.recipe.request.RecipeUpdateRequest;
import moja.refrigerator.dto.recipe.response.RecipeDetailResponse;
import moja.refrigerator.dto.recipe.response.RecipeRecommendResponse;
import moja.refrigerator.dto.recipe.response.RecipeResponse;
import moja.refrigerator.service.recipe.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void createRecipe(
            @RequestPart RecipeCreateRequest request
            , @RequestPart (required =false) List<MultipartFile> files
    ){
        recipeService.createRecipe(request
                , files
        );
    }

    @GetMapping
    public List<RecipeResponse> getAllRecipes(){
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public RecipeDetailResponse getRecipe(@PathVariable long id){ return recipeService.getRecipe(id);}

    @DeleteMapping
    public void deleteRecipe(@RequestParam long recipePk){
        recipeService.deleteRecipe(recipePk);
    }

    @PutMapping
    public void updateRecipe(
            @RequestPart RecipeUpdateRequest request
            ,@RequestPart (required =false) List<MultipartFile> files
    ){
        recipeService.updateRecipe(request,files);
    }

    @GetMapping("/recommend")
    public List<RecipeRecommendResponse> getRecommendedRecipes(@RequestParam Long userPk) {
        return recipeService.getRecommendedRecipes(userPk);
    }
}
