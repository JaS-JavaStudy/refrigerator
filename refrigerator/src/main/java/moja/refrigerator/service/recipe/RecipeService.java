package moja.refrigerator.service.recipe;

import moja.refrigerator.aggregate.recipe.Recipe;
import moja.refrigerator.dto.recipe.request.RecipeCreateRequest;
import moja.refrigerator.dto.recipe.request.RecipeLikeRequest;
import moja.refrigerator.dto.recipe.request.RecipeUpdateRequest;
import moja.refrigerator.dto.recipe.response.RecipeDetailResponse;
import moja.refrigerator.dto.recipe.response.RecipeLikeResponse;
import moja.refrigerator.dto.recipe.response.RecipeRecommendResponse;
import moja.refrigerator.dto.recipe.response.RecipeResponse;
import moja.refrigerator.repository.recipe.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecipeService {
    void createRecipe(RecipeCreateRequest request
            ,List<MultipartFile> files
    );
    List<RecipeResponse> getAllRecipes();
    RecipeDetailResponse getRecipe(long id);
    void deleteRecipe(long recipePk);
    void updateRecipe(
            RecipeUpdateRequest request
            ,List<MultipartFile> files
    );
    List<RecipeRecommendResponse> getRecommendedRecipes(Long userPk);
    RecipeRecommendResponse getRandomRecipe();
    RecipeLikeResponse toggleLikeDislike(RecipeLikeRequest request);
}
