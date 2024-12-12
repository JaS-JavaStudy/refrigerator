package moja.refrigerator.service.ingredient;

//import moja.refrigerator.aggregate.ingredient.IngredientCategory;
import jakarta.persistence.EntityNotFoundException;
import moja.refrigerator.aggregate.ingredient.IngredientBookmark;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
//import moja.refrigerator.aggregate.ingredient.IngredientStorage;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.ingredient.request.IngredientCreateRequest;
//import moja.refrigerator.repository.ingredient.IngredientCategoryRepository;
import moja.refrigerator.dto.ingredient.request.RequestRegistIngredientBookmark;
import moja.refrigerator.dto.ingredient.response.ResponseRegistIngredientBookmark;
import moja.refrigerator.repository.ingredient.IngredientBookmarkRepository;
import moja.refrigerator.dto.ingredient.response.IngredientResponse;
import moja.refrigerator.repository.ingredient.IngredientManagementRepository;
//import moja.refrigerator.repository.ingredient.IngredientStorageRepository;
import moja.refrigerator.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService{

    private IngredientManagementRepository ingredientManagementRepository;
//    private IngredientStorageRepository ingredientStorageRepository;
//    private IngredientCategoryRepository ingredientCategoryRepository;
    private IngredientBookmarkRepository ingredientBookmarkRepository;
    private UserRepository userRepository;
    private ModelMapper mapper;

    @Autowired
    public IngredientServiceImpl(IngredientManagementRepository ingredientManagementRepository,
//                                 IngredientStorageRepository ingredientStorageRepository,
//                                 IngredientCategoryRepository ingredientCategoryRepository,
                                 IngredientBookmarkRepository ingredientBookmarkRepository,
                                 UserRepository userRepository,
                                 ModelMapper mapper) {
        this.ingredientManagementRepository = ingredientManagementRepository;
//        this.ingredientStorageRepository = ingredientStorageRepository;
//        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.ingredientBookmarkRepository = ingredientBookmarkRepository;
        this.userRepository = userRepository;
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

        // 재료를 JpaRepository의 save() 메소드로 DB에 저장 !
        ingredientManagementRepository.save(ingredient);
    }

    @Override

    public ResponseRegistIngredientBookmark createIngredientBookmark(RequestRegistIngredientBookmark requestBookmark) {
        User user = userRepository.findById(requestBookmark.getUserPk())
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        IngredientManagement ingredientManagement = ingredientManagementRepository
                .findById(requestBookmark.getIngredientPk())
                .orElseThrow(() -> new EntityNotFoundException("재료를 찾을 수 없습니다."));

        IngredientBookmark ingredientBookmark = new IngredientBookmark();

        ingredientBookmark.setUser(user);
        ingredientBookmark.setIngredientManagement(ingredientManagement);

        ingredientBookmarkRepository.save(ingredientBookmark);

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper.map(ingredientBookmark, ResponseRegistIngredientBookmark.class);
    }

    @Transactional(readOnly = true)
    public List<IngredientResponse> getIngredient() {
        List<IngredientManagement> ingredients = ingredientManagementRepository.findAll();

        return ingredients.stream()
                .map(ingredient -> mapper.map(ingredient, IngredientResponse.class))
                .collect(Collectors.toList());
    }

}
