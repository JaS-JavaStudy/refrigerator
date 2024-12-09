package moja.refrigerator.service.ingredient;

import moja.refrigerator.repository.ingredient.IngredientManagementRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService{

    private IngredientManagementRepository ingredientManagementRepository;
    private ModelMapper mapper;

    @Autowired
    public IngredientServiceImpl(IngredientManagementRepository ingredientManagementRepository,
                                 ModelMapper mapper) {
        this.ingredientManagementRepository = ingredientManagementRepository;
        this.mapper = mapper;
    }
}
