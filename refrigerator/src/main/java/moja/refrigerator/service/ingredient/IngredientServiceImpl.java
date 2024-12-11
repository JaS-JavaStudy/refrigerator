package moja.refrigerator.service.ingredient;

//import moja.refrigerator.aggregate.ingredient.IngredientCategory;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
//import moja.refrigerator.aggregate.ingredient.IngredientStorage;
import moja.refrigerator.dto.ingredient.request.IngredientCreateRequest;
//import moja.refrigerator.repository.ingredient.IngredientCategoryRepository;
import moja.refrigerator.dto.ingredient.request.RequestRegistIngredientBookmark;
import moja.refrigerator.dto.ingredient.response.ResponseRegistIngredientBookmark;
import moja.refrigerator.repository.ingredient.IngredientBookmarkRepository;
import moja.refrigerator.repository.ingredient.IngredientManagementRepository;
//import moja.refrigerator.repository.ingredient.IngredientStorageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngredientServiceImpl implements IngredientService{

    private IngredientManagementRepository ingredientManagementRepository;
//    private IngredientStorageRepository ingredientStorageRepository;
//    private IngredientCategoryRepository ingredientCategoryRepository;
    private IngredientBookmarkRepository ingredientBookmarkRepository;
    private ModelMapper mapper;

    @Autowired
    public IngredientServiceImpl(IngredientManagementRepository ingredientManagementRepository,
//                                 IngredientStorageRepository ingredientStorageRepository,
//                                 IngredientCategoryRepository ingredientCategoryRepository,
                                 IngredientBookmarkRepository ingredientBookmarkRepository,
                                 ModelMapper mapper) {
        this.ingredientManagementRepository = ingredientManagementRepository;
//        this.ingredientStorageRepository = ingredientStorageRepository;
//        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.ingredientBookmarkRepository = ingredientBookmarkRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void createIngredient(IngredientCreateRequest request) {
        IngredientManagement ingredient = mapper.map(request, IngredientManagement.class);

//        IngredientCategory category = ingredientCategoryRepository.findById(request.getIngredientCategoryPk())
//                        .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
//
//        IngredientStorage storage = ingredientStorageRepository.findById(request.getIngredientStoragePk())
//                        .orElseThrow(() -> new IllegalArgumentException("저장소를 찾을 수 없습니다."));
//
//        ingredient.setIngredientCategory(category);
//        ingredient.setIngredientStorage(storage);

        ingredientManagementRepository.save(ingredient);
    }

    @Override
    public ResponseRegistIngredientBookmark createIngredientBookmark(RequestRegistIngredientBookmark requestBookmark) {

        return new ResponseRegistIngredientBookmark();
    }
}
