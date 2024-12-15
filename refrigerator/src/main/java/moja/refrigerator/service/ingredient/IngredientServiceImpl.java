package moja.refrigerator.service.ingredient;

import jakarta.persistence.EntityNotFoundException;
import moja.refrigerator.aggregate.ingredient.*;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.repository.ingredient.*;

import moja.refrigerator.dto.ingredient.request.*;
import moja.refrigerator.dto.ingredient.response.*;

import moja.refrigerator.repository.user.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Optional;
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

        // 재료를 JpaRepository 의 save() 메소드로 DB에 저장 !
        ingredientMyRefrigeratorRepository.save(myRefrigerator);
    }

    @Transactional(readOnly = true)
    public List<IngredientResponse> getIngredient(Long userPk) {
        List<IngredientMyRefrigerator> ingredients = ingredientMyRefrigeratorRepository.findByUserUserPk(userPk);

        LocalDate currentDate = LocalDate.now();
        AtomicInteger counter = new AtomicInteger(1);

        return ingredients.stream()
                .map(ingredient -> {
                    IngredientResponse response = mapper.map(ingredient, IngredientResponse.class);
                    response.setNumber(counter.getAndIncrement());
                    response.setIngredientName(ingredient.getIngredientManagement().getIngredientName());
                    response.setSeasonDate(ingredient.getIngredientManagement().getSeasonDate());
                    response.setIngredientStorage(ingredient.getIngredientManagement().getIngredientStorage().getIngredientStorage());

                    // 현재 날짜 기준, 유통기한 남은 일수 계산
                    LocalDate expirationDate = LocalDate.parse(ingredient.getExpirationDate());
                    long remainExpirationDate = ChronoUnit.DAYS.between(currentDate, expirationDate);
                    response.setRemainExpirationDate(remainExpirationDate);

                    return response;
                })
                // 남은 일수 기준 오름차순 정렬
                .sorted(Comparator.comparingLong(IngredientResponse::getRemainExpirationDate))
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

        IngredientMyRefrigerator ingredientManagement = ingredientMyRefrigeratorRepository
                .findById(requestBookmark.getIngredientMyRefrigeratorPk())
                .orElseThrow(() -> new EntityNotFoundException("재료를 찾을 수 없습니다."));

        IngredientBookmark ingredientBookmark = new IngredientBookmark();

        ingredientBookmark.setUser(user);
        ingredientBookmark.setIngredientMyRefrigerator(ingredientManagement);

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
            String message = ingredientBookmark.getIngredientMyRefrigerator()
                    .getIngredientManagement().getIngredientName() + " 재료의 즐겨찾기를 삭제했습니다";
            response.setMessage(message);
            return response;
        } catch (Exception e) {
            String message =
                    ingredientBookmark.getIngredientMyRefrigerator()
                            .getIngredientManagement().getIngredientName() + " 재료의 즐겨찾기를 삭제 실패했습니다";
            response.setMessage(message);
            return response;
        }
    }
}
