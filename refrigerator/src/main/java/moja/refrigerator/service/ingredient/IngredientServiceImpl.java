package moja.refrigerator.service.ingredient;

import jakarta.persistence.EntityNotFoundException;
import moja.refrigerator.aggregate.ingredient.*;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.dto.ingredient.request.IngredientCreateRequest;
import moja.refrigerator.dto.ingredient.request.IngredientUpdateRequest;
import moja.refrigerator.dto.ingredient.request.RequestRegistIngredientBookmark;
import moja.refrigerator.dto.ingredient.response.IngredientResponse;
import moja.refrigerator.dto.ingredient.response.ResponseRegistIngredientBookmark;
import moja.refrigerator.repository.ingredient.*;
import moja.refrigerator.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService{

    private IngredientManagementRepository ingredientManagementRepository;
    private IngredientBookmarkRepository ingredientBookmarkRepository;
    private IngredientMyRefrigeratorRepository ingredientMyRefrigeratorRepository;
    private UserRepository userRepository;
    private ModelMapper mapper;

    @Autowired
    public IngredientServiceImpl(IngredientManagementRepository ingredientManagementRepository,
                                 IngredientBookmarkRepository ingredientBookmarkRepository,
                                 IngredientMyRefrigeratorRepository ingredientMyRefrigeratorRepository,
                                 UserRepository userRepository,
                                 ModelMapper mapper) {
        this.ingredientManagementRepository = ingredientManagementRepository;
        this.ingredientBookmarkRepository = ingredientBookmarkRepository;
        this.ingredientMyRefrigeratorRepository = ingredientMyRefrigeratorRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void createIngredient(IngredientCreateRequest request, Long userPk, Long ingredientManagementPk) {
        IngredientMyRefrigerator myRefrigerator = mapper.map(request, IngredientMyRefrigerator.class);

        User user = userRepository.findById(userPk)
                        .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        IngredientManagement ingredientManagement = ingredientManagementRepository.findById(ingredientManagementPk)
                        .orElseThrow(() -> new IllegalArgumentException("재료를 찾을 수 없습니다."));

        myRefrigerator.setUser(user);
        myRefrigerator.setIngredientManagement(ingredientManagement);

        // 재료를 JpaRepository의 save() 메소드로 DB에 저장 !
        ingredientMyRefrigeratorRepository.save(myRefrigerator);
    }

    @Transactional(readOnly = true)
    public List<IngredientResponse> getIngredient(Long userPk) {
        List<IngredientMyRefrigerator> ingredients = ingredientMyRefrigeratorRepository.findByUserUserPk(userPk);

        AtomicInteger counter = new AtomicInteger(1);

        return ingredients.stream()
                .map(ingredient -> {
                    IngredientResponse response = mapper.map(ingredient, IngredientResponse.class);
                    response.setNumber(counter.getAndIncrement());
                    response.setIngredientName(ingredient.getIngredientManagement().getIngredientName());
                    response.setSeasonDate(ingredient.getIngredientManagement().getSeasonDate());
                    response.setIngredientStorage(ingredient.getIngredientManagement().getIngredientStorage().getIngredientStorage());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateIngredient(IngredientUpdateRequest request) {
        IngredientMyRefrigerator ingredient = ingredientMyRefrigeratorRepository
                .findById(request.getIngredientMyRefrigeratorPk())
                .orElseThrow(() -> new EntityNotFoundException("수정할 재료를 찾을 수 없습니다."));

        mapper.map(request, ingredient);
    }

    @Override
    @Transactional
    public void deleteIngredient(long ingredientMyRefrigeratorPk) {
        if (!ingredientMyRefrigeratorRepository.existsById(ingredientMyRefrigeratorPk)) {
            throw new EntityNotFoundException("삭제할 재료를 찾을 수 없습니다.");
        }
        ingredientMyRefrigeratorRepository.deleteById(ingredientMyRefrigeratorPk);
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


}
