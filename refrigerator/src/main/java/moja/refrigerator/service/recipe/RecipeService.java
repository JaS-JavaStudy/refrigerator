package moja.refrigerator.service.recipe;

import moja.refrigerator.aggregate.recipe.Recipe;
import moja.refrigerator.dto.recipe.request.RecipeCreateRequest;
import moja.refrigerator.dto.recipe.request.RecipeLikeRequest;
import moja.refrigerator.dto.recipe.request.RecipeUpdateRequest;
import moja.refrigerator.dto.recipe.response.*;
import moja.refrigerator.repository.recipe.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    Recipe createRecipe(RecipeCreateRequest request
            ,List<MultipartFile> recipeSources
            ,List<MultipartFile> recipeStepSources
    );
    List<RecipeResponse> getAllRecipes();
    RecipeDetailResponse getRecipe(long id);
    void deleteRecipe(long recipePk);
    Recipe updateRecipe(
            RecipeUpdateRequest request
            ,List<MultipartFile> recipeSources
            ,List<MultipartFile> recipeStepSources
    );
    List<RecipeRecommendResponse> getRecommendedRecipes(Long userPk);
    RecipeRecommendResponse getRandomRecipe();
    RecipeLikeResponse toggleLikeDislike(RecipeLikeRequest request);
    RecipeLikeResponse getLikeDislike(RecipeLikeRequest request);
    List<RecipeCategoryResponse> getRecipeCategory();
    List<RecipeResponse> getRecipeLikedByUserPk(Long userPk);
}
