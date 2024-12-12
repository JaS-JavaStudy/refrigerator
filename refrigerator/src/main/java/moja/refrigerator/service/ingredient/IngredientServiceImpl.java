package moja.refrigerator.service.ingredient;

import moja.refrigerator.aggregate.ingredient.IngredientCategory;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
import moja.refrigerator.aggregate.ingredient.IngredientStorage;
import moja.refrigerator.dto.ingredient.request.IngredientCreateRequest;
import moja.refrigerator.repository.ingredient.IngredientCategoryRepository;
import moja.refrigerator.dto.ingredient.request.IngredientUpdateRequest;
import moja.refrigerator.dto.ingredient.response.IngredientResponse;
import moja.refrigerator.repository.ingredient.IngredientManagementRepository;
import moja.refrigerator.repository.ingredient.IngredientStorageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService{

    private IngredientManagementRepository ingredientManagementRepository;
    private IngredientStorageRepository ingredientStorageRepository;
    private IngredientCategoryRepository ingredientCategoryRepository;
    private ModelMapper mapper;

    @Autowired
    public IngredientServiceImpl(IngredientManagementRepository ingredientManagementRepository,
                                 IngredientStorageRepository ingredientStorageRepository,
                                 IngredientCategoryRepository ingredientCategoryRepository,
                                 ModelMapper mapper) {
        this.ingredientManagementRepository = ingredientManagementRepository;
        this.ingredientStorageRepository = ingredientStorageRepository;
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void createIngredient(IngredientCreateRequest request) {
        IngredientManagement ingredient = mapper.map(request, IngredientManagement.class);

        IngredientCategory category = ingredientCategoryRepository.findById(request.getIngredientCategoryPk())
                        .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        IngredientStorage storage = ingredientStorageRepository.findById(request.getIngredientStoragePk())
                        .orElseThrow(() -> new IllegalArgumentException("해당 보관 방법을 찾을 수 없습니다."));

        ingredient.setIngredientCategory(category);
        ingredient.setIngredientStorage(storage);

        // 재료를 JpaRepository의 save() 메소드로 DB에 저장 !
        ingredientManagementRepository.save(ingredient);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngredientResponse> getIngredient() {
        List<IngredientManagement> ingredients = ingredientManagementRepository.findAll();

        return ingredients.stream()
                .map(ingredient -> mapper.map(ingredient, IngredientResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateIngredient(IngredientUpdateRequest request) {
        // 1. 수정할 재료 조회
        IngredientManagement ingredient = ingredientManagementRepository
                .findById(request.getIngredientManagementPk())
                .orElseThrow(() -> new IllegalArgumentException("수정할 재료를 찾을 수 없습니다."));

        // 2. 새로운 카테고리와 저장소 조회
        IngredientCategory newCategory = ingredientCategoryRepository.findById(request.getIngredientCategoryPk())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        IngredientStorage newStorage = ingredientStorageRepository.findById(request.getIngredientStoragePk())
                .orElseThrow(() -> new IllegalArgumentException("해당 보관 방법을 찾을 수 없습니다."));

        // 3. 기본 필드들 업데이트
        ingredient.setIngredientName(request.getIngredientName());
        ingredient.setExpirationDate(request.getExpirationDate());
        ingredient.setRegistrationDate(request.getRegistrationDate());
        ingredient.setSeasonDate(request.getSeasonDate());

        // 4. 연관관계 설정
        ingredient.setIngredientCategory(newCategory);
        ingredient.setIngredientStorage(newStorage);
    }

    @Override
    @Transactional
    public void deleteIngredient(long ingredientManagementPK) {
        IngredientManagement ingredient = ingredientManagementRepository
                .findById(ingredientManagementPK)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 재료를 찾을 수 없습니다."));

        ingredientManagementRepository.delete(ingredient);
    }

}
