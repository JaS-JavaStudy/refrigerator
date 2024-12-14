package moja.refrigerator.service.recipe;


import moja.refrigerator.aggregate.recipe.Recipe;
import moja.refrigerator.aggregate.recipe.RecipeCategory;
import moja.refrigerator.aggregate.recipe.RecipeSource;
import moja.refrigerator.dto.ingredient.response.IngredientResponse;
import moja.refrigerator.dto.recipe.request.RecipeCreateRequest;
import moja.refrigerator.dto.recipe.request.RecipeUpdateRequest;
import moja.refrigerator.dto.recipe.response.RecipeDetailResponse;
import moja.refrigerator.dto.recipe.response.RecipeResponse;
import moja.refrigerator.repository.recipe.RecipeCategoryRepositoy;
import moja.refrigerator.repository.recipe.RecipeRepository;
import moja.refrigerator.repository.recipe.RecipeSourceRepository;
import moja.refrigerator.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;
    private UserRepository userRepository;
    private RecipeSourceRepository recipeSourceRepository;
    private RecipeCategoryRepositoy recipeCategoryRepositoy;
    private ModelMapper mapper;

    @Autowired
    public RecipeServiceImpl(
            RecipeRepository recipeRepository,
            ModelMapper mapper,
            RecipeSourceRepository recipeSourceRepository,
            UserRepository userRepository,
            RecipeCategoryRepositoy recipeCategoryRepositoy
    ) {
        this.recipeRepository = recipeRepository;
        this.mapper = mapper;
        this.recipeSourceRepository = recipeSourceRepository;
        this.userRepository = userRepository;
        this.recipeCategoryRepositoy = recipeCategoryRepositoy;
    }

    @Override
    @Transactional
    public void createRecipe(RecipeCreateRequest request) {

        Recipe recipe = mapper.map(request, Recipe.class);

//        //User 조회
//        User user = userRepository.findById(request.getUserId)
//                        .orElseThrow(IllegalArgumentException::new);
//        recipe.setUserPk(user);

//        //RecipeSource 조회
//        RecipeSource recipeSource = recipeSourceRepository.findById(request.getRecipeSource())
//                        .orElseThrow(IllegalArgumentException::new);
//        recipe.setRecipeSource(recipeSource);

        //RecipeCategory
//        RecipeCategory recipeCategory = .findById(request.getRecipeCategory())
//                        .orElseThrow(IllegalArgumentException::new);
//        recipe.setRecipeCategory(recipeCategory);

        recipeRepository.save(recipe);
    }

    @Override
    public List<RecipeResponse> getAllRecipes(){
        return recipeRepository.findAll().stream()
                .map(recipe -> mapper.map(recipe, RecipeResponse.class))
                .collect(Collectors.toList());
    }

    public RecipeDetailResponse getRecipe(long id){
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("recipe not found"));
        RecipeDetailResponse response = mapper.map(recipe,RecipeDetailResponse.class);
        return response;
    }

                                          @Override
    public void deleteRecipe(long recipePk) {
        Recipe recipe = recipeRepository.findByRecipePk(recipePk)
                .orElseThrow(IllegalArgumentException::new);
        recipeRepository.delete(recipe);
    }

    @Override
    @Transactional
    public void updateRecipe(RecipeUpdateRequest request) {
        Recipe recipe = recipeRepository.findByRecipePk(request.getRecipePk())
                .orElseThrow(IllegalArgumentException::new);

        if (request.getRecipeName() != null) recipe.setRecipeName(request.getRecipeName());
        if (request.getRecipeCookingTime() != 0) recipe.setRecipeCookingTime(request.getRecipeCookingTime());
        if (request.getRecipeDifficulty() != 0) recipe.setRecipeDifficulty(request.getRecipeDifficulty());
        if (request.getRecipeSource() != null) recipe.setRecipeSource(recipeSourceRepository.findByRecipeSourceFileName(request.getRecipeSource())
                .orElseThrow(IllegalArgumentException::new));
        if (request.getRecipeCategory() != null) recipe.setRecipeCategory(recipeCategoryRepositoy.findByRecipeCategory(request.getRecipeCategory())
                .orElseThrow(IllegalArgumentException::new));

        recipeRepository.save(recipe);

    }
}