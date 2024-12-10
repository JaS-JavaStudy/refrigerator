package moja.refrigerator.service.recipe;


import moja.refrigerator.repository.recipe.RecipeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;
    private ModelMapper mapper;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository, ModelMapper mapper) {
        this.recipeRepository = recipeRepository;
        this.mapper = mapper;
    }
}