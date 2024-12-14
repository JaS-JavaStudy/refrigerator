package moja.refrigerator.service.ingredient;

import jakarta.persistence.EntityNotFoundException;
import moja.refrigerator.aggregate.ingredient.IngredientBookmark;
import moja.refrigerator.aggregate.ingredient.IngredientCategory;
import moja.refrigerator.aggregate.ingredient.IngredientManagement;
import moja.refrigerator.aggregate.ingredient.IngredientStorage;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.ingredient.request.*;
import moja.refrigerator.dto.ingredient.response.*;
import moja.refrigerator.repository.ingredient.IngredientBookmarkRepository;
import moja.refrigerator.repository.ingredient.IngredientCategoryRepository;
import moja.refrigerator.repository.ingredient.IngredientManagementRepository;
import moja.refrigerator.repository.ingredient.IngredientStorageRepository;
import moja.refrigerator.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService{

    private IngredientManagementRepository ingredientManagementRepository;
    private IngredientStorageRepository ingredientStorageRepository;
    private IngredientCategoryRepository ingredientCategoryRepository;
    private IngredientBookmarkRepository ingredientBookmarkRepository;
    private UserRepository userRepository;
    private ModelMapper mapper;

    @Autowired
    public IngredientServiceImpl(IngredientManagementRepository ingredientManagementRepository,
                                 IngredientStorageRepository ingredientStorageRepository,
                                 IngredientCategoryRepository ingredientCategoryRepository,
                                 IngredientBookmarkRepository ingredientBookmarkRepository,
                                 UserRepository userRepository,
                                 ModelMapper mapper) {
        this.ingredientManagementRepository = ingredientManagementRepository;
        this.ingredientStorageRepository = ingredientStorageRepository;
        this.ingredientCategoryRepository = ingredientCategoryRepository;
        this.ingredientBookmarkRepository = ingredientBookmarkRepository;
        this.userRepository = userRepository;
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
    public void deleteIngredient(long ingredientManagementPk) {
        IngredientManagement ingredient = ingredientManagementRepository
                .findById(ingredientManagementPk)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 재료를 찾을 수 없습니다."));

        ingredientManagementRepository.delete(ingredient);
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

    @Override
    public List<ResponseUsersIngredientBookmarkLists> getUsersIngredientBookmarkLists(
            RequestIngredientBookmarkLists requestBookmarkLists) {
        List<IngredientBookmark> ingredientBookmarkLists = ingredientBookmarkRepository
                .findAllByUser_UserPk(requestBookmarkLists.getUserPk());

        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return ingredientBookmarkLists.stream().map(ingredientBookmark -> mapper
                .map(ingredientBookmark, ResponseUsersIngredientBookmarkLists.class))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseDeleteIngredientBookmark deleteIngredientBookmark(
            RequestDeleteIngredientBookmark requestDeleteBookmark) {
        IngredientBookmark ingredientBookmark = ingredientBookmarkRepository
                .findById(requestDeleteBookmark.getIngredientBookmarkPk())
                .orElseThrow(() -> new EntityNotFoundException("즐겨찾기를 찾을 수 없습니다."));

        ResponseDeleteIngredientBookmark response = new ResponseDeleteIngredientBookmark();

        try {
            ingredientBookmarkRepository.deleteById(requestDeleteBookmark.getIngredientBookmarkPk());
            String message = ingredientBookmark.getIngredientManagement()
                    .getIngredientName() + " 재료의 즐겨찾기를 삭제했습니다";
            response.setMessage(message);
            return response;
        } catch (Exception e) {
            String message =
                    ingredientBookmark.getIngredientManagement()
                            .getIngredientName() + " 재료의 즐겨찾기를 삭제 실패했습니다";
            response.setMessage(message);
            return response;
        }
    }
}
